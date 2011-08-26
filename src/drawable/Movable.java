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

    public void rotateTo(float x, float y) {
        this.rotateTo(new Vector2f(x, y));
    }

    public void rotateTo(Vector2f newDirection) {
        // Angle calculations assume player is at (0,0). Transform direction vector to match this assumption.
        Vector2f.sub(newDirection, this.getLocation(), newDirection);
        Vector2f playerDirection = this.getDirection();

        float angle;    // Amount to rotate player in radians
        // Direction vector is at the same position as player. Do nothing.
        if (newDirection.x == 0 && newDirection.y == 0) {
            return;
        }

        angle = Vector2f.angle(newDirection, playerDirection);

        // Assume that angle means rotation in clockwise direction.
        this.rotate(true, angle);
        // If new angle after rotation is bigger, rotation should have been in counter-clockwise direction.
        float newAngle = Vector2f.angle(newDirection, this.getDirection());
        if (newAngle > angle) {
            this.rotate(false, angle * 2);
        }
    }

    public void translate(Vector2f translateVec) {
        modelViewMatrix.translate(translateVec);
        transformed = true;
    }

    public void translate(float x, float y) {
        this.translate(new Vector2f(x, y));
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
