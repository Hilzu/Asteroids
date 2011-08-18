
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL20;

public class ShaderManager {

    public static int useShaders(Shader type) {
        String vertShader;
        String fragShader;
        try {
            vertShader = Tools.readStringFromFile("shaders/"
                    + type.getVertShaderFileName());
            fragShader = Tools.readStringFromFile("shaders/"
                    + type.getFragShaderFileName());
        } catch (IOException ex) {
            System.out.println("Could not read shader from file!");
            System.out.println(ex);
            return 0;
        }
        return initShaders(vertShader, fragShader);
    }

    public static int LoadShader(int type, String shaderSrc) {
        int shader;
        IntBuffer compiled = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();

        shader = GL20.glCreateShader(type);

        if (shader == 0) {
            return 0;
        }

        GL20.glShaderSource(shader, shaderSrc);

        GL20.glCompileShader(shader);

        // Check if shader was compiled succesfully
        GL20.glGetShader(shader, GL20.GL_COMPILE_STATUS, compiled);
        if (compiled.get(0) == 0) {
            System.out.println("GL20.glGetShaderInfoLog(shader, 1000): " + GL20.glGetShaderInfoLog(shader, 1000));
            GL20.glDeleteShader(shader);
            return 0;
        }

        return shader;
    }

    public static int initShaders(String vShaderStr, String fShaderStr) {

        int vertexShader;
        int fragmentShader;

        vertexShader = LoadShader(GL20.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = LoadShader(GL20.GL_FRAGMENT_SHADER, fShaderStr);

        int programObject = GL20.glCreateProgram();

        if (programObject == 0) {
            return 0;
        }

        GL20.glAttachShader(programObject, vertexShader);
        GL20.glAttachShader(programObject, fragmentShader);

        GL20.glBindAttribLocation(programObject, 0, "vVertex");
        GL20.glBindAttribLocation(programObject, 1, "vColor");

        GL20.glLinkProgram(programObject);

        //Check if program was linked succesfully
        IntBuffer linked = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GL20.glGetProgram(programObject, GL20.GL_LINK_STATUS, linked);
        if (linked.get(0) == 0) {
            System.out.println("Error linking program:");
            System.out.println(GL20.glGetProgramInfoLog(programObject, 1000));
            GL20.glDeleteProgram(programObject);
            return 0;
        }

        return programObject;
    }
}
