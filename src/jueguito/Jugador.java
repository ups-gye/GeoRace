package jueguito;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Jugador {
    private int x, y, dx, dy;
    public final int ANCHO_CARRO = 50;
    public final int ALTO_CARRO = 20;

    public Jugador() {
        x = 100;
        y = 100;
    }

    public void mover(int ancho, int alto) {
        x += dx;
        y += dy;

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x > ancho - ANCHO_CARRO) {
            x = ancho - ANCHO_CARRO;
        }
        if (y > alto - ALTO_CARRO) {
            y = alto - ALTO_CARRO;
        }
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, ANCHO_CARRO, ALTO_CARRO); // Dibuja un rectángulo en lugar de un círculo
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -1;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

    public int getX() {
        return x;
    }
}