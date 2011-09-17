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
    /**
     * Distance from center to bounding box sides.
     */
    protected float boundingBoxX;
    /**
     * Distance from center to bounding box top and bottom.
     */
    protected float boundingBoxY;

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
    /**
     * Rotate this to point towards point indicated by given vector.
     * @param newDirection Vector to rotate towards.
     */
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
    
    /**
     * Move to a certain location on the screen.
     * @param location New location of this.
     */
    public void moveTo(Vector2f location) {
        this.moveTo(location.x, location.y);
    }

    public void moveTo(float x, float y) {
        modelViewMatrix.m30 = x;
        modelViewMatrix.m31 = y;
        transformed = true;
    }
    
    /**
     * Gets the direction of this. Direction is returned in relation of y-axis.
     * @return Direction relative to the y-axis.
     */
    public Vector2f getDirection() {
        return new Vector2f(modelViewMatrix.m10, modelViewMatrix.m11);
    }

    public Vector2f getLocation() {
        return new Vector2f(modelViewMatrix.m30, modelViewMatrix.m31);
    }
    
    /**
     * Move this according to it's logic, which might be an algorithm or keyboard input.
     */
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
    
    /**
     * Move this and adjust movement using the given coefficient. Coefficient usually is the frameDelta time to make moving FPS-independent.
     * @param coefficient 
     */
    public abstract void move(int coefficient);

    public float getCollisionBoxX() {
        return boundingBoxX;
    }

    public float getCollisionBoxY() {
        return boundingBoxY;
    }
    
    /**
     * Check if this is colliding with the given object.
     * @param m object to check collision with.
     * @return true of colliding, false otherwise.
     */
    public boolean isColliding(Movable m) {
        return isColliding(this, m);
    }

    public static boolean isColliding(Movable a, Movable b) {
        Vector2f aLoc = a.getLocation();
        Vector2f bLoc = b.getLocation();

        float aXMin = aLoc.x - a.boundingBoxX;
        float aXMax = aLoc.x + a.boundingBoxX;
        float aYMin = aLoc.y - a.boundingBoxY;
        float aYMax = aLoc.y + a.boundingBoxY;

        float bXMin = bLoc.x - b.boundingBoxX;
        float bXMax = bLoc.x + b.boundingBoxX;
        float bYMin = bLoc.y - b.boundingBoxY;
        float bYMax = bLoc.y + b.boundingBoxY;

        //Drawing of bounding boxes.
//        float[] verts = {
//            aXMin, aYMin, 0,
//            aXMin, aYMax, 0,
//            aXMax, aYMax, 0,
//            aXMax, aYMin, 0
//        };
//        float[] color = {
//            1, 1, 1,
//            1, 1, 1,
//            1, 1, 1,
//            1, 1, 1
//        };
//        Square box = new Square(verts, color);
//        box.draw();
//
//        float[] verts2 = {
//            bXMin, bYMin, 0,
//            bXMin, bYMax, 0,
//            bXMax, bYMax, 0,
//            bXMax, bYMin, 0
//        };
//        box = new Square(verts2, color);
//        box.draw();

        if (aXMin > bXMax || aXMax < bXMin || aYMin > bYMax || aYMax < bYMin) {
            return false;
        }

        return true;
    }
}
