
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hilzu
 */
class Tools {

    static String readStringFromFile(String path) {
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
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(floatArray.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(floatArray).position(0);
        return floatBuffer;
    }
    
    public static void checkGLErrors(String msg) {
        while (true) {                
                try {
                    Util.checkGLError();
                    break;
                } catch (OpenGLException ex) {
                    System.out.println(msg);
                    System.out.println(ex.getMessage());
                }
            }
    }
}
