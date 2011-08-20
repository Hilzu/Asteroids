
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class Player implements Movable {

    private static final float[] verts = {
        0.0f, 0.05f, 0.0f,
        -0.03f, -0.03f, 0.0f,
        0.03f, -0.03f, 0.0f
    };
    private static final float[] color = {
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f
    };
    
    private FloatBuffer verticesBuffer;
    private FloatBuffer colorBuffer;
    private Matrix4f modelView;
    private FloatBuffer mvpBuffer;

    public Player() {
        verticesBuffer = Tools.floatArrayToFloatBuffer(verts);
        colorBuffer = Tools.floatArrayToFloatBuffer(color);
        modelView = new Matrix4f(); // Init Modelview matrix as Identity matrix
        mvpBuffer = Tools.floatArrayToFloatBuffer(new float[16]);
        modelView.store(mvpBuffer);
        mvpBuffer.position(0);
    }

    @Override
    public void draw() {
        ShaderManager.useShader(Shader.FLAT, mvpBuffer, colorBuffer);

        GL20.glVertexAttribPointer(0, 3, false, 0, verticesBuffer);
        GL20.glVertexAttribPointer(1, 3, false, 0, colorBuffer);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }

    @Override
    public void move(float angle, float distance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}