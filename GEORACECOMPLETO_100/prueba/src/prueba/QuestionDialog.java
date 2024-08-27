package prueba;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuestionDialog {

    private String question;
    private String[] options;
    private int correctAnswerIndex;
    private boolean isCorrect;
    private JFrame frame;

    public QuestionDialog(String question, String[] options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void showQuestion() {
        frame = new JFrame("Pregunta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel questionLabel = new JLabel("<html>" + question + "</html>");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(options.length, 1));

        ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = new JRadioButton[options.length];

        for (int i = 0; i < options.length; i++) {
            buttons[i] = new JRadioButton(options[i]);
            group.add(buttons[i]);
            optionsPanel.add(buttons[i]);
            int index = i;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isCorrect = index == correctAnswerIndex;
                }
            });
        }

        frame.add(optionsPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, isCorrect ? "Correcto" : "Incorrecto");
                frame.dispose();
            }
        });

        frame.add(submitButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
