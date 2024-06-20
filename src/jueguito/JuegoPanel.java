/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jueguito;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class JuegoPanel extends JPanel implements ActionListener {
    private Timer timer;
    private Jugador jugador;
    private List<Pregunta> preguntas;
    private Pregunta preguntaActual;
    private int indicePreguntaActual;
    private BufferedImage pistaDCarreras;

    public JuegoPanel() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        jugador = new Jugador();
        addKeyListener(new TAdapter());
        timer = new Timer(10, this);
        timer.start();

        preguntas = Pregunta.generarPreguntas();
        indicePreguntaActual = 0;
        preguntaActual = preguntas.get(indicePreguntaActual);

        try {
            pistaDCarreras = ImageIO.read(getClass().getResource("/jueguito/imagen.jpg")); // Carga la imagen de la pista
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibuja el fondo de la pista de carreras
        g.drawImage(pistaDCarreras, 0, 0, getWidth(), getHeight(), null);

        jugador.dibujar(g);

        // Dibuja la pregunta actual
        g.setColor(Color.GREEN);
        g.drawString(preguntaActual.getEnunciado(), 10, 20);

        // Dibuja las opciones de respuesta
        int y = 40;
        for (int i = 0; i < preguntaActual.getOpciones().size(); i++) {
            String opcion = preguntaActual.getOpciones().get(i);
            g.drawString((i + 1) + ". " + opcion, 10, y);
            y += 20;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jugador.mover(getWidth(), getHeight());

        // Detiene el juego cuando el carro llegue al final de la pista
        if (jugador.getX() + jugador.ANCHO_CARRO >= getWidth()) {
            timer.stop();
        }

        // Cuando el jugador llegue a la mitad del panel, muestra una nueva pregunta
        if (jugador.getX() >= getWidth() / 2) {
            System.out.println("Cambiando a la siguiente pregunta");
            indicePreguntaActual++;
            if (indicePreguntaActual < preguntas.size()) {
                preguntaActual = preguntas.get(indicePreguntaActual);
                System.out.println("Pregunta actual: " + preguntaActual.getEnunciado());
                repaint(); // Llamada manual a repaint() aquí
            } else {
                System.out.println("No hay más preguntas");
                // Si no hay más preguntas, puedes terminar el juego o reiniciarlo
                timer.stop();
            }
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            jugador.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            jugador.keyReleased(e);
        }
    }
}