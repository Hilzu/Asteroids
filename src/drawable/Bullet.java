package drawable;

import org.lwjgl.opengl.GL11;

import org.lwjgl.util.vector.Vector2f;
import shader.Shader;
import shader.ShaderManager;

public class Bullet extends Movable {

    private static final float[] VERT = {0, 0, 0};
    private static final float[] COLOR = {.98f, .04f, .7f};
    private final float SPEED = 0.002f;

    public Bullet() {
        super(VERT, COLOR);
        boundingBoxX = 0.01f;
        boundingBoxY = 0.01f;
    }

    @Override
    public void draw() {
        super.draw();

        ShaderManager.useShader(Shader.BULLET, modelViewBuffer, colorBuffer);

        GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
    }

    @Override
    public void move(int coefficient) {
        this.translate(0, SPEED * coefficient);
    }

    @Override
    public String toString() {
        Vector2f location = this.getLocation();
        return String.format("Bullet (%f, %f)", location.getX(), location.getY());
    }
}
