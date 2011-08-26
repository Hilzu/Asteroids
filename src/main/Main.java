package main;

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
import org.lwjgl.opengl.GL32;

import shader.ShaderManager;
import drawable.Movable;
import drawable.Asteroid;
import drawable.Bullet;
import drawable.Player;
import drawable.Drawable;
import tool.Tools;

public class Main {

    public static final int FPS = 60;
    public static final int DISPLAY_WIDTH = 800;
    public static final int DISPLAY_HEIGHT = 600;
    public static final int KEY_FORWARD = Keyboard.KEY_W;
    public static final int KEY_BACKWARD = Keyboard.KEY_S;
    public static final int KEY_LEFT = Keyboard.KEY_A;
    public static final int KEY_RIGHT = Keyboard.KEY_D;
    public static final int BTN_SHOOT = 0;

    private static Player player;
    private static int frameDelta;
    private static List<Drawable> drawables;
    private static List<Movable> movables;
    private static List<Bullet> bullets;

    public static void main(String[] args) {
        initDisplay();

        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Allows to define size of point primitive in vertex shader
        GL11.glEnable(GL32.GL_PROGRAM_POINT_SIZE);

        ShaderManager.initShaders();

        Tools.checkGLErrors("Initialization");

        drawables = new LinkedList<Drawable>();
        movables = new LinkedList<Movable>();
        bullets = new LinkedList<Bullet>();

        player = new Player();
        drawables.add(player);
        movables.add(player);

        Asteroid asteroid = new Asteroid();
        drawables.add(asteroid);
        movables.add(asteroid);

        // Init frame delta time so that first reading is sane.
        Tools.getDelta();

        while (!Display.isCloseRequested()) {
            frameDelta = Tools.getDelta();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            pollMouseLocation();
            pollKeyboard();
            pollMouseButtons();

            for (Movable movable : movables) {
                movable.move(frameDelta);
            }
            for (Drawable drawable : drawables) {
                drawable.draw();
            }

            List<Bullet> bulletsToRemove = new LinkedList<Bullet>();
            for (Bullet bullet : bullets) {
                Vector2f bulletLocation = bullet.getLocation();
                if (bulletLocation.x < -1.0) {
                    bulletsToRemove.add(bullet);
                    continue;
                }
                if (bulletLocation.x > 1.0) {
                    bulletsToRemove.add(bullet);
                    continue;
                }
                if (bulletLocation.y < -1.0) {
                    bulletsToRemove.add(bullet);
                    continue;
                }
                if (bulletLocation.y > 1.0) {
                    bulletsToRemove.add(bullet);
                    continue;
                }
            }
            for (Bullet bullet : bulletsToRemove) {
                drawables.remove(bullet);
                movables.remove(bullet);
                bullets.remove(bullet);
            }

            Display.update();

            Tools.updateFPS();

            Tools.checkGLErrors("Main loop");

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
            // Key pushed event
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
            } // Key lifted event
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

    private static void pollMouseLocation() {
        float x = Mouse.getX();
        float y = Mouse.getY();

        // Project window coordinates to normal [-1.0, 1.0] coordinate space
        x -= DISPLAY_WIDTH / 2;     // [0, 800] => [-400, 400]
        y -= DISPLAY_HEIGHT / 2;
        x /= DISPLAY_WIDTH / 2;     // [-400, 400] => [-1, 1]
        y /= DISPLAY_HEIGHT / 2;

        Vector2f mouseDirection = new Vector2f(x, y);
        player.rotateTo(mouseDirection);
    }

    private static void pollMouseButtons() {
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                switch (Mouse.getEventButton()) {
                    case BTN_SHOOT: {
                        Bullet bullet = new Bullet();

                        bullet.translate(player.getLocation());
                        bullet.rotateTo(player.getDirection());

                        // Offset bullet so that it spawns at ships tip.
                        bullet.translate(0, .04f);

                        bullets.add(bullet);
                        movables.add(bullet);
                        drawables.add(bullet);
                        break;
                    }
                }
            }
        }
    }
}
