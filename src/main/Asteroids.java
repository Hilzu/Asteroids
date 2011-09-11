
package main;

import drawable.Asteroid;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.opengl.GL15;

public class Asteroids {
    
    private static List<Asteroid> asteroids = new LinkedList<Asteroid>();
    private static List<Asteroid> destroyQueue = new LinkedList<Asteroid>();
    private static int amountDestroyed = 0;

    public void newAsteroid() {
        Asteroid asteroid = new Asteroid();
        float x = (float) Math.random() * 2 - 1;    // -1.0 - 1.0
        float y = (float) Math.random() * 2 - 1;
        asteroid.translate(x, y);
        asteroids.add(asteroid);
    }

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }
    
    public void update() {
        for (Asteroid asteroid : destroyQueue) {
            GL15.glDeleteBuffers(asteroid.getVertBufferPointer());
            GL15.glDeleteBuffers(asteroid.getColorBufferPointer());
            asteroids.remove(asteroid);
            newAsteroid();
        }
        destroyQueue.clear();
        for (Asteroid asteroid : asteroids) {
            asteroid.move(Main.getFrameDelta());
            asteroid.draw();
            if (asteroid.isColliding(Main.getPlayer())) {
                Main.getPlayer().hit();
            }
        }
    }

    public void destroyAsteroid(Asteroid asteroid) {
        destroyQueue.add(asteroid);
        amountDestroyed++;
        System.out.println("Asteroid destroyed! Amount destroyed: " + amountDestroyed);
    }
}
