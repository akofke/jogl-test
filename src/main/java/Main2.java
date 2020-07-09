import com.jogamp.common.net.Uri;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.camera.ArcBallCamera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.time.Duration;
import java.time.Instant;

public class Main2 {
    public static void main(String[] args) {
        var profile = GLProfile.get(GLProfile.GL3);
        var caps = new GLCapabilities(profile);


        var frame = new JFrame("Hi");

        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            var panel = new GLCanvas(caps);
//            var panel = new GLJPanel(caps);
            panel.setPreferredSize(new Dimension(600, 600));
            var basic = new BasicFrame();
            panel.addGLEventListener(basic);

            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);

            var animator = new FPSAnimator(panel, 60);
            animator.start();
        });
    }

    static class BasicFrame implements GLEventListener, MouseMotionListener {
        private float rquad = 0;
        private final GLU glu = new GLU();
        private long start = System.currentTimeMillis();
        private long lastTime;

        private final ArcBallCamera cam = new ArcBallCamera();

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

        @Override
        public void init(GLAutoDrawable drawable) {
            final GL3 gl = drawable.getGL().getGL3();
            gl.glEnable(gl.GL_DEPTH_TEST);
            gl.glClearColor( 0.2f, 0.3f, 0.3f, 0f );

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
            gl.glBufferData(gl.GL_ARRAY_BUFFER, vertices.capacity() * Float.BYTES, vertices, gl.GL_STATIC_DRAW);

            gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 6 * Float.BYTES, 0);
            gl.glEnableVertexAttribArray(0);

            gl.glVertexAttribPointer(1, 3, gl.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
            gl.glEnableVertexAttribArray(1);

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

            cam.center(0, 0, 0);

            lastTime = System.nanoTime();
        }

        @Override
        public void dispose(GLAutoDrawable drawable) {

        }

        @Override
        public void display(GLAutoDrawable drawable) {
            var start = Instant.now();
            final GL3 gl = drawable.getGL().getGL3();

            gl.glClearColor( 0.2f, 0.3f, 0.3f, 0f );
            gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

            this.shaderState.useProgram(gl, true);

            float time = (this.start - System.currentTimeMillis()) / 1000.0f;
            long thisTime = System.nanoTime();
            float timeDiff = (float) ((this.lastTime - thisTime) / 1.0e9);
            lastTime = thisTime;

            var model = new Matrix4f()
                    .rotate(time, new Vector3f(0.5f, 1.0f, 0.0f).normalize());
            var view = cam.viewMatrix(new Matrix4f());
            var projection = new Matrix4f()
                    .perspective(((float) Math.toRadians(45.0)), 1.0f, 0.1f, 100.0f);

            shaderState.uniform(gl, new GLUniformData("model", 4, 4, model.get(matBuffer)));
            shaderState.uniform(gl, new GLUniformData("view", 4, 4, view.get(matBuffer)));
            shaderState.uniform(gl, new GLUniformData("projection", 4, 4, projection.get(matBuffer)));

            shaderState.uniform(gl, new GLUniformData("viewPos", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.0f, 0.0f, 3.0f})));

            shaderState.uniform(gl, new GLUniformData("material.ambient", 3, GLBuffers.newDirectFloatBuffer(new float[]{1.0f, 0.5f, 0.31f})));
            shaderState.uniform(gl, new GLUniformData("material.diffuse", 3, GLBuffers.newDirectFloatBuffer(new float[]{1.0f, 0.5f, 0.31f})));
            shaderState.uniform(gl, new GLUniformData("material.specular", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.5f, 0.5f, 0.5f})));
            shaderState.uniform(gl, new GLUniformData("material.shininess", 32.0f));

            shaderState.uniform(gl, new GLUniformData("dirLight.direction", 3, GLBuffers.newDirectFloatBuffer(new float[]{-0.2f, -1.0f, -0.3f})));
            shaderState.uniform(gl, new GLUniformData("dirLight.ambient", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.1f, 0.1f, 0.1f})));
            shaderState.uniform(gl, new GLUniformData("dirLight.diffuse", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.6f, 0.6f, 0.6f})));
            shaderState.uniform(gl, new GLUniformData("dirLight.specular", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.6f, 0.6f, 0.6f})));

            gl.glBindVertexArray(this.vao);
//            int vertexColorLoc = gl.glGetUniformLocation(this.shader.program(), "ourColor");
//            gl.glUniform4f(vertexColorLoc, 0.0f, greenValue, 0.0f, 1.0f);
//            gl.glDrawElements(gl.GL_TRIANGLES, indices.capacity(), gl.GL_UNSIGNED_INT, 0);
            gl.glDrawArrays(gl.GL_TRIANGLES, 0, vertices.capacity());

            System.out.println(Duration.between(start, Instant.now()).toNanos() / 1_000_000.0);
        }

        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            final GL3 gl = drawable.getGL().getGL3();
            if( height <= 0 )
                height = 1;

            final float h = ( float ) width / ( float ) height;
            gl.glViewport( 0, 0, width, height );

//            glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}
