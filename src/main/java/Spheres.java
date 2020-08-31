import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.SplittableRandom;

public class Spheres {
    public static void main(String[] args) {
        var profile = GLProfile.get(GLProfile.GL3);
        var caps = new GLCapabilities(profile);


        var frame = new JFrame("Hi");

        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            var panel = new GLCanvas(caps);
//            var panel = new GLJPanel(caps);
//            panel.setSkipGLOrientationVerticalFlip(true);
            panel.setPreferredSize(new Dimension(600, 600));
            var basic = new BasicFrame();
            panel.addGLEventListener(basic);
            panel.addMouseMotionListener(basic);
            panel.addMouseWheelListener(basic);

            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);

//            var animator = new FPSAnimator(panel, 300);
            var animator = new Animator(panel);
            animator.setUpdateFPSFrames(60, System.err);
            animator.start();
        });
    }

    static class BasicFrame implements GLEventListener, MouseMotionListener, MouseWheelListener {
        private float rquad = 0;
        private final GLU glu = new GLU();
        private long start = System.currentTimeMillis();
        private long lastTime;

        private int width;
        private int height;
        private int mouseX;
        private int mouseY;
        private float zoom = 20.0f;
        private float pitch = 0.3f;
        private float yaw = 0.2f;

        private final FloatBuffer matBuffer = GLBuffers.newDirectFloatBuffer(16);

        private ShaderProgram shader;
        private int vao;

//        private final FloatBuffer vertices = GLBuffers.newDirectFloatBuffer(new float[]{
//                0.5f,  0.5f, 0.0f,  // top right
//                0.5f, -0.5f, 0.0f,  // bottom right
//                -0.5f, -0.5f, 0.0f,  // bottom left
//                -0.5f,  0.5f, 0.0f   // top left
//        });

        private ShaderState shaderState;

        private int N = 10000;

        private final FloatBuffer pos = genPos(N);
        private int posVbo = -1;

        private IntBuffer sphereIndices;

        private FloatBuffer genPos(int n) {
            var rand = new Random().doubles(-10, 10).iterator();
            var buf = GLBuffers.newDirectFloatBuffer(n * 3);
            var pos = new Vector3f();
            for (int i = 0; i < n; i++) {
                pos.set(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()).get(3 * i, buf);
            }
            return buf;
        }

        private void updatePos() {
            var rand = new SplittableRandom().doubles(-0.01, 0.01).iterator();
            for (int i = 0; i < pos.capacity(); i++) {
                float x = pos.get(i);
                x += rand.nextDouble();
                pos.put(i, x);
            }
        }

        @Override
        public void init(GLAutoDrawable drawable) {
            final GL3 gl = drawable.getGL().getGL3();
            gl.setSwapInterval(0);
            gl.glEnable(gl.GL_DEPTH_TEST);
//            gl.glEnable(gl.GL_CULL_FACE);
            gl.glClearColor( 0.1f, 0.1f, 0.1f, 0f );

            var vert = ShaderCode.create(gl, gl.GL_VERTEX_SHADER, 1, this.getClass(), new String[]{"sphere.vert"}, false);
            var geom = ShaderCode.create(gl, gl.GL_GEOMETRY_SHADER, 1, this.getClass(), new String[]{"sphere.geom"}, false);
            var frag = ShaderCode.create(gl, gl.GL_FRAGMENT_SHADER, 1, this.getClass(), new String[]{"sphere.frag"}, false);

            var prog = new ShaderProgram();
            prog.add(gl, vert, System.err);
            prog.add(gl, geom, System.err);
            prog.add(gl, frag, System.err);
            prog.link(gl, System.err);
            this.shader = prog;
            this.shaderState = new ShaderState();
            this.shaderState.attachShaderProgram(gl, this.shader, false);

            var vaoBuf = IntBuffer.allocate(1);
            gl.glGenVertexArrays(1, vaoBuf);
            vao = vaoBuf.get(0);
            gl.glBindVertexArray(vao);

            var vbo = IntBuffer.allocate(1);

            gl.glGenBuffers(1, vbo);
            this.posVbo = vbo.get(0);
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo.get(0));
            gl.glBufferData(gl.GL_ARRAY_BUFFER, pos.capacity() * Float.BYTES, pos, gl.GL_STREAM_DRAW);
            gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 3 * Float.BYTES, 0);
            gl.glEnableVertexAttribArray(0);

            gl.glBindVertexArray(0);

            lastTime = System.nanoTime();
        }

        @Override
        public void dispose(GLAutoDrawable drawable) {

        }

        @Override
        public void display(GLAutoDrawable drawable) {
            var start = System.nanoTime();
            final GL3 gl = drawable.getGL().getGL3();

            gl.glClearColor( 0.1f, 0.1f, 0.1f, 0f );
            gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

//            updatePos();
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, this.posVbo);
            gl.glBufferSubData(gl.GL_ARRAY_BUFFER, 0, pos.capacity() * Float.BYTES, pos);

            this.shaderState.useProgram(gl, true);

            float time = (this.start - System.currentTimeMillis()) / 1000.0f;
            long thisTime = System.nanoTime();
            float timeDiff = (float) ((this.lastTime - thisTime) / 1.0e9);
            lastTime = thisTime;

            var view = new Matrix4f()
                    .translation(0, 0, -20)
                    .rotateX(pitch)
                    .rotateY(yaw);
            var projection = new Matrix4f()
                    .perspective(((float) Math.toRadians(90.0)), ((float) width) / height, 0.1f, 100.0f);

            projection = new Matrix4f()
                    .ortho(-width / 2f / 100 * zoom, width / 2f / 100 * zoom, -height/ 2f / 100 * zoom, height / 2f / 100 * zoom, 0.01f, 100.0f);


            shaderState.uniform(gl, new GLUniformData("view", 4, 4, view.get(matBuffer)));
            shaderState.uniform(gl, new GLUniformData("projection", 4, 4, projection.get(matBuffer)));
            shaderState.uniform(gl, new GLUniformData("nearZ", 0.1f));

            var viewPos = view.transformPosition(new Vector3f()).negate();
            shaderState.uniform(gl, new GLUniformData("viewPos", 3, viewPos.get(GLBuffers.newDirectFloatBuffer(3))));

            shaderState.uniform(gl, new GLUniformData("material.ambient", 3, GLBuffers.newDirectFloatBuffer(new float[]{1.0f, 0.5f, 0.31f})));
            shaderState.uniform(gl, new GLUniformData("material.diffuse", 3, GLBuffers.newDirectFloatBuffer(new float[]{1.0f, 0.5f, 0.31f})));
            shaderState.uniform(gl, new GLUniformData("material.specular", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.5f, 0.5f, 0.5f})));
            shaderState.uniform(gl, new GLUniformData("material.shininess", 32.0f));

