
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Main {

    private static final int DISPLAY_WIDTH = 800;
    private static final int DISPLAY_HEIGHT = 600;

    public static void main(String[] args) {
        initDisplay();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        final float[] vertices = {
            0.0f, 0.9f, 0.0f,
            -0.9f, -0.9f, 0.0f,
            0.9f, -0.9f, 0.0f};
        final float[] colors = {
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f};
        Triangle triangle = new Triangle(vertices, colors);

        while (!Display.isCloseRequested()) {
            triangle.draw();
            Display.update();
        }

        Display.destroy();

    }

    private static void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
            Display.setTitle("Asteroids");
            Display.setVSyncEnabled(true);
            Display.create();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
