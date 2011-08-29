package drawable;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import tool.Tools;

public abstract class Drawable {

    protected FloatBuffer vertsBuffer;
    protected FloatBuffer colorBuffer;
    private int vertBufferPointer;
    private int colorBufferPointer;

    public Drawable(float[] verts, float[] colors) {
        vertsBuffer = Tools.floatArrayToFloatBuffer(verts);
        colorBuffer = Tools.floatArrayToFloatBuffer(colors);
        vertBufferPointer = Tools.createVBOBuffer();
        colorBufferPointer = Tools.createVBOBuffer();
    }

    public void draw() {
        Tools.dataToVertexBufferObject(vertsBuffer, vertBufferPointer);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        Tools.dataToVertexBufferObject(colorBuffer, colorBufferPointer);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
    }    
}
