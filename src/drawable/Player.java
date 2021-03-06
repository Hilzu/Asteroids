package drawable;

import main.Bullets;
import main.Main;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import shader.ShaderManager;
import shader.Shader;

public class Player extends Movable {

    private static final float[] VERTS = {
        0.0f, 0.05f, 0.0f,
        -0.03f, -0.05f, 0.0f,
        0.03f, -0.05f, 0.0f
    };
    private static final float RED = 0;
    private static final float GREEN = .5f;
    private static final float BLUE = .5f;
    private static final float[] COLORS = {
        RED, GREEN, BLUE,
        RED, GREEN, BLUE,
        RED, GREEN, BLUE
    };
    public static final int KEY_FORWARD = Keyboard.KEY_W;
    public static final int KEY_BACKWARD = Keyboard.KEY_S;
    public static final int KEY_LEFT = Keyboard.KEY_A;
    public static final int KEY_RIGHT = Keyboard.KEY_D;
    public static final int BTN_SHOOT = 0;
    private static final float Y_SPEED = 0.001f;
    private static final float X_SPEED = 0.0008f;
    private boolean forwardThrust;
    private boolean backwardThrust;
    private boolean leftThrust;
    private boolean rightThrust;
    private static Bullets bullets = new Bullets();
    private float currentYSpeed, currentXSpeed;
    private int hitsAmount;

    public Player() {
        super(VERTS, COLORS);
        calculateBoundingBox();
        currentYSpeed = 0;
        currentXSpeed = 0;
        hitsAmount = 0;
        forwardThrust = false;
        backwardThrust = false;
        leftThrust = false;
        rightThrust = false;
    }

    @Override
    public void draw() {
        super.draw();

        calculateBoundingBox();

        ShaderManager.useShader(Shader.FLAT, modelViewBuffer);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }

    private void calculateBoundingBox() {
        Vector4f vertex1 = new Vector4f(VERTS[0], VERTS[1], VERTS[2], 1);
        Vector4f vertex2 = new Vector4f(VERTS[3], VERTS[4], VERTS[5], 1);
        Vector4f vertex3 = new Vector4f(VERTS[6], VERTS[7], VERTS[8], 1);
        Vector4f position = new Vector4f(modelViewMatrix.m30, modelViewMatrix.m31, modelViewMatrix.m32, modelViewMatrix.m33);

        Matrix4f.transform(modelViewMatrix, vertex1, vertex1);
        Matrix4f.transform(modelViewMatrix, vertex2, vertex2);
        Matrix4f.transform(modelViewMatrix, vertex3, vertex3);

        float minX = Math.min(Math.min(vertex1.x, vertex2.x), vertex3.x) - position.x;
        float minY = Math.min(Math.min(vertex1.y, vertex2.y), vertex3.y) - position.y;
        float maxX = Math.max(Math.max(vertex1.x, vertex2.x), vertex3.x) - position.x;
        float maxY = Math.max(Math.max(vertex1.y, vertex2.y), vertex3.y) - position.y;

        boundingBoxX = Math.max(Math.abs(minX), Math.abs(maxX));
        boundingBoxY = Math.max(Math.abs(minY), Math.abs(maxY));
    }

    @Override
    public void move(int coefficient) {
        float accel = 0.000005f;
        float ySpeedLimit = 0.001f;
        float xSpeedLimit = 0.001f;
        if (forwardThrust) {
            currentYSpeed += accel * coefficient;
            if (currentYSpeed > ySpeedLimit) {
                currentYSpeed = ySpeedLimit;
            }
        }
        if (backwardThrust) {
            currentYSpeed -= accel * coefficient;
            if (currentYSpeed < ySpeedLimit * -1) {
                currentYSpeed = ySpeedLimit * -1;
            }
        }
        if (!forwardThrust && !backwardThrust) {
            if (currentYSpeed < 0) {
                currentYSpeed += accel * coefficient;
            } else {
                currentYSpeed -= accel * coefficient;
            }
        }
        if (leftThrust) {
            currentXSpeed -= accel * coefficient;
            if (currentXSpeed < xSpeedLimit * -1) {
                currentXSpeed = xSpeedLimit * -1;
            }
        }
        if (rightThrust) {
            currentXSpeed += accel * coefficient;
            if (currentXSpeed > xSpeedLimit) {
                currentXSpeed = xSpeedLimit;
            }
        }
        
        this.translate(currentXSpeed * coefficient, currentYSpeed * coefficient);
    }

    public void hit() {
        this.moveTo(0, 0);
        hitsAmount++;
        System.out.println("Got hit! Hits: " + hitsAmount);
    }

    private void pollKeyboard() {

        while (Keyboard.next()) {
            // Key pushed event
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case KEY_FORWARD:
                        forwardThrust = true;
                        break;
                    case KEY_BACKWARD:
                        backwardThrust = true;
                        break;
                    case KEY_LEFT:
                        leftThrust = true;
                        break;
                    case KEY_RIGHT:
                        rightThrust = true;
                        break;
                }
            } // Key lifted event
            else {
                switch (Keyboard.getEventKey()) {
                    case KEY_FORWARD:
                        forwardThrust = false;
                        break;
                    case KEY_BACKWARD:
                        backwardThrust = false;
                        break;
                    case KEY_LEFT:
                        leftThrust = false;
                        break;
                    case KEY_RIGHT:
                        rightThrust = false;
                        break;
                }
            }
        }
    }

    private void pollMouse() {
        float x = Mouse.getX();
        float y = Mouse.getY();
        // Project window coordinates to normal [-1.0, 1.0] coordinate space
        x -= Main.DISPLAY_WIDTH / 2;     // [0, 800] => [-400, 400]
        y -= Main.DISPLAY_HEIGHT / 2;
        x /= Main.DISPLAY_WIDTH / 2;     // [-400, 400] => [-1, 1]
        y /= Main.DISPLAY_HEIGHT / 2;
        Vector2f mouseDirection = new Vector2f(x, y);

        this.rotateTo(mouseDirection);

        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                switch (Mouse.getEventButton()) {
                    case BTN_SHOOT: {
                        bullets.newBullet(this.getLocation(), mouseDirection);
                        break;
                    }
                }
            }
        }
    }

    public void update() {
        pollMouse();
        pollKeyboard();
        this.move(Main.getFrameDelta());
        this.draw();
    }
}
