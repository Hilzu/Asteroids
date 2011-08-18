
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Triangle extends Drawable {

    private FloatBuffer verticesBuffer;
    private FloatBuffer colorBuffer;

    public Triangle(float[] vertices, float[] colors) {
        verticesBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesBuffer.put(vertices).position(0);

        colorBuffer = ByteBuffer.allocateDirect(colors.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(colors).position(0);
    }

    @Override
    public void draw() {

        ShaderManager.useShader(Shader.SIMPLE);

        GL20.glVertexAttribPointer(0, 3, false, 0, verticesBuffer);
        GL20.glVertexAttribPointer(1, 3, false, 0, colorBuffer);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }
}