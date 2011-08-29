package drawable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import shader.Shader;
import shader.ShaderManager;

public class Asteroid extends Movable {

    private static final float[] VERTS = {
        0.0f, 0.0f, 0.0f,
        0.05f, 0.087f, 0.0f,
        -0.05f, 0.087f, 0.0f,
        -0.1f, 0.0f, 0.0f,
        -0.05f, -0.087f, 0.0f,
        0.05f, -0.087f, 0.0f,
        0.1f, 0.0f, 0.0f,
        0.05f, 0.087f, 0.0f
    };
    private static final float r = .2f;
    private static final float g = .15f;
    private static final float b = .1f;
    private static final float[] COLORS = {
        r, g, b,
        r, g, b,
        r, g, b,
        r, g, b,
        r, g, b,
        r, g, b,
        r, g, b,
        r, g, b
    };
    private static float asteroidYSpeed = 0.0010f;
    private static float asteroidXSpeed = 0.0012f;

    public Asteroid() {
        super(VERTS, COLORS);
    }

    @Override
    public void draw() {
        super.draw();

        ShaderManager.useShader(Shader.FLAT, modelViewBuffer, colorBuffer);

        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 8);
    }

    @Override
    public void move(int coefficient) {
        this.translate(asteroidXSpeed * coefficient, asteroidYSpeed * coefficient);
        Vector2f location = this.getLocation();
        if (location.x > 0.9f || location.x < -0.9f) {
            moveTo(0.9f * Math.signum(location.x), location.y);
            asteroidXSpeed *= -1;
        }
        if (location.y > 0.9f || location.y < -0.9f) {
            moveTo(location.x, 0.9f * Math.signum(location.y));
            asteroidYSpeed *= -1;
        }
    }

    @Override
    public String toString() {
        Vector2f location = this.getLocation();
        return String.format("Asteroid (%f, %f)", location.getX(), location.getY());
    }  
}
