package drawable;

import org.lwjgl.opengl.GL11;
import shader.Shader;
import shader.ShaderManager;

public class Square extends Movable {

    public Square(float[] verts, float[] colors) {
        super(verts, colors);
    } 
    
    public static Square constructSquare(float x, float y, float width, float height) {
        float[] verts = {
            x-width/2, y-height/2, 0,
            x-width/2, y+height/2, 0,
            x+width/2, y-height/2, 0,
            x+width/2, y+height/2, 0
        };
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
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void move(int coefficient) {
        return;
    }
}
