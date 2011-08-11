
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {

    private static final int DISPLAY_WIDTH = 800;
    private static final int DISPLAY_HEIGHT = 600;

    public static void main(String[] args) {
        initDisplay();

        while (!Display.isCloseRequested()) {
            Display.update();
        }

        Display.destroy();

    }

    private static void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
            Display.setTitle("Asteroids");
            Display.create();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
