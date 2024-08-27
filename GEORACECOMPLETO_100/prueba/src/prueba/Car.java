package prueba;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

class Car {
    int x, y;
    int width, height;
    Image image;

    public Car(int x, int y, int width, int height, String imagePath) {
        this.x = 650;
        this.y = 300;
        this.width = width;
        this.height = height;
        try {
            URL imageURL = getClass().getResource(imagePath);
            if (imageURL != null) {
                this.image = ImageIO.read(imageURL);
            } else {
                throw new IOException("Image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public void moveUp(int roadTop) {
        if (y - 10 >= roadTop) {
            y -= 10;
        }
    }

    public void moveDown(int roadBottom) {
        if (y + height + 10 <= roadBottom) {
            y += 10;
        }
    }

    public void moveLeft() {
        if (x - 10 >= 0) {
            x -= 10;
        }
    }

    public void moveRight(int windowWidth) {
        if (x + width + 10 <= windowWidth) {
            x += 10;
        }
    }

    public Rectangle getBounds() {
        // Ajustar el tamaño del rectángulo de colisión para que sea un poco más pequeño que la imagen del coche
        int collisionPadding = 20; // Ajusta este valor según sea necesario
        return new Rectangle(x + collisionPadding, y + collisionPadding, width - 2 * collisionPadding, height - 2 * collisionPadding);
    }
}
