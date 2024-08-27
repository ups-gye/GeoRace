package prueba;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarGame extends JFrame implements KeyListener {

    private Car car;
    private Road road;
    private Background background;
    private Grass grass;
    private List<Obstacle> obstacles;
    private List<QuestionDialog> questionDialogs; 
    private List<Question> questions;
    private Random rand;
    private Lives lives;
    private boolean collisionOccurred;
    private boolean invulnerable;
    private Timer moveTimer;
    private Timer scoreTimer;
    private Timer questionTimer;
    private Timer invulnerabilityTimer;
    private int questionInterval = 10000;
    private final int roadTop = 300;
    private final int roadBottom = 500;
    private final int maxObstacles = 3;
    private final int minDistanceBetweenObstacles = 200;
    private int score;
    private int currentQuestionIndex = 0;
    private int questionCounter = 0;
    
    private Socket socket;
    private PrintWriter out;
    private String username;

    public CarGame(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
        initSocket();
        initComponents();
    }

    private void initSocket() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        this.car = new Car(100, roadTop + 50, 100, 60, "player.png");
        this.road = new Road(0, roadTop, 800, 200, "carretera.jpg");
        this.background = new Background(0, 0, 800, 300, "edad_prehis.jpg");
        this.grass = new Grass(0, 365, 900, 200, "CESPED_HIS.png");
        this.obstacles = new ArrayList<>();
        this.questions = new ArrayList<>();
        this.rand = new Random();
        this.lives = new Lives(3, "corazon.png");
        this.invulnerable = false;
        this.score = 0;

        // Inicializamos la lista de diálogos de preguntas
        this.questionDialogs = new ArrayList<>();
        initQuestions();

        setTitle("Car Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(this);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                background.draw(g);
                road.draw(g);
                car.draw(g);
                grass.draw(g);
                for (Obstacle obstacle : obstacles) {
                    obstacle.draw(g);
                }
                for (Question question : questions) {
                    question.draw(g);
                }
                lives.draw(g);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("RECORD: " + score, getWidth() - 150, 30);
                if (collisionOccurred) {
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.drawString("¡Colisión!", 350, 300);
                }
            }
        };

        add(panel);

        moveTimer = new Timer(100, e -> {
            if (!lives.isGameOver()) {
                background.move();
                road.move();
                grass.move();
                for (Obstacle obstacle : obstacles) {
                    obstacle.move(roadTop, roadBottom);
                    if (!invulnerable && obstacle.checkCollision(car.getBounds())) {
                        lives.loseLife();
                        collisionOccurred = true;
                        invulnerable = true;

                        invulnerabilityTimer = new Timer(2000, ev -> invulnerable = false);
                        invulnerabilityTimer.setRepeats(false);
                        invulnerabilityTimer.start();

                        if (lives.isGameOver()) {
                            resetGame();
                            break;
                        }
                    }
                }
                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
                    question.move(roadTop, roadBottom);
                    if (question.checkCollision(car.getBounds())) {
                        showQuestionInterface(i);
                    }
                }
                if (collisionOccurred) {
                    collisionOccurred = false;
                }
            }
            repaint();
        });
        moveTimer.start();

        scoreTimer = new Timer(1000, e -> {
            if (!lives.isGameOver()) {
                score++;
                sendScoreToServer();
            }
        });
        scoreTimer.start();

        Timer obstacleTimer = new Timer(rand.nextInt(2000) + 1000, e -> {
            if (obstacles.size() < maxObstacles && !lives.isGameOver()) {
                int yPosition = rand.nextInt(roadBottom - roadTop - 50) + roadTop;
                if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).x > minDistanceBetweenObstacles) {
                    if (rand.nextBoolean()) {
                        obstacles.add(new Obstacle(-50, yPosition, 50, 50, "piedra.png"));
                    } else {
                        obstacles.add(new Obstacle(-50, yPosition, 50, 50, "piedra2.png"));
                    }
                }
            }
            ((Timer) e.getSource()).setDelay(rand.nextInt(2000) + 1000);
        });
        obstacleTimer.start();

        questionTimer = new Timer(questionInterval, e -> {
            if (questions.size() < 1 && !lives.isGameOver()) {
                int yPosition = rand.nextInt(roadBottom - roadTop - 50) + roadTop;
                questions.add(new Question(-50, yPosition, 50, 50, "pregunta.png"));
                questionInterval += 5000;
                questionTimer.setDelay(questionInterval);
            }
        });
        questionTimer.start();

        setVisible(true);
    }

    private void resetGame() {
        lives = new Lives(3, "corazon.png");
        score = 0;
        car = new Car(100, roadTop + 50, 100, 60, "player.png");
        obstacles.clear();
        questions.clear();
        questionInterval = 10000;
        questionTimer.setDelay(questionInterval);
        invulnerable = false;
        collisionOccurred = false;
        currentQuestionIndex = 0;
        questionCounter = 0;
        changeBackground("edad_prehis.jpg"); 
        grass.changeGrassImage("CESPED_HIS.png");
    }

    private void showQuestionInterface(int questionIndex) {
        pauseGame();

        QuestionDialog dialog = questionDialogs.get(currentQuestionIndex);
        dialog.showQuestion();

        questions.remove(questionIndex);

        currentQuestionIndex = (currentQuestionIndex + 1) % questionDialogs.size();

        dialog.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                questionCounter++;
                if (dialog.isCorrect()) {
                    score += 100;
                    if (lives.getLives() < 3) {
                        lives.gainLife();
                    }
                }

                if (questionCounter % 5 == 0) {
                    switch ((questionCounter / 5) % 4) {
                        case 1:
                            changeBackground("edad_antigua.jpg");
                            grass.changeGrassImage("arena.png");
                            break;
                        case 2:
                            changeBackground("edad_media.jpg");
                            grass.changeGrassImage("tierra.png");
                            break;
                        case 3:
                            changeBackground("edad_moderna.jpg");
                            grass.changeGrassImage("muelle.png");
                            break;
                        default:
                            changeBackground("edad_prehis.jpg");
                            grass.changeGrassImage("CESPED_HIS.png");
                            break;
                    }
                }

                if (questionCounter == 20) {
                    new FinalInterface(score).setVisible(true);
                    dispose();
                } else {
                    resumeGame();
                }
            }
        });
    }

    private void pauseGame() {
        moveTimer.stop();
        scoreTimer.stop();
        questionTimer.stop();
    }

    private void resumeGame() {
        moveTimer.start();
        scoreTimer.start();
        questionTimer.start();
    }
    

    private void initQuestions() {
        String[] questionsArray = {
            "¿Cuál era una característica común del Paleolítico superior?",
            "¿Qué innovación tecnológica es característica del Neolítico?",
            "¿Cuál era una función principal de las pinturas rupestres durante el Paleolítico?",
            "¿Qué civilización antigua es conocida por la construcción de las pirámides?",
            "¿Qué era un desarrollo importante durante la Edad del Bronce?",
            
            "¿Qué filósofo griego es conocido por su método socrático de enseñanza?",
            "¿Quién fue el primer emperador romano?",
            "¿Cuál era la capital del Imperio Romano en su apogeo?",
            "¿Quién escribió 'La Ilíada' y 'La Odisea'?",
            "¿Cuál fue la principal religión del Imperio Romano?",
            
            "¿Cuál fue el emperador del Sacro Imperio Romano Germánico que fue coronado por el papa en 800 d.C.?",
            "¿Qué evento marcó el comienzo de la Edad Media en Europa?",
            "¿Quién fue el líder normando que conquistó Inglaterra en 1066?",
            "¿Qué orden religiosa fue conocida por sus caballeros guerreros durante las Cruzadas?",
            "¿Cuál era la capital del Imperio Bizantino?",
            
            "¿Quién fue el monarca inglés que rompió con la Iglesia católica y estableció la Iglesia de Inglaterra?",
            "¿Cuál fue la batalla decisiva que puso fin al dominio islámico en España en 1492?",
            "¿Quién fue el líder de la Reforma Protestante en Alemania?",
            "¿Qué tratado puso fin a la Guerra de los Treinta Años en 1648?",
            "¿Cuál fue el zar que transformó Rusia en una gran potencia europea?",
            
               
        };

        String[][] optionsArray = {
            {"Uso de herramientas de piedra pulida", "Desarrollo de la agricultura", "Domesticación de animales", "Arte rupestre"},
            {"Herramientas de piedra tallada", "Metalurgia del hierro", "Invención de la rueda", "Agricultura y ganadería"},
            {"Registro de transacciones comerciales", "Marcación de territorios de caza", "Comunicación escrita", "Ritual y mitología"},
            {"Sumerios", "Egipcios", "Fenicios", "Griegos"},
            {"Invención de la imprenta", "Construcción de grandes ciudades", "Uso de herramientas de bronce", "Comercio marítimo extensivo"},
            
            {"Platón", "Aristóteles", "Sócrates", "Heráclito"},
            {"Julio César", "Marco Aurelio", "Augusto", "Nerón"},
            {"Roma", "Atenas", "Cartago", "Alejandría"},
            {"Aristóteles", "Platón", "Homero", "Sófocles"},
            {"Cristianismo", "Judaísmo", "Zoroastrismo", "Mitología Romana"},
            
            {"Carlomagno", "Otón I", "Carlos V", "Federico II"},
            {"La caída del Imperio Romano de Occidente", "La coronación de Carlomagno", "La Batalla de Hastings", "La Plaga Negra"},
            {"Guillermo el Conquistador", "Ricardo Corazón de León", "Carlos Martel", "Harold Godwinson"},
            {"Caballeros Templarios", "Orden Teutónica", "Orden de San Juan", "Orden de Santiago"},
            {"Roma", "Atenas", "Constantinopla", "Jerusalén"},
            
            {"Enrique VIII", "Eduardo VI", "María I", "Isabel I"},
            {"Batalla de Lepanto", "Batalla de Tours", "Batalla de Las Navas de Tolosa", "Reconquista de Granada"},
            {"Martín Lutero", "Juan Calvino", "Enrique VIII", "Carlos V"},
            {"Tratado de Utrecht", "Tratado de Versalles", "Paz de Westfalia", "Paz de Augsburgo"},
            {"Pedro el Grande", "Iván el Terrible", "Miguel Románov", "Alejandro I"},
            
        };

        int[] correctAnswers = {3, 3, 3, 1, 2,  2, 2, 0, 2, 0,  0, 0, 0, 0, 2, 0, 3, 0, 2, 0};



        for (int i = 0; i < questionsArray.length; i++) {
            questionDialogs.add(new QuestionDialog(questionsArray[i], optionsArray[i], correctAnswers[i]));
        }
    }

    private void changeBackground(String imagePath) {
        background.loadImage(imagePath);
        repaint();
    }

    private void sendScoreToServer() {
        if (out != null) {
            out.println("User: " + username + " - Score: " + score);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarGame(null, "").setVisible(true));
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!lives.isGameOver()) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_UP:
                    car.moveUp(roadTop);
                    break;
                case KeyEvent.VK_DOWN:
                    car.moveDown(roadBottom);
                    break;
                case KeyEvent.VK_LEFT:
                    car.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    car.moveRight(getWidth());
                    break;
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
