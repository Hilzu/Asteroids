package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;

public class Tools {

    public static String readStringFromFile(String path) {
        File fileHandle = new File(path);
        BufferedReader inputReader = null;
        try {
            inputReader = new BufferedReader(new FileReader(fileHandle));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            System.exit(1);
        }

        String line = null;
        StringBuilder string = new StringBuilder();

        try {
            try {
                while (true) {

                    line = inputReader.readLine();

                    if (line == null) {
                        break;
                    }

                    string.append(line);
                    string.append(System.getProperty("line.separator"));
                }
            } finally {
                inputReader.close();
            }
        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(1);
        }

        return string.toString();
    }

    public static FloatBuffer floatArrayToFloatBuffer(float[] floatArray) {
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(floatArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(floatArray).position(0);
        return floatBuffer;
    }

    public static void checkGLErrors(String msg) {
        while (true) {
            try {
                Util.checkGLError();
                break;
            } catch (OpenGLException ex) {
                System.out.print(msg + ": ");
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void dataToVertexBufferObject(FloatBuffer data) {
        int buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
    }

    /**
     * Get the time in milliseconds using LWJGL timer.
     *
     * @return The system time in millisecons.
     */
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    private static long lastFrame = 0;

    /**
     * Calculates time passed in milliseconds since this method was called the last
     * time. Used in making animation and movement FPS independent.
     *
     * @return Time passed in milliseconds since last time this method was called.
     */
    public static int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }
    private static long lastFPS = getTime();
    private static int fps = 0;

    /**
     * Calculates amount of frames rendered in a second
     */
    public static void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0; //reset the FPS counter
            lastFPS += 1000; //add one second
        }
        fps++;
    }
}
