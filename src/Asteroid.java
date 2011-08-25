
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

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
    private static float asteroidYSpeed = 0.0010f;
    private static float asteroidXSpeed = 0.0012f;

    public Asteroid() {
        super(verts, color);
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
    public void move(int coefficient) {
        this.translate(asteroidXSpeed * coefficient, asteroidYSpeed * coefficient);
        Vector2f location = this.getLocation();
        if (location.x > 0.9f || location.x < -0.9f) {
            asteroidXSpeed *= -1;
        }
        if (location.y > 0.9f || location.y < -0.9f) {
            asteroidYSpeed *= -1;
        }
    }
}
