
import org.lwjgl.util.vector.Matrix4f;


public interface Drawable {
    
    void draw();

    void draw(Matrix4f mvp);
}
