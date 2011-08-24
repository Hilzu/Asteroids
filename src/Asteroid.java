
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Asteroid extends Movable {

    private static final float[] verts = {
        0.0f, 0.0f, 0.0f,
        0.05f, 0.087f, 0.0f,
        -0.05f, 0.087f, 0.0f,
        -0.1f, 0.0f, 0.0f,
        -0.05f, -0.087f, 0.0f,
        0.05f, -0.087f, 0.0f,
        0.1f, 0.0f, 0.0f,
        0.05f, 0.087f, 0.0f
    };
    private static final float[] color = {
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f
    };
    private FloatBuffer vertsBuffer;
    private FloatBuffer colorBuffer;
    private FloatBuffer modelViewBuffer;
    private boolean transformed;

    public Asteroid() {
        super();
        vertsBuffer = Tools.floatArrayToFloatBuffer(verts);
        colorBuffer = Tools.floatArrayToFloatBuffer(color);
        modelViewBuffer = Tools.floatArrayToFloatBuffer(new float[16]);
        transformed = true;
    }

    @Override
    public void draw() {
        if (transformed) {
            modelViewMatrix.store(modelViewBuffer);
            modelViewBuffer.position(0);
            transformed = false;
        }
        ShaderManager.useShader(Shader.FLAT, modelViewBuffer, colorBuffer);

        Tools.dataToVertexBufferObject(vertsBuffer);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        Tools.dataToVertexBufferObject(colorBuffer);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 8);
    }

    @Override
    public void rotate(boolean clockwise, float radians) {
        super.rotate(clockwise, radians);
        transformed = true;
    }

    @Override
    public void translate(float x, float y) {
        super.translate(x, y);
        transformed = true;
    }
}
