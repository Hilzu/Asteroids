
import java.io.BufferedReader;
import java.io.FileReader;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ARBFragmentShader;

/**
 * The vertex and fragment shaders are setup when the box object is
 * constructed. They are applied to the GL state prior to the box
 * being drawn, and released from that state after drawing.
 * @author Stephen Jones
 */
public class ShaderTest {

    private int shader = 0;
    private int vertShader = 0;
    private int fragShader = 0;

    public ShaderTest() {

        /*
         * create the shader program. If OK, create vertex
         * and fragment shaders
         */
        shader = ARBShaderObjects.glCreateProgramObjectARB();
        vertShader = createVertShader("simple.vs");
        fragShader = createFragShader("simple.fs");

        /*
         * if the vertex and fragment shaders setup sucessfully,
         * attach them to the shader program, link the sahder program
         * (into the GL context I suppose), and validate
         */
        ARBShaderObjects.glAttachObjectARB(shader, vertShader);
        ARBShaderObjects.glAttachObjectARB(shader, fragShader);
        ARBShaderObjects.glLinkProgramARB(shader);
        ARBShaderObjects.glValidateProgramARB(shader);
    }

    public void draw() {
        ARBShaderObjects.glUseProgramObjectARB(shader);

        //release the shader
        ARBShaderObjects.glUseProgramObjectARB(0);

    }

    /*
     * With the exception of syntax, setting up vertex and fragment shaders
     * is the same.
     * @param the name and path to the vertex shader
     */
    private int createVertShader(String filename) {
        //vertShader will be non zero if succefully created

        vertShader = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        //if created, convert the vertex shader code to a String
        String vertexCode = "";
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                vertexCode += line + "\n";
            }
        } catch (Exception e) {
            System.out.println("Fail reading vertex shading code");
            return 0;
        }
        /*
         * associate the vertex code String with the created vertex shader
         * and compile
         */
        ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
        ARBShaderObjects.glCompileShaderARB(vertShader);

        return vertShader;
    }

    //same as per the vertex shader except for method syntax
    private int createFragShader(String filename) {

        fragShader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        String fragCode = "";
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                fragCode += line + "\n";
            }
        } catch (Exception e) {
            System.out.println("Fail reading fragment shading code");
            return 0;
        }
        ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
        ARBShaderObjects.glCompileShaderARB(fragShader);

        return fragShader;
    }
}