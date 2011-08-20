
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.*;

public class Main {

    private static final int DISPLAY_WIDTH = 800;
    private static final int DISPLAY_HEIGHT = 600;

    public static void main(String[] args) {
        initDisplay();
        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        ShaderManager.initShaders();
        enableAntiAliasing();
        Tools.checkGLErrors("init");
        List<Drawable> drawables = new LinkedList<Drawable>();
        Player player = new Player();
        drawables.add(player);

        float angle = 0.0001f;
        float dist = 0.00001f;

        while (!Display.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            for (Drawable drawable : drawables) {
                drawable.draw();

            }
            Display.update();

            player.move(angle, dist);
            //angle += 0.1f;
            
            Tools.checkGLErrors("loop");
        }

        Display.destroy();
    }

    private static void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
            Display.setTitle("Asteroids");
            Display.setVSyncEnabled(true);
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
}
