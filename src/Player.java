
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
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
        
        int buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        
        buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
        
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }

    @Override
    public void move(float angle, float distance) {
        Vector3f rotateAxis = new Vector3f(0, 0, -1.0f);
        rotateAxis.normalise();
        modelView.rotate(angle, rotateAxis);
        
        Vector2f distVec = new Vector2f(0, distance);
        modelView.translate(distVec);
        
        modelView.store(mvpBuffer);
        mvpBuffer.position(0);
    }
}
