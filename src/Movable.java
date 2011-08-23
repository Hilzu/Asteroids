
import org.lwjgl.util.vector.Vector2f;

interface Movable extends Drawable {

    public void rotate(boolean clockwise, float radians);

    public void translate(float x, float y);

    public Vector2f getDirection();

    public Vector2f getLocation();
}
