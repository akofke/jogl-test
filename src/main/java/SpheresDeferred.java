import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.SplittableRandom;

public class SpheresDeferred {
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

            var animator = new FPSAnimator(panel, 3);
//            var animator = new Animator(panel);
            animator.setUpdateFPSFrames(60, System.err);
            animator.start();
        });
    }

    static class BasicFrame implements GLEventListener, MouseMotionListener, MouseWheelListener {
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

//        private ShaderState shaderStateGeom;
//        private ShaderState shaderStateLighting;

        private int N = 1000;

        private final FloatBuffer pos = genPos(N);
        private int posVbo = -1;
        private ShaderProgram shaderGeometryPass;
        private ShaderProgram shaderLightingPass;
        private int quadVAO = -1;
        private FBObject.TextureAttachment gNormal;
        private FBObject.TextureAttachment gPosition;
        private FBObject.TextureAttachment gAlbedoSpec;
        private FBObject gBufferFBO;

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

        private void makeFramebuffer(GL3 gl) {
            gBufferFBO = new FBObject();
            gBufferFBO.init(gl, width, height, 0);
            gPosition = gBufferFBO.attachTexture2D(gl,
                    0,
                    gl.GL_RGBA16F,
                    gl.GL_RGBA,
                    gl.GL_FLOAT,
                    gl.GL_NEAREST,
                    gl.GL_NEAREST,
                    gl.GL_REPEAT,
                    gl.GL_REPEAT);

            gNormal = gBufferFBO.attachTexture2D(gl,
                    1,
                    gl.GL_RGBA16F,
                    gl.GL_RGBA,
                    gl.GL_FLOAT,
                    gl.GL_NEAREST,
                    gl.GL_NEAREST,
                    gl.GL_REPEAT,
                    gl.GL_REPEAT);

            gAlbedoSpec = gBufferFBO.attachTexture2D(gl,
                    2,
                    gl.GL_RGBA,
                    gl.GL_RGBA,
                    gl.GL_UNSIGNED_BYTE,
                    gl.GL_NEAREST,
                    gl.GL_NEAREST,
                    gl.GL_REPEAT,
                    gl.GL_REPEAT);
            gBufferFBO.bind(gl);
            gl.glDrawBuffers(3, new int[]{ gl.GL_COLOR_ATTACHMENT0, gl.GL_COLOR_ATTACHMENT1, gl.GL_COLOR_ATTACHMENT2}, 0);

            gBufferFBO.attachRenderbuffer(gl, FBObject.Attachment.Type.DEPTH, FBObject.DEFAULT_BITS);
            System.out.println(gBufferFBO.getStatusString());
            if (!gBufferFBO.isStatusValid()) {
                throw new RuntimeException();
            }
            gBufferFBO.unbind(gl);
        }

        private void resizeFBO(GL3 gl) {
            System.out.println(gBufferFBO.reset(gl, width, height, 0));
            gBufferFBO.bind(gl);
            gl.glDrawBuffers(3, new int[]{ gl.GL_COLOR_ATTACHMENT0, gl.GL_COLOR_ATTACHMENT1, gl.GL_COLOR_ATTACHMENT2}, 0);
        }

        @Override
        public void init(GLAutoDrawable drawable) {
            final GL3 gl = drawable.getGL().getGL3();

            makeFramebuffer(gl);

//            gl.setSwapInterval(0);
            gl.glEnable(gl.GL_DEPTH_TEST);
//            gl.glEnable(gl.GL_CULL_FACE);

            var intBuf = GLBuffers.newDirectIntBuffer(1);

            // configure g-buffer framebuffer
            // -----------------------------
//            gl.glGenFramebuffers(1, intBuf);
//            gBuffer = intBuf.get(0);
//            gl.glBindFramebuffer(gl.GL_FRAMEBUFFER, gBuffer);
//
//            gl.glGenTextures(1, intBuf);
//            gPosition = intBuf.get(0);
//            gl.glBindTexture(gl.GL_TEXTURE_2D, gPosition);
//            gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGBA16F, width, height, 0, gl.GL_RGBA, gl.GL_FLOAT, null);
//            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST);
//            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST);
//            gl.glFramebufferTexture2D(gl.GL_FRAMEBUFFER, gl.GL_COLOR_ATTACHMENT0, gl.GL_TEXTURE_2D, gPosition, 0);
//
//            gl.glGenTextures(1, intBuf);
//            gNormal = intBuf.get(0);
//            gl.glBindTexture(gl.GL_TEXTURE_2D, gNormal);
//            gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGBA16F, width, height, 0, gl.GL_RGBA, gl.GL_FLOAT, null);
//            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST);
//            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST);
//            gl.glFramebufferTexture2D(gl.GL_FRAMEBUFFER, gl.GL_COLOR_ATTACHMENT1, gl.GL_TEXTURE_2D, gNormal, 0);
//
//            gl.glGenTextures(1, intBuf);
//            gAlbedoSpec = intBuf.get(0);
//            gl.glBindTexture(gl.GL_TEXTURE_2D, gAlbedoSpec);
//            gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGBA, width, height, 0, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, null);
//            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST);
//            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST);
//            gl.glFramebufferTexture2D(gl.GL_FRAMEBUFFER, gl.GL_COLOR_ATTACHMENT2, gl.GL_TEXTURE_2D, gAlbedoSpec, 0);
//
//            // Tell OpenGL which color attachments we'll use for rendering
//            gl.glDrawBuffers(3, new int[]{ gl.GL_COLOR_ATTACHMENT0, gl.GL_COLOR_ATTACHMENT1, gl.GL_COLOR_ATTACHMENT2}, 0);
//
//            gl.glGenRenderbuffers(1, intBuf);
//            int rboDepth = intBuf.get(0);
//            gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, rboDepth);
//            gl.glRenderbufferStorage(gl.GL_RENDERBUFFER, gl.GL_DEPTH_COMPONENT, width, height);
//            gl.glFramebufferRenderbuffer(gl.GL_FRAMEBUFFER, gl.GL_DEPTH_ATTACHMENT, gl.GL_RENDERBUFFER, rboDepth);
//
//            if (gl.glCheckFramebufferStatus(gl.GL_FRAMEBUFFER) != gl.GL_FRAMEBUFFER_COMPLETE) {
//                throw new RuntimeException("Framebuffer not complete");
//            }
//
//            gl.glBindFramebuffer(gl.GL_FRAMEBUFFER, 0);



            var vertDeferred = ShaderCode.create(gl, gl.GL_VERTEX_SHADER, 1, this.getClass(), new String[]{"sphere.vert"}, false);
            var geomDeferred = ShaderCode.create(gl, gl.GL_GEOMETRY_SHADER, 1, this.getClass(), new String[]{"sphere.geom"}, false);
            var fragDeferred = ShaderCode.create(gl, gl.GL_FRAGMENT_SHADER, 1, this.getClass(), new String[]{"sphereDeferred.frag"}, false);

            var prog = new ShaderProgram();
            prog.add(gl, vertDeferred, System.err);
            prog.add(gl, geomDeferred, System.err);
            prog.add(gl, fragDeferred, System.err);
            prog.link(gl, System.err);
            this.shaderGeometryPass = prog;

            var lightingShader = new ShaderProgram();
            var vertLighting = ShaderCode.create(gl, gl.GL_VERTEX_SHADER, 1, this.getClass(), new String[]{"sphereLightingPass.vert"}, false);
            var fragLighting = ShaderCode.create(gl, gl.GL_FRAGMENT_SHADER, 1, this.getClass(), new String[]{"sphereLightingPass.frag"}, false);
            lightingShader.add(gl, vertLighting, System.err);
            lightingShader.add(gl, fragLighting, System.err);
            lightingShader.link(gl, System.err);
            this.shaderLightingPass = lightingShader;
            this.shaderLightingPass.useProgram(gl, true);
            setUniform(gl, shaderLightingPass, new GLUniformData("gPosition", 0));
            setUniform(gl, shaderLightingPass, new GLUniformData("gNormal", 1));
            setUniform(gl, shaderLightingPass, new GLUniformData("gAlbedoSpec", 2));

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

            gl.glGenVertexArrays(1, intBuf);
            quadVAO = intBuf.get(0);
            gl.glGenBuffers(1, intBuf);
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


//            updatePos();
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, this.posVbo);
            gl.glBufferSubData(gl.GL_ARRAY_BUFFER, 0, pos.capacity() * Float.BYTES, pos);

            // Geometry pass
            this.gBufferFBO.bind(gl);
            gl.glClearColor( 0.0f, 0.0f, 0.0f, 1f );
            gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
//            this.shaderStateGeom.useProgram(gl, true);

            var view = new Matrix4f()
                    .translation(0, 0, -zoom)
                    .rotateX(pitch)
                    .rotateY(yaw);
            var projection = new Matrix4f()
                    .perspective(((float) Math.toRadians(45.0)), ((float) width) / height, 0.1f, 100.0f);

//            view = new Matrix4f()
//                    .translation(0, 0, -20f)
//                    .rotateX(pitch)
//                    .rotateY(yaw);
//            projection = new Matrix4f()
//                    .ortho(-width / 2f / 100 * zoom, width / 2f / 100 * zoom, -height/ 2f / 100 * zoom, height / 2f / 100 * zoom, 0.01f, 100.0f);

            this.shaderGeometryPass.useProgram(gl, true);
//            shaderStateGeom.uniform(gl, new GLUniformData("view", 4, 4, view.get(matBuffer)));
//            gl.glUniformMatrix4fv(gl.glGetUniformLocation(this.shaderGeometryPass.program(), "view"), 1, false, view.get(matBuffer));
            setUniform(gl, this.shaderGeometryPass, new GLUniformData("view", 4, 4, view.get(matBuffer)));
            setUniform(gl, this.shaderGeometryPass, new GLUniformData("projection", 4, 4, projection.get(matBuffer)));
            setUniform(gl, this.shaderGeometryPass, new GLUniformData("nearZ", 0.1f));

            var viewPos = view.transformPosition(new Vector3f()).negate();
            setUniform(gl, this.shaderGeometryPass, new GLUniformData("viewPos", 3, viewPos.get(GLBuffers.newDirectFloatBuffer(3))));

            setUniform(gl, this.shaderGeometryPass, new GLUniformData("material.ambient", 3, GLBuffers.newDirectFloatBuffer(new float[]{1.0f, 0.5f, 0.31f})));
            setUniform(gl, this.shaderGeometryPass, new GLUniformData("material.diffuse", 3, GLBuffers.newDirectFloatBuffer(new float[]{1.0f, 0.5f, 0.31f})));
            setUniform(gl, this.shaderGeometryPass, new GLUniformData("material.specular", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.5f, 0.5f, 0.5f})));
            setUniform(gl, this.shaderGeometryPass, new GLUniformData("material.shininess", 32.0f));


            gl.glBindVertexArray(this.vao);
            gl.glDrawArrays(gl.GL_POINTS, 0, pos.capacity() / 3);