//            var lightDir = new Vector3f(-1, -1, -1).rotateX(time);
            var lightDir = new Vector3f(.5f, .5f, -1);
            shaderState.uniform(gl, new GLUniformData("dirLight.direction", 3, lightDir.get(GLBuffers.newDirectFloatBuffer(3))));
            shaderState.uniform(gl, new GLUniformData("dirLight.ambient", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.6f, 0.6f, 0.6f})));
            shaderState.uniform(gl, new GLUniformData("dirLight.diffuse", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.8f, 0.8f, 0.8f})));
            shaderState.uniform(gl, new GLUniformData("dirLight.specular", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.9f, 0.9f, 0.9f})));

            gl.glBindVertexArray(this.vao);

            gl.glDrawArrays(gl.GL_POINTS, 0, pos.capacity() / 3);

//            gl.glFlush();
//            System.out.println((System.nanoTime() - start) / 1.0e6);
        }

        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            final GL3 gl = drawable.getGL().getGL3();

            // wtf
            var scale = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform().getScaleX();
            System.out.println(scale);
//            width = width * (int)scale;
//            height = height * (int)scale;
            this.width = width;
            this.height = height;
            gl.glViewport( 0, 0, width, height);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            yaw += Math.toRadians((e.getX() - mouseX) * 1.01f);
            pitch += Math.toRadians((e.getY() - mouseY) * 1.01f);
            mouseX = e.getX();
            mouseY = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            this.mouseX = e.getX();
            this.mouseY = e.getY();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getPreciseWheelRotation() < 0) {
                zoom /= 1.05f;
            } else {
                zoom *= 1.05f;
            }
        }
    }
}
