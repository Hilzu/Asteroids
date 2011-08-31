package drawable;

import org.lwjgl.opengl.GL11;
import shader.Shader;
import shader.ShaderManager;

public class Square extends Movable {

    public Square(float[] verts, float[] colors) {
        super(verts, colors);
    }

    public static Square constructSquare(float width, float height) {
        float[] verts = {
            -width / 2, -height / 2, 0,
            -width / 2, height / 2, 0,
            width / 2, height / 2, 0,
            width / 2, -height / 2, 0,};
        float[] color = {
            1, 1, 1,
            1, 1, 1,
            1, 1, 1,
            1, 1, 1
        };
        return new Square(verts, color);
    }

    @Override
    public void draw() {
        super.draw();
        ShaderManager.useShader(Shader.FLAT, modelViewBuffer);
        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 4);
    }

    @Override
    public void move(int coefficient) {
        return;
    }
}
