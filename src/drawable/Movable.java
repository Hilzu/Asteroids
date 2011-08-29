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
    protected float collisionBoxH;

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
        // Vectors are directions from (0, 0). Substracting location from direction transforms direction to work with objects not at (0, 0).
        // Bind newDirection to a new Vector so that the original remains untouched.
        newDirection = Vector2f.sub(newDirection, this.getLocation(), null);
        Vector2f currentDirection = this.getDirection();

        // Direction vector is the same as current location.
        if (newDirection.x == 0 && newDirection.y == 0) {
            return;
        }
        // Amount to rotate This in radians
        float angle = Vector2f.angle(newDirection, currentDirection);

        // Assume that angle means rotation in clockwise direction.
        this.rotate(true, angle);
        // If new angle after rotation is not close to zero, rotation should have been in counter-clockwise direction.
        float newAngle = Vector2f.angle(newDirection, this.getDirection());
        if (newAngle > 0.05f) {
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

    public void moveTo(Vector2f location) {
        this.moveTo(location.x, location.y);
    }

    public void moveTo(float x, float y) {
        modelViewMatrix.m30 = x;
        modelViewMatrix.m31 = y;
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
        super.draw();
    }

    public abstract void move(int coefficient);

    public float getCollisionBoxRadius() {
        return collisionBoxH;
    }
    
    
}
