
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Player extends Movable {

    private static final float[] verts = {
        0.0f, 0.05f, 0.0f,
        -0.03f, -0.03f, 0.0f,
        0.03f, -0.03f, 0.0f
    };
    private static final float[] color = {
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f
    };
    private static final float Y_SPEED = 0.001f;
    private static final float X_SPEED = 0.001f;
    private FloatBuffer vertsBuffer;
    private FloatBuffer colorBuffer;
    private FloatBuffer modelViewBuffer;
    private boolean transformed;
    private float currentYSpeed, currentXSpeed;

    public Player() {
        super();
        vertsBuffer = Tools.floatArrayToFloatBuffer(verts);
        colorBuffer = Tools.floatArrayToFloatBuffer(color);
        modelViewBuffer = Tools.floatArrayToFloatBuffer(new float[16]);
        transformed = true;
        currentYSpeed = 0;
        currentXSpeed = 0;
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

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
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

    public void moveVertically(boolean forward) {
        if (forward) {
            currentYSpeed += Y_SPEED;
        } else {
            currentYSpeed -= Y_SPEED;
        }
    }

    public void moveSideways(boolean left) {
        if (left) {
            currentXSpeed -= X_SPEED;
        } else {
            currentXSpeed += X_SPEED;
        }
    }

    public void move() {
        this.translate(currentXSpeed * Main.frameDelta, currentYSpeed * Main.frameDelta);
    }
}
