package drawable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import shader.Shader;
import shader.ShaderManager;
import tool.Tools;

public class Bullet extends Movable {

    private static final float[] vert = {0, 0, 0};
    private static final float[] color = {1, 1, 1};

    public Bullet() {
        super(vert, color);
    }

    @Override
    public void draw() {
        super.draw();

        ShaderManager.useShader(Shader.BULLET, modelViewBuffer, colorBuffer);

        Tools.dataToVertexBufferObject(vertsBuffer);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        Tools.dataToVertexBufferObject(colorBuffer);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
    }

    @Override
    public void move(int coefficient) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
