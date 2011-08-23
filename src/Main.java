
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;

public class Main {

    private static final int FPS = 60;
    private static final int DISPLAY_WIDTH = 800;
    private static final int DISPLAY_HEIGHT = 600;
    private static final int KEY_FORWARD = Keyboard.KEY_W;
    private static final int KEY_BACKWARD = Keyboard.KEY_S;
    private static final int KEY_LEFT = Keyboard.KEY_A;
    private static final int KEY_RIGHT = Keyboard.KEY_D;
    private static final float PLAYER_SPEED = 0.001f;
    private static Player player;
    private static float speed = 0;
    private static int delta;

    public static void main(String[] args) {
        initDisplay();

        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        ShaderManager.initShaders();

        enableAntiAliasing();

        Tools.checkGLErrors("init");

        List<Drawable> drawables = new LinkedList<Drawable>();
        player = new Player();
        drawables.add(player);

        while (!Display.isCloseRequested()) {
            delta = Tools.getDelta();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            for (Drawable drawable : drawables) {
                drawable.draw();
            }

            pollMouse();
            pollKeyboard();
            Display.update();
            Tools.updateFPS();

            Tools.checkGLErrors("loop");
            Display.sync(FPS);
        }

        Display.destroy();
    }

    private static void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
            Display.setTitle("Asteroids");
            // Display.setVSyncEnabled(true);
            Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCore(true));
        } catch (LWJGLException ex) {
            System.out.println("Could not init display!");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static void enableAntiAliasing() {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glEnable(GL11.GL_POINT_SMOOTH); // DEPRECATED
        //GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_NICEST); // TODO: http://mmmovania.blogspot.com/2010/12/circular-point-sprites-in-opengl-33.html
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
    }

    private static void pollKeyboard() {

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case KEY_FORWARD:
                        speed += PLAYER_SPEED;
                        break;
                    case KEY_BACKWARD:
                        speed -= PLAYER_SPEED;
                        break;
                }
            } else {
                switch (Keyboard.getEventKey()) {
                    case KEY_FORWARD:
                        speed -= PLAYER_SPEED;
                        break;
                    case KEY_BACKWARD:
                        speed += PLAYER_SPEED;
                        break;
                }
            }
        }

        player.translate(0, speed * delta);
    }

    private static void pollMouse() {
        float x = Mouse.getX();
        float y = Mouse.getY();

        // Project screen coordinates to normal [-1.0, 1.0] coordinate space
        x -= DISPLAY_WIDTH / 2;
        y -= DISPLAY_HEIGHT / 2;
        x /= DISPLAY_WIDTH / 2;
        y /= DISPLAY_HEIGHT / 2;


        Vector2f mouseDirection = new Vector2f(x, y);
        // Angle calculations assume player is at (0,0). Translate mouse vector to match this assumption.
        Vector2f.sub(mouseDirection, player.getLocation(), mouseDirection);
        Vector2f playerDirection = player.getDirection();

        float angle;    // Amount to rotate player in radians
        // Mouse is at the exact same location as player
        if (mouseDirection.x == 0 && mouseDirection.y == 0) {
            return;
        }

        angle = Vector2f.angle(mouseDirection, playerDirection);

        // Assume that angle means rotation in clockwise direction.
        player.rotate(true, angle);
        // If new angle after rotation is bigger, rotation should have been in counter-clockwise direction.
        float newAngle = Vector2f.angle(mouseDirection, player.getDirection());
        if (newAngle > angle) {
            player.rotate(false, angle * 2);
        }
    }
}
