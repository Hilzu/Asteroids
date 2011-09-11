package main;

import drawable.Asteroid;
import drawable.Bullet;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;

public class Bullets {

    private static List<Bullet> bullets = new LinkedList<Bullet>();
    private Asteroids asteroids;
    private List<Bullet> destroyQueue = new LinkedList<Bullet>();

    public Bullets() {
        asteroids = new Asteroids();
    }

    public void newBullet(Vector2f location, Vector2f direction) {
        Bullet bullet = new Bullet();

        bullet.translate(location);
        bullet.rotateTo(direction);

        // Offset bullet so that it spawns at ships tip.
        bullet.translate(0, .04f);

        bullets.add(bullet);
    }

    public void markOutOfViewBullets() {
        for (Bullet bullet : bullets) {
            Vector2f bulletLocation = bullet.getLocation();
            if (bulletLocation.x < -1.0) {
                destroyQueue.add(bullet);
                continue;
            }
            if (bulletLocation.x > 1.0) {
                destroyQueue.add(bullet);
                continue;
            }
            if (bulletLocation.y < -1.0) {
                destroyQueue.add(bullet);
                continue;
            }
            if (bulletLocation.y > 1.0) {
                destroyQueue.add(bullet);
                continue;
            }
        }
    }
    
    public void checkAsteroidCollisions() {
        for (Bullet bullet : bullets) {
            for (Asteroid asteroid : asteroids.getAsteroids()) {
                if (bullet.isColliding(asteroid)) {
                    asteroids.destroyAsteroid(asteroid);
                    this.destroyBullet(bullet);
                }
            }
        }
    }
    
    public void update() {
        this.markOutOfViewBullets();
        for (Bullet bullet : destroyQueue) {
            GL15.glDeleteBuffers(bullet.getVertBufferPointer());
            GL15.glDeleteBuffers(bullet.getColorBufferPointer());
            bullets.remove(bullet);
        }
        destroyQueue.clear();
        
        for (Bullet bullet : bullets) {
            bullet.move(Main.getFrameDelta());
            bullet.draw();
        }
        this.checkAsteroidCollisions();
    }

    public void destroyBullet(Bullet bullet) {
        destroyQueue.add(bullet);
    }
}
