package main;

import drawable.Bullet;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;

public class Bullets {

    private List<Bullet> bullets;

    public Bullets() {
        bullets = new LinkedList<Bullet>();
    }

    public void newBullet(Vector2f location, Vector2f direction) {
        Bullet bullet = new Bullet();

        bullet.translate(location);
        bullet.rotateTo(direction);

        // Offset bullet so that it spawns at ships tip.
        bullet.translate(0, .04f);

        bullets.add(bullet);

        Main.movables.add(bullet);
        Main.drawables.add(bullet);
    }

    public void removeOutOfViewBullets() {
        List<Bullet> bulletsToRemove = new LinkedList<Bullet>();
        for (Bullet bullet : bullets) {
            Vector2f bulletLocation = bullet.getLocation();
            if (bulletLocation.x < -1.0) {
                bulletsToRemove.add(bullet);
                continue;
            }
            if (bulletLocation.x > 1.0) {
                bulletsToRemove.add(bullet);
                continue;
            }
            if (bulletLocation.y < -1.0) {
                bulletsToRemove.add(bullet);
                continue;
            }
            if (bulletLocation.y > 1.0) {
                bulletsToRemove.add(bullet);
                continue;
            }
        }
        for (Bullet bullet : bulletsToRemove) {
            GL15.glDeleteBuffers(bullet.getVertBufferPointer());
            GL15.glDeleteBuffers(bullet.getColorBufferPointer());
            Main.drawables.remove(bullet);
            Main.movables.remove(bullet);
            bullets.remove(bullet);
        }
    }
}
