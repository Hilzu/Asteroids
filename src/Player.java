
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

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
        super.draw();
        
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

    public void rotateTo(Vector2f newDirection) {
        // Angle calculations assume player is at (0,0). Transform direction vector to match this assumption.
        Vector2f.sub(newDirection, this.getLocation(), newDirection);
        Vector2f playerDirection = this.getDirection();

        float angle;    // Amount to rotate player in radians
        // Direction vector is at the same position as player. Do nothing.
        if (newDirection.x == 0 && newDirection.y == 0) {
            return;
        }

        angle = Vector2f.angle(newDirection, playerDirection);

        // Assume that angle means rotation in clockwise direction.
        this.rotate(true, angle);
        // If new angle after rotation is bigger, rotation should have been in counter-clockwise direction.
        float newAngle = Vector2f.angle(newDirection, this.getDirection());
        if (newAngle > angle) {
            this.rotate(false, angle * 2);
        }
    }
}
