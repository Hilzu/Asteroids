
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Asteroid implements Movable {

    private static final float[] verts = {
        0.0f, 0.0f, 0.0f,
        0.5f, 0.87f, 0.0f,
        -0.5f, 0.87f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -0.5f, -0.87f, 0.0f,
        0.5f, -0.87f, 0.0f,
        1.0f, 0.0f, 0.0f,
        0.5f, 0.87f, 0.0f
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
    private Matrix4f modelViewMatrix;
    private FloatBuffer modelViewBuffer;

    public Asteroid() {
        vertsBuffer = Tools.floatArrayToFloatBuffer(verts);
        colorBuffer = Tools.floatArrayToFloatBuffer(color);
        modelViewMatrix = new Matrix4f(); // Init Modelview matrix as Identity matrix
        modelViewMatrix.scale(new Vector3f(0.1f, 0.1f, 0.1f));
        modelViewBuffer = Tools.floatArrayToFloatBuffer(new float[16]);
        modelViewMatrix.store(modelViewBuffer);
        modelViewBuffer.position(0);
    }

    @Override
    public void draw() {
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
        Vector3f rotateAxis;
        if (clockwise) {
            rotateAxis = new Vector3f(0, 0, -1.0f);
        } else {
            rotateAxis = new Vector3f(0, 0, 1.0f);
        }
        modelViewMatrix.rotate(radians, rotateAxis);

        modelViewMatrix.store(modelViewBuffer);
        modelViewBuffer.position(0);
    }

    @Override
    public void translate(float x, float y) {
        Vector2f translateVec = new Vector2f(x, y);
        modelViewMatrix.translate(translateVec);

        modelViewMatrix.store(modelViewBuffer);
        modelViewBuffer.position(0);
    }

    @Override
    public Vector2f getDirection() {

        return new Vector2f(modelViewMatrix.m10, modelViewMatrix.m11);
    }

    @Override
    public Vector2f getLocation() {
        return new Vector2f(modelViewMatrix.m30, modelViewMatrix.m31);
    }

}
