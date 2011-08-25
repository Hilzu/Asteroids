package drawable;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import tool.Tools;

public abstract class Movable extends Drawable {

    protected Matrix4f modelViewMatrix;
    protected FloatBuffer modelViewBuffer;
    protected boolean transformed;

    public Movable(float[] verts, float[] colors) {
        super(verts, colors);
        modelViewMatrix = new Matrix4f();
        transformed = true;
        modelViewBuffer = Tools.floatArrayToFloatBuffer(new float[16]);
    }

    public void rotate(boolean clockwise, float radians) {
        Vector3f rotateAxis;
        if (clockwise) {
            rotateAxis = new Vector3f(0, 0, -1.0f);
        } else {
            rotateAxis = new Vector3f(0, 0, 1.0f);
        }
        modelViewMatrix.rotate(radians, rotateAxis);
        transformed = true;
    }

    public void translate(float x, float y) {
        Vector2f translateVec = new Vector2f(x, y);
        modelViewMatrix.translate(translateVec);
        transformed = true;
    }

    public Vector2f getDirection() {
        return new Vector2f(modelViewMatrix.m10, modelViewMatrix.m11);
    }

    public Vector2f getLocation() {
        return new Vector2f(modelViewMatrix.m30, modelViewMatrix.m31);
    }

    public void move() {
        this.move(1);
    }

    @Override
    public void draw() {
        if (transformed) {
            modelViewMatrix.store(modelViewBuffer);
            modelViewBuffer.position(0);
            transformed = false;
        }
    }

    public abstract void move(int coefficient);
}
