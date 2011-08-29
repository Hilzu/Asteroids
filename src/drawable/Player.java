package drawable;

import org.lwjgl.opengl.GL11;

import shader.ShaderManager;
import shader.Shader;

public class Player extends Movable {

    private static final float[] VERTS = {
        0.0f, 0.05f, 0.0f,
        -0.03f, -0.03f, 0.0f,
        0.03f, -0.03f, 0.0f
    };
    private static final float r = 0;
    private static final float g = .5f;
    private static final float b = .5f;
    private static final float[] COLORS = {
        r, g, b,
        r, g, b,
        r, g, b
    };
    private static final float Y_SPEED = 0.001f;
    private static final float X_SPEED = 0.0008f;

    private float currentYSpeed, currentXSpeed;

    public Player() {
        super(VERTS, COLORS);

        currentYSpeed = 0;
        currentXSpeed = 0;
    }

    @Override
    public void draw() {
        super.draw();

        ShaderManager.useShader(Shader.FLAT, modelViewBuffer);

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
