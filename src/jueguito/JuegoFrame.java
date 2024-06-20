package jueguito;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.JFrame;

public class JuegoFrame extends JFrame {
    public JuegoFrame() {
        setTitle("Juego Básico");
        setSize(800, 600);
        setResizable(false);
        JuegoPanel panel = new JuegoPanel();
        add(panel);
    }
}
