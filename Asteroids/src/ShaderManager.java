
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL20;


public class ShaderManager {
    
    public static int LoadShader(int type, String shaderSrc) {
        int shader;
        IntBuffer compiled = ByteBuffer.allocateDirect(4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();

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

    public static int initShaders(String vShaderPath, String fShaderPath) {   //TODO: write this to read shader source from file
        String fShaderStr = ""
                + "#version 150                                              \n"
                + "out vec4 vFragColor; // Fragment color to rasterize       \n"
                + "in vec4 vVaryingColor; // Incoming color from vertex stage\n"
                + "void main(void)                                           \n"
                + "{                                                         \n"
                + "    vFragColor = vVaryingColor;                           \n"
                + "}";
        String vShaderStr = ""
                + "#version 150                                              \n"
                + "in vec4 vVertex; // Vertex position attribute             \n"
                + "in vec4 vColor; // Vertex color attribute                 \n"
                + "out vec4 vVaryingColor; // Color value passed to fragment shader \n"
                + "void main(void)                                           \n"
                + "{                                                         \n"
                + "vVaryingColor = vColor;// Simply copy the color value     \n"
                + "gl_Position = vVertex; // Simply pass along the vertex position \n"
                + "}";

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
