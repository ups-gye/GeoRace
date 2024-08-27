package prueba;

import javax.swing.*;
import java.awt.*;

public class FinalInterface extends JFrame {
    public FinalInterface(int finalScore) {
        setTitle("Juego Completado");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel("¡Has completado todas las preguntas!");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Puntaje Final: " + finalScore);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton closeButton = new JButton("Cerrar");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> System.exit(0));

        panel.add(messageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(closeButton);

        add(panel);
    }
}
