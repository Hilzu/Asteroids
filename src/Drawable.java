
import org.lwjgl.util.vector.Matrix4f;


public abstract class Drawable {
    
    abstract void draw();

    abstract void draw(Matrix4f mvp);
}
