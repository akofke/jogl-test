import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.behaviors.vp.OrbitBehavior;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class J3D {

    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");

        var gct = new GraphicsConfigTemplate3D();
        gct.setSceneAntialiasing(GraphicsConfigTemplate.REQUIRED);
        System.out.println(gct.toString());
        var config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(gct);

        var box = new DisplayBox();
        var canvas = new Canvas3D(config);
        System.out.println(canvas.getSceneAntialiasingAvailable());
        canvas.setPreferredSize(new Dimension(600, 600));
        var universe = new SimpleUniverse(canvas);
        universe.addBranchGraph(box.root);

        OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL);
        BoundingSphere bounds =
                new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        orbit.setSchedulingBounds(bounds);
        universe.getViewingPlatform().setViewPlatformBehavior(orbit);

        universe.getViewingPlatform().setNominalViewingTransform();
        universe.getViewer().getView().setMinimumFrameCycleTime(5);

        var frame = new JFrame();
        frame.getContentPane().add(canvas);
        SwingUtilities.invokeLater(() -> {
            frame.pack();
            frame.setVisible(true);
        });
    }

    public static class DisplayBox {
        double[] coords = new double[1000 * 3];
        BranchGroup root = new BranchGroup();

        int n = 5000;
        TransformGroup[] groups = new TransformGroup[n];
        Transform3D[] tfs = new Transform3D[n];
        Vector3d[] pos = new Vector3d[n];

        public DisplayBox() {
            var rand = new Random();
            for (int i = 0; i < n; i++) {
                var tf = new Transform3D();
                var p = new Vector3d(rand.doubles(3, -1, 1).toArray());
                tf.setTranslation(p);
                var group = new TransformGroup(tf);
                group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                var app = new Appearance();
                var color = new Color3f(0.5f, 0.7f, 0.6f);
                var black = new Color3f(0, 0, 0);
                var white = new Color3f(1, 1, 1);
                app.setMaterial(new Material(
                        color,
                        black,
                        color,
                        white,
                        100.0f
                ));
                var sphere = new Sphere(0.01f, Primitive.GENERATE_NORMALS, 32, app);
                group.addChild(sphere);
                groups[i] = group;
                tfs[i] = tf;
                pos[i] = p;
                root.addChild(group);
            }

            BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 100.0);
            AmbientLight al = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
            al.setInfluencingBounds(bounds);
            Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
            Vector3f light1Direction  = new Vector3f(1.0f, 1.0f, 1.0f);
            Color3f light2Color = new Color3f(1.0f, 1.0f, 0.9f);
            Vector3f light2Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);

            DirectionalLight light1
                    = new DirectionalLight(light1Color, light1Direction);
            light1.setInfluencingBounds(bounds);
            root.addChild(light1);

            DirectionalLight light2
                    = new DirectionalLight(light2Color, light2Direction);
            light2.setInfluencingBounds(bounds);
            root.addChild(light2);
            root.addChild(al);

            root.compile();
        }
    }
}
