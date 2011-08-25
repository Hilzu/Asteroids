
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

    public static final int FPS = 60;
    public static final int DISPLAY_WIDTH = 800;
    public static final int DISPLAY_HEIGHT = 600;
    public static final int KEY_FORWARD = Keyboard.KEY_W;
    public static final int KEY_BACKWARD = Keyboard.KEY_S;
    public static final int KEY_LEFT = Keyboard.KEY_A;
    public static final int KEY_RIGHT = Keyboard.KEY_D;

    private static Player player;
    private static float asteroidYSpeed = 0.0010f;
    private static float asteroidXSpeed = 0.0012f;

    public static int frameDelta;

    public static void main(String[] args) {
        initDisplay();

        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        ShaderManager.initShaders();

        Tools.checkGLErrors("init");

        List<Drawable> drawables = new LinkedList<Drawable>();
        player = new Player();
        drawables.add(player);
        Asteroid asteroid = new Asteroid();
        drawables.add(asteroid);

        // Init frame delta time so that first reading is sane.
        Tools.getDelta();

        while (!Display.isCloseRequested()) {
            frameDelta = Tools.getDelta();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            pollMouse();
            pollKeyboard();
            move(asteroid);
            player.move();

            for (Drawable drawable : drawables) {
                drawable.draw();
            }

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
            // Use OpenGL 3.2 Core profile so that deprecated functionality can't be used
            Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCore(true));
        } catch (LWJGLException ex) {
            System.out.println("Could not init display!");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static void pollKeyboard() {

        while (Keyboard.next()) {
            // Key down event
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case KEY_FORWARD:
                        player.moveVertically(true);
                        break;
                    case KEY_BACKWARD:
                        player.moveVertically(false);
                        break;
                    case KEY_LEFT:
                        player.moveSideways(true);
                        break;
                    case KEY_RIGHT:
                        player.moveSideways(false);
                        break;
                }
            }
            // Key up event
            else {
                switch (Keyboard.getEventKey()) {
                    case KEY_FORWARD:
                        player.moveVertically(false);
                        break;
                    case KEY_BACKWARD:
                        player.moveVertically(true);
                        break;
                    case KEY_LEFT:
                        player.moveSideways(false);
                        break;
                    case KEY_RIGHT:
                        player.moveSideways(true);
                        break;
                }
            }
        }
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

    private static void move(Movable movable) {
        movable.translate(asteroidXSpeed * frameDelta, asteroidYSpeed * frameDelta);
        Vector2f location = movable.getLocation();
        if (location.x > 0.9f || location.x < -0.9f) {
            asteroidXSpeed *= -1;
        }
        if (location.y > 0.9f || location.y < -0.9f) {
            asteroidYSpeed *= -1;
        }
    }
}
