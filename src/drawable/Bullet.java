package drawable;

import org.lwjgl.opengl.GL11;

import shader.Shader;
import shader.ShaderManager;

public class Bullet extends Movable {

    private static final float[] VERT = {0, 0, 0};
    private static final float[] COLOR = {.98f, .04f, .7f};
    private static final float SPEED = 0.002f;

    public Bullet() {
        super(VERT, COLOR);
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
}
