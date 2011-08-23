
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

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
    private FloatBuffer vertsBuf;
    private FloatBuffer colorBuf;
    private Matrix4f modelView;
    private FloatBuffer modelViewBuf;

    public Player() {
        vertsBuf = Tools.floatArrToFloatBuf(verts);
        colorBuf = Tools.floatArrToFloatBuf(color);
        modelView = new Matrix4f(); // Init Modelview matrix as Identity matrix
        modelViewBuf = Tools.floatArrToFloatBuf(new float[16]);
        modelView.store(modelViewBuf);
        modelViewBuf.position(0);
    }

    @Override
    public void draw() {
        ShaderManager.useShader(Shader.FLAT, modelViewBuf, colorBuf);

        Tools.dataToVertBufObj(vertsBuf);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        Tools.dataToVertBufObj(colorBuf);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }

    @Override
    public void rotate(boolean clockwise, float radians) {
        Vector3f rotateAxis;
        if (clockwise) {
            rotateAxis = new Vector3f(0, 0, -1.0f);
        } else {
            rotateAxis = new Vector3f(0, 0, 1.0f);
        }
        modelView.rotate(radians, rotateAxis);

        modelView.store(modelViewBuf);
        modelViewBuf.position(0);
    }

    @Override
    public void translate(float x, float y) {
        Vector2f translateVec = new Vector2f(x, y);
        modelView.translate(translateVec);

        modelView.store(modelViewBuf);
        modelViewBuf.position(0);
    }

    @Override
    public Vector2f getDirection() {

        return new Vector2f(modelView.m10, modelView.m11);
    }

    @Override
    public Vector2f getLocation() {
        return new Vector2f(modelView.m30, modelView.m31);
    }

}
