package drawable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

import shader.Shader;
import shader.ShaderManager;
import tool.Tools;

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

        Tools.dataToVertexBufferObject(vertsBuffer);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        Tools.dataToVertexBufferObject(colorBuffer);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 8);
    }

    @Override
    public void move(int coefficient) { // FIXME: Asteroid sometimes get stuck at borders
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
