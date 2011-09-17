package main;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.GL32;

import shader.ShaderManager;
import drawable.Player;
import tool.Tools;

public class Main {

    public static final int FPS = 60;
    public static final int DISPLAY_WIDTH = 800;
    public static final int DISPLAY_HEIGHT = 600;
    
    private static Player player;
    private static int frameDelta;
    private static Bullets bullets;
    private static Asteroids asteroids;

    public static void main(String[] args) {
        initDisplay();

        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Allows to define size of point primitive in vertex shader
        GL11.glEnable(GL32.GL_PROGRAM_POINT_SIZE);

        ShaderManager.initShaders();

        Tools.checkGLErrors("Initialization");

        bullets = new Bullets();
        asteroids = new Asteroids();

        player = new Player();

        asteroids.newAsteroid();

        // Init frame delta time so that first reading is sane.
        Tools.getDelta();

        while (!Display.isCloseRequested()) {
            frameDelta = Tools.getDelta();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            player.update();
            bullets.update();
            asteroids.update();

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

    public static int getFrameDelta() {
        return frameDelta;
    }

    public static Player getPlayer() {
        return player;
    }
}