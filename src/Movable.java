
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Movable implements Drawable {

    protected Matrix4f modelViewMatrix;

    public Movable() {
        modelViewMatrix = new Matrix4f();
    }

    public void rotate(boolean clockwise, float radians) {
        Vector3f rotateAxis;
        if (clockwise) {
            rotateAxis = new Vector3f(0, 0, -1.0f);
        } else {
            rotateAxis = new Vector3f(0, 0, 1.0f);
        }
        modelViewMatrix.rotate(radians, rotateAxis);
    }

    public void translate(float x, float y) {
        Vector2f translateVec = new Vector2f(x, y);
        modelViewMatrix.translate(translateVec);
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

    public abstract void move(int coefficient);
}
