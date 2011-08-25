package drawable;

import java.nio.FloatBuffer;

import tool.Tools;

public abstract class Drawable {

    protected FloatBuffer vertsBuffer;
    protected FloatBuffer colorBuffer;

    public Drawable(float[] verts, float[] colors) {
        vertsBuffer = Tools.floatArrayToFloatBuffer(verts);
        colorBuffer = Tools.floatArrayToFloatBuffer(colors);
    }

    public abstract void draw();
}
