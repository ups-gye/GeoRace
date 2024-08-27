package prueba;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Lives {
    private int lives;
    private final int maxLives = 3;
    private Image heartImage;

    public Lives(int initialLives, String imagePath) {
        this.lives = initialLives;
        loadHeartImage(imagePath);
    }

    private void loadHeartImage(String imagePath) {
        try {
            URL imageURL = getClass().getResource(imagePath);
            if (imageURL != null) {
                this.heartImage = ImageIO.read(imageURL);
            } else {
                throw new IOException("Image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public void gainLife() {
        if (lives < maxLives) {
            lives++;
        }
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public void draw(Graphics g) {
        int heartWidth = heartImage.getWidth(null);
        for (int i = 0; i < lives; i++) {
            g.drawImage(heartImage, 10 + i * (heartWidth + 10), 10, null);
        }
    }
}
