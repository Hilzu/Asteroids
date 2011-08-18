
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.EnumMap;
import java.util.Map;
import org.lwjgl.opengl.GL20;

public class ShaderManager {
    
    private static Map<Shader, Integer> shaderPrograms =
            new EnumMap<Shader, Integer>(Shader.class);

    public static void initShaders() {
        for (Shader shader : Shader.values()) {
            String vertShader;
            String fragShader;
            try {
                vertShader = Tools.readStringFromFile("shaders/"
                        + shader.getVertShaderFileName());
                fragShader = Tools.readStringFromFile("shaders/"
                        + shader.getFragShaderFileName());
            } catch (IOException ex) {
                throw new RuntimeException("Could not read shader from file!" + ex);
            }
            int programObject = createShaderProgram(vertShader, fragShader);
            shaderPrograms.put(shader, programObject);
        }
    }

    public static void useShader(Shader shaderType) {
        GL20.glUseProgram(shaderPrograms.get(shaderType));
    }

    private static int compileShader(int shaderType, String shaderSrc) {
        int shader;
        

        shader = GL20.glCreateShader(shaderType);

        if (shader == 0) {
            return 0;
        }

        GL20.glShaderSource(shader, shaderSrc);

        GL20.glCompileShader(shader);

        // Check if shader was compiled succesfully
        IntBuffer compiled = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GL20.glGetShader(shader, GL20.GL_COMPILE_STATUS, compiled);
        if (compiled.get(0) == 0) {
            System.out.println("GL20.glGetShaderInfoLog(shader, 1000): " + GL20.glGetShaderInfoLog(shader, 1000));
            GL20.glDeleteShader(shader);
            return 0;
        }

        return shader;
    }

    private static int createShaderProgram(String vShaderStr, String fShaderStr) {

        int vertexShader;
        int fragmentShader;

        vertexShader = compileShader(GL20.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = compileShader(GL20.GL_FRAGMENT_SHADER, fShaderStr);

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
