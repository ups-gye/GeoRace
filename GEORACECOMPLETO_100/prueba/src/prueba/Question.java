package prueba;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

class Question {
    int x;
    int y;
    int width, height;
    Image image;

    public Question(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
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

    public void move(int roadTop, int roadBottom) {
        x += 5; // Ajusta la velocidad del objeto "Pregunta" aquí
        if (x > 800) { // Si el objeto "Pregunta" sale de la pantalla, lo reiniciamos
            x = -width;
            y = roadTop + (int) (Math.random() * (roadBottom - roadTop - height));
        }
    }

    public boolean checkCollision(Rectangle carBounds) {
        Rectangle questionBounds = new Rectangle(x, y, width, height);
        return questionBounds.intersects(carBounds);
    }
}
