import com.jogamp.common.net.Uri;
import com.jogamp.common.nio.Buffers;
import com.jogamp.nativewindow.NativeWindow;
import com.jogamp.nativewindow.ScalableSurface;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import jogamp.nativewindow.SurfaceScaleUtils;
import jogamp.nativewindow.macosx.OSXUtil;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.camera.ArcBallCamera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.SplittableRandom;

public class Main2 {
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

        private final FloatBuffer colors = GLBuffers.newDirectFloatBuffer(new float[] {
                1.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
        });

        private final IntBuffer indices = GLBuffers.newDirectIntBuffer(new int[] {
                0, 1, 3,
                1, 2, 3
        });

        private final FloatBuffer vertices = GLBuffers.newDirectFloatBuffer(new float[]{
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,

                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,

                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,

                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,

                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f
        });
        private ShaderState shaderState;

        private int N = 10000;

        private final FloatBuffer pos = genPos(N);
        private int posVbo = -1;

        private FloatBuffer sphereVertices;
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

        private void readSphere() {
            try {
                var obj = ObjReader.read((getClass().getResourceAsStream("sphere.obj")));
                obj = ObjUtils.convertToRenderable(obj);
                var vertices = ObjData.getVerticesArray(obj);
                var normals = ObjData.getNormalsArray(obj);
                this.sphereIndices = ObjData.getFaceVertexIndices(obj);

                var sphereVertices = GLBuffers.newDirectFloatBuffer(2 * vertices.length);
                for (int i = 0; i < vertices.length / 3; i++) {
                    sphereVertices.put(vertices, i * 3, 3);
                    sphereVertices.put(normals, i * 3, 3);
                }
                sphereVertices.rewind();
                this.sphereVertices = sphereVertices;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void init(GLAutoDrawable drawable) {
            this.readSphere();

            final GL3 gl = drawable.getGL().getGL3();
            gl.setSwapInterval(0);
            gl.glEnable(gl.GL_DEPTH_TEST);
            gl.glEnable(gl.GL_CULL_FACE);
            gl.glClearColor( 0.1f, 0.1f, 0.1f, 0f );

            var vert = ShaderCode.create(gl, gl.GL_VERTEX_SHADER, 1, this.getClass(), new String[]{"vertex.vert"}, false);
            var frag = ShaderCode.create(gl, gl.GL_FRAGMENT_SHADER, 1, this.getClass(), new String[]{"fragment.frag"}, false);

            var prog = new ShaderProgram();
            prog.add(gl, vert, System.err);
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
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo.get(0));
            gl.glBufferData(gl.GL_ARRAY_BUFFER, sphereVertices.capacity() * Float.BYTES, sphereVertices, gl.GL_STATIC_DRAW);

            gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 6 * Float.BYTES, 0);
            gl.glEnableVertexAttribArray(0);

            gl.glVertexAttribPointer(1, 3, gl.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
            gl.glEnableVertexAttribArray(1);

            gl.glGenBuffers(1, vbo);
            gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, vbo.get(0));
            gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, sphereIndices.capacity() * Integer.BYTES, sphereIndices, gl.GL_STATIC_DRAW);

            gl.glGenBuffers(1, vbo);
            this.posVbo = vbo.get(0);
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo.get(0));
            gl.glBufferData(gl.GL_ARRAY_BUFFER, pos.capacity() * Float.BYTES, pos, gl.GL_STREAM_DRAW);
            gl.glVertexAttribPointer(2, 3, gl.GL_FLOAT, false, 3 * Float.BYTES, 0);
            gl.glEnableVertexAttribArray(2);
            gl.glVertexAttribDivisor(2, 1);

//            gl.glGenBuffers(1, vbo);
//            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo.get(0));
//            gl.glBufferData(gl.GL_ARRAY_BUFFER, colors.capacity() * Float.BYTES, colors, gl.GL_STATIC_DRAW);
//
//            gl.glVertexAttribPointer(1, 3, gl.GL_FLOAT, false, 3 * Float.BYTES, 0);
//            gl.glEnableVertexAttribArray(1);

