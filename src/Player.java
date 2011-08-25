
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

    private float currentYSpeed, currentXSpeed;

    public Player() {
        super(verts, color);

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

    @Override
    public void move(int coefficient) {
        this.translate(currentXSpeed * coefficient, currentYSpeed * coefficient);
    }
}
