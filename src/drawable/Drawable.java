package drawable;

import java.nio.FloatBuffer;

import tool.Tools;
// TODO: Make constructor that takes used shader and primitive as arguments.
public abstract class Drawable {

    protected FloatBuffer vertsBuffer;
    protected FloatBuffer colorBuffer;

    public Drawable(float[] verts, float[] colors) {
        vertsBuffer = Tools.floatArrayToFloatBuffer(verts);
        colorBuffer = Tools.floatArrayToFloatBuffer(colors);
    }

    public abstract void draw();    // TODO: Move basic drawing logic here.
}