//            gl.glFlush();
//            System.out.println((System.nanoTime() - start) / 1.0e6);

            gl.glBindFramebuffer(gl.GL_FRAMEBUFFER, 0);

            // Lighting pass
            // -------------

            gl.glClearColor( 0.0f, 0.0f, 0.0f, 1f );
            gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

//            this.shaderStateLighting.useProgram(gl, true);
            this.shaderLightingPass.useProgram(gl, true);

            gl.glActiveTexture(gl.GL_TEXTURE0);
            gl.glBindTexture(gl.GL_TEXTURE_2D, gPosition.getName());
            gl.glActiveTexture(gl.GL_TEXTURE1);
            gl.glBindTexture(gl.GL_TEXTURE_2D, gNormal.getName());
            gl.glActiveTexture(gl.GL_TEXTURE2);
            gl.glBindTexture(gl.GL_TEXTURE_2D, gAlbedoSpec.getName());

//            var lightDir = new Vector3f(-1, -1, -1).rotateX(time);
            var lightDir = new Vector3f(.5f, .5f, -1);
            setUniform(gl, this.shaderLightingPass, new GLUniformData("light.direction", 3, lightDir.get(GLBuffers.newDirectFloatBuffer(3))));
            setUniform(gl, this.shaderLightingPass, new GLUniformData("light.ambient", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.6f, 0.6f, 0.6f})));
            setUniform(gl, this.shaderLightingPass, new GLUniformData("light.diffuse", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.8f, 0.8f, 0.8f})));
            setUniform(gl, this.shaderLightingPass, new GLUniformData("light.specular", 3, GLBuffers.newDirectFloatBuffer(new float[]{0.9f, 0.9f, 0.9f})));

            // render quad
            gl.glBindVertexArray(this.quadVAO);
            gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 0, 4);
            gl.glBindVertexArray(0);

//            gl.glBindFramebuffer(gl.GL_READ_FRAMEBUFFER, this.gBufferFBO.getReadFramebuffer());
//            gl.glReadBuffer(gl.GL_COLOR_ATTACHMENT0);
//            gl.glBindFramebuffer(gl.GL_DRAW_FRAMEBUFFER, 0);
//            gl.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, gl.GL_COLOR_BUFFER_BIT, gl.GL_NEAREST);
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
            this.resizeFBO(gl);
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

    public static void setUniform(GL3 gl, ShaderProgram program, GLUniformData uniform) {
        gl.glUseProgram(program.program());
        int loc = gl.glGetUniformLocation(program.program(), uniform.getName());
//        System.out.println(loc);
        uniform.setLocation(loc);
        gl.glUniform(uniform);
    }
}
