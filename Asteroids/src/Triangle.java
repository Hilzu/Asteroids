
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Triangle {

    private int programObject;
    private FloatBuffer verticesBuffer;
    private FloatBuffer colorBuffer;
    private final float[] vertices = {0.0f, 0.2f, 0.0f, -0.2f, -0.2f, 0.0f, 0.2f, -0.2f, 0.0f};
    private final float[] color = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};

    public Triangle() {
        verticesBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesBuffer.put(vertices).position(0);

        colorBuffer = ByteBuffer.allocateDirect(color.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(color).position(0);
    }

    private int LoadShader(int type, String shaderSrc) {
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

    public void initShaders(String vShaderPath, String fShaderPath) {   //TODO: write this to read shader source from file
        String fShaderStr = ""
                + "#version 330                                              \n"
                + "out vec4 vFragColor; // Fragment color to rasterize       \n"
                + "in vec4 vVaryingColor; // Incoming color from vertex stage\n"
                + "void main(void)                                           \n"
                + "{                                                         \n"
                + "    vFragColor = vVaryingColor;                           \n"
                + "}";
        String vShaderStr = ""
                + "#version 330                                              \n"
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
        IntBuffer linked = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();

        vertexShader = LoadShader(GL20.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = LoadShader(GL20.GL_FRAGMENT_SHADER, fShaderStr);

        programObject = GL20.glCreateProgram();

        if (programObject == 0) {
            return;
        }

        GL20.glAttachShader(programObject, vertexShader);
        GL20.glAttachShader(programObject, fragmentShader);

        GL20.glBindAttribLocation(programObject, 0, "vVertex");
        GL20.glBindAttribLocation(programObject, 1, "vColor");

        GL20.glLinkProgram(programObject);

        //Check if program was linked succesfully
        GL20.glGetProgram(programObject, GL20.GL_LINK_STATUS, linked);
        if (linked.get(0) == 0) {
            System.out.println("Error linking program:");
            System.out.println(GL20.glGetProgramInfoLog(programObject, 1000));
            GL20.glDeleteProgram(programObject);
            return;
        }

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL20.glUseProgram(programObject);

        GL20.glVertexAttribPointer(0, 3, false, 0, verticesBuffer);
        GL20.glVertexAttribPointer(1, 3, false, 0, colorBuffer);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }
}