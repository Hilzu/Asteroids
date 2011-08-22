
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

    private static final int DISPLAY_WIDTH = 800;
    private static final int DISPLAY_HEIGHT = 600;
    private static final int KEY_FORWARD = Keyboard.KEY_W;
    private static final int KEY_BACKWARD = Keyboard.KEY_S;
    private static final int KEY_LEFT = Keyboard.KEY_A;
    private static final int KEY_RIGHT = Keyboard.KEY_D;
    private static final float PLAYER_SPEED = 0.001f;
    
    private static Player player;
    private static float speed = 0;

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
            int delta = Tools.getDelta();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            for (Drawable drawable : drawables) {
                drawable.draw();
            }
            
            pollInput(delta);
            Display.update();
            Tools.updateFPS();
            
            Tools.checkGLErrors("loop");
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

    private static void pollInput(int delta) {
        float x = Mouse.getX();
        float y = Mouse.getY();
        
        x -= DISPLAY_WIDTH / 2;
        y -= DISPLAY_HEIGHT / 2;
        
        x /= DISPLAY_WIDTH / 2;
        y /= DISPLAY_HEIGHT / 2;
        
        if (x == 0 && y == 0) {
            return;
        }
        Vector2f mousePos = new Vector2f(x, y);
        mousePos.normalise();
        Vector2f playerPos = player.getBearing();
        playerPos.normalise();
        float angle = Vector2f.dot(playerPos, mousePos);
        
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
        
        player.move(angle, speed * delta);
    }
}