//            var ebo = new int[1];
//            gl.glGenBuffers(1, ebo, 0);
//            gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
//            gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, indices.capacity() * Integer.BYTES, indices, gl.GL_STATIC_DRAW);

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

            updatePos();
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, this.posVbo);
            gl.glBufferSubData(gl.GL_ARRAY_BUFFER, 0, pos.capacity() * Float.BYTES, pos);

            this.shaderState.useProgram(gl, true);

            float time = (this.start - System.currentTimeMillis()) / 1000.0f;
            long thisTime = System.nanoTime();
            float timeDiff = (float) ((this.lastTime - thisTime) / 1.0e9);
            lastTime = thisTime;

            var model = new Matrix4f()
//                    .identity();
                    .scale(0.01f)
                    .rotate(time, new Vector3f(0.5f, 1.0f, 0.0f).normalize());
            var normalMatrix = model.normal(new Matrix3f());
            var view = new Matrix4f()
                    .translation(0, 0, -zoom)
                    .rotateX(pitch)
                    .rotateY(yaw);
            var projection = new Matrix4f()
                    .perspective(((float) Math.toRadians(30.0)), ((float) width) / height, 0.1f, 100.0f);

            shaderState.uniform(gl, new GLUniformData("model", 4, 4, model.get(matBuffer)));
            shaderState.uniform(gl, new GLUniformData("normalMatrix", 3, 3, normalMatrix.get(GLBuffers.newDirectFloatBuffer(9))));
            shaderState.uniform(gl, new GLUniformData("view", 4, 4, view.get(matBuffer)));
            shaderState.uniform(gl, new GLUniformData("projection", 4, 4, projection.get(matBuffer)));

            var viewPos = view.transformPosition(new Vector3f()).negate();
            shaderState.uniform(gl, new GLUniformData("viewPos", 3, viewPos.get(GLBuffers.newDirectFloatBuffer(3))));

            shaderState.uniform(gl, new GLUniformData("material.ambient", 3, GLBuffers.newDirectFloatBuffer(new float[]{1.0f, 0.5f, 0.31f})));
            shaderState.uniform(gl, new GLUniformData("material.diffuse", 3, GLBuffers.newDirectFloatBuffer(new float[]{1.0f, 0.5f, 0.31f})));
            shaderState.uniform(gl, new GLUniformData("material.specular", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.5f, 0.5f, 0.5f})));
            shaderState.uniform(gl, new GLUniformData("material.shininess", 32.0f));

//            var lightDir = new Vector3f(-1, -1, -1).rotateX(time);
            var lightDir = new Vector3f(1, 1, 1);
            shaderState.uniform(gl, new GLUniformData("dirLight.direction", 3, lightDir.get(GLBuffers.newDirectFloatBuffer(3))));
            shaderState.uniform(gl, new GLUniformData("dirLight.ambient", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.2f, 0.2f, 0.2f})));
            shaderState.uniform(gl, new GLUniformData("dirLight.diffuse", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.6f, 0.6f, 0.6f})));
            shaderState.uniform(gl, new GLUniformData("dirLight.specular", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.6f, 0.6f, 0.6f})));

            gl.glBindVertexArray(this.vao);
//            int vertexColorLoc = gl.glGetUniformLocation(this.shader.program(), "ourColor");
//            gl.glUniform4f(vertexColorLoc, 0.0f, greenValue, 0.0f, 1.0f);
//            gl.glDrawElements(gl.GL_TRIANGLES, indices.capacity(), gl.GL_UNSIGNED_INT, 0);

//            gl.glDrawArrays(gl.GL_TRIANGLES, 0, vertices.capacity());

            gl.glDrawElementsInstanced(gl.GL_TRIANGLES, sphereIndices.capacity(), gl.GL_UNSIGNED_INT, 0, N);

//            gl.glFlush();
//            System.out.println((System.nanoTime() - start) / 1.0e6);
        }

        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            final GL3 gl = drawable.getGL().getGL3();

            // wtf
            var scale = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform().getScaleX();
            width = width * (int)scale;
            height = height * (int)scale;
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
