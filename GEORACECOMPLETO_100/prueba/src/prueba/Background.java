package prueba;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

class Background {
    int x1, x2;
    int y;
    int width, height;
    Image image;

    public Background(int x, int y, int width, int height, String imagePath) {
        this.x1 = x;
        this.x2 = x + width;
        this.y = y;
        this.width = width;
        this.height = height;
        loadImage(imagePath);
    }

    public void loadImage(String imagePath) {
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
        g.drawImage(image, x1, y, width, height, null);
        g.drawImage(image, x2, y, width, height, null);
    }

    public void move() {
        x1 += 5;
        x2 += 5;
        if (x1 >= width) {
            x1 = x2 - width;
        }
        if (x2 >= width) {
            x2 = x1 - width;
        }
    }
}
