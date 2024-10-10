import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.util.Random;

public class JogoCorrida extends JPanel implements ActionListener {

    private int largura = 700;
    private int altura = 700;

    private Rectangle carroPrincipal;
    private Rectangle carroAmarelo;
    private Rectangle[] carrosOponentes;
    private Rectangle[] buracos;

    private Timer timer;

    private double velocidade = 1.5;
    private double velocidadeAmarelo = 1.5;

    private boolean jogoIniciado = false;

    private int voltasAzul = 0;
    private int voltasAmarelo = 0;

    private BufferedImage carroPrincipalImage;
    private BufferedImage carroAmareloImage;
    private BufferedImage carroOponenteImage;
    private BufferedImage ruaImage;
    private BufferedImage buracoImage;

    private JButton botaoIniciar;
    private JButton botaoReiniciar;

    private boolean moveUpAzul, moveDownAzul, moveLeftAzul, moveRightAzul;
    private boolean moveUpAmarelo, moveDownAmarelo, moveLeftAmarelo, moveRightAmarelo;

    private boolean acelerarAzul, acelerarAmarelo;

    public JogoCorrida() {
        carroPrincipal = new Rectangle(largura / 2 - 20, altura - 120, 30, 60);
        carroAmarelo = new Rectangle(largura / 2 + 20, altura - 120, 30, 60);
        carrosOponentes = new Rectangle[2];
        buracos = new Rectangle[2];

        setLayout(null);

        botaoIniciar = new JButton("Iniciar");
        botaoIniciar.setBounds(largura / 2 - 60, altura / 2 - 20, 120, 40);
        botaoIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJogo();
                botaoIniciar.setVisible(false);
            }
        });
        add(botaoIniciar);

        botaoReiniciar = new JButton("Reiniciar");
        botaoReiniciar.setBounds(largura / 2 - 60, altura / 2 + 100, 120, 40);
        botaoReiniciar.setVisible(false);
        botaoReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarJogo();
            }
        });
        add(botaoReiniciar);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_UP: moveUpAzul = true; break;
                    case KeyEvent.VK_DOWN: moveDownAzul = true; break;
                    case KeyEvent.VK_LEFT: moveLeftAzul = true; break;
                    case KeyEvent.VK_RIGHT: moveRightAzul = true; break;
                    case KeyEvent.VK_W: moveUpAmarelo = true; break;
                    case KeyEvent.VK_S: moveDownAmarelo = true; break;
                    case KeyEvent.VK_A: moveLeftAmarelo = true; break;
                    case KeyEvent.VK_D: moveRightAmarelo = true; break;
                    case KeyEvent.VK_C: acelerarAzul = true; break;
                    case KeyEvent.VK_F: acelerarAmarelo = true; break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_UP: moveUpAzul = false; break;
                    case KeyEvent.VK_DOWN: moveDownAzul = false; break;
                    case KeyEvent.VK_LEFT: moveLeftAzul = false; break;
                    case KeyEvent.VK_RIGHT: moveRightAzul = false; break;
                    case KeyEvent.VK_W: moveUpAmarelo = false; break;
                    case KeyEvent.VK_S: moveDownAmarelo = false; break;
                    case KeyEvent.VK_A: moveLeftAmarelo = false; break;
                    case KeyEvent.VK_D: moveRightAmarelo = false; break;
                    case KeyEvent.VK_C: acelerarAzul = false; break;
                    case KeyEvent.VK_F: acelerarAmarelo = false; break;
                }
            }
        });

        setFocusable(true);
        timer = new Timer(20, this);

        try {
            carroPrincipalImage = ImageIO.read(new File("C:\\Users\\letic\\OneDrive\\Desktop\\PROGRAMAÇÃO\\JogodeCorrida\\carrinho-azul.png"));
            carroAmareloImage = ImageIO.read(new File("C:\\Users\\letic\\OneDrive\\Desktop\\PROGRAMAÇÃO\\JogodeCorrida\\carrinho-amarelo.png"));
            carroOponenteImage = ImageIO.read(new File("C:\\Users\\letic\\OneDrive\\Desktop\\PROGRAMAÇÃO\\JogodeCorrida\\carrinho-branco.png"));
            ruaImage = ImageIO.read(new File("C:\\Users\\letic\\OneDrive\\Desktop\\PROGRAMAÇÃO\\JogodeCorrida\\rua.png"));
            buracoImage = ImageIO.read(new File("C:\\Users\\letic\\OneDrive\\Desktop\\PROGRAMAÇÃO\\JogodeCorrida\\buraco.png"));
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagens: " + e.getMessage());
        }

        tocarMusica("C:\\Users\\letic\\OneDrive\\Desktop\\PROGRAMAÇÃO\\JogodeCorrida\\musga.wav");
    }

    public void iniciarJogo() {
        jogoIniciado = true;
        voltasAzul = 0;
        voltasAmarelo = 0;
        velocidade = 1.5;
        velocidadeAmarelo = 1.5;
        timer.start();
        adicionarCarrosOponentes();
    }

    public void reiniciarJogo() {
        jogoIniciado = false;
        timer.stop();
        carroPrincipal.setLocation(largura / 2 - 20, altura - 120);
        carroAmarelo.setLocation(largura / 2 + 20, altura - 120);
        buracos = new Rectangle[2];
        carrosOponentes = new Rectangle[2];
        botaoReiniciar.setVisible(false);
        botaoIniciar.setVisible(true);
    }

    private void adicionarCarrosOponentes() {
        for (int i = 0; i < carrosOponentes.length; i++) {
            int x;
            do {
                x = (int) (Math.random() * (200 - 30)) + (largura / 2 - 100);
            } while (!posicaoValida(x, i));
            carrosOponentes[i] = new Rectangle(x, -100 * (i + 1), 30, 60);
        }
    }



    public void adicionarBuracos() {
        if (voltasAzul >= 1 || voltasAmarelo >= 1) {
            for (int i = 0; i < buracos.length; i++) {
                if (buracos[i] == null) {
                    int x = (int) (Math.random() * (200 - 30)) + (largura / 2 - 100);
                    int y = (int) (Math.random() * 500) + 100;
                    buracos[i] = new Rectangle(x, y, 30, 10);

                    int buracoIndex = i;
                    Timer buracoTimer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            buracos[buracoIndex] = null;
                        }
                    });
                    buracoTimer.setRepeats(false);
                    buracoTimer.start();
                }
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, largura, altura);
        g.drawImage(ruaImage, largura / 2 - 100, 0, 200, altura, null);
        g.drawImage(carroPrincipalImage, carroPrincipal.x, carroPrincipal.y, carroPrincipal.width, carroPrincipal.height, null);
        g.drawImage(carroAmareloImage, carroAmarelo.x, carroAmarelo.y, carroAmarelo.width, carroAmarelo.height, null);

        for (Rectangle carroOponente : carrosOponentes) {
            if (carroOponente != null) {
                g.drawImage(carroOponenteImage, carroOponente.x, carroOponente.y, carroOponente.width, carroOponente.height, null);
            }
        }

        for (Rectangle buraco : buracos) {
            if (buraco != null) {
                g.drawImage(buracoImage, buraco.x, buraco.y, buraco.width, buraco.height, null);
            }
        }

        g.setColor(Color.BLACK);
        g.drawString("Voltas Carro Azul: " + voltasAzul, 20, 20);
        g.drawString("Voltas Carro Amarelo: " + voltasAmarelo, 20, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (jogoIniciado) {
            moverCarros();
            moverCarrosOponentes();
            verificarColisoes();
            verificarVoltas();
            verificarVitoria();
            adicionarBuracos();
        }
        repaint();
    }

    private void moverCarros() {
        // Movimento automático para cima
        carroPrincipal.y -= velocidade;
        carroAmarelo.y -= velocidadeAmarelo;

        // Controles manuais
        if (moveUpAzul) carroPrincipal.y -= velocidade;
        if (moveDownAzul) carroPrincipal.y += velocidade;
        if (moveLeftAzul && carroPrincipal.x > largura / 2 - 75) carroPrincipal.x -= velocidade;
        if (moveRightAzul && carroPrincipal.x < largura / 2 + 50) carroPrincipal.x += velocidade;

        if (moveUpAmarelo) carroAmarelo.y -= velocidadeAmarelo;
        if (moveDownAmarelo) carroAmarelo.y += velocidadeAmarelo;
        if (moveLeftAmarelo && carroAmarelo.x > largura / 2 - 75) carroAmarelo.x -= velocidadeAmarelo;
        if (moveRightAmarelo && carroAmarelo.x < largura / 2 + 50) carroAmarelo.x += velocidadeAmarelo;

        // Aceleração
        if (acelerarAzul) velocidade += 0.1;
        if (acelerarAmarelo) velocidadeAmarelo += 0.1;

        // Verificar se os carros atingiram o topo da tela
        verificarVoltas();

        // Manter os carros dentro dos limites laterais da pista
        carroPrincipal.x = Math.max(largura / 2 - 75, Math.min(largura / 2 + 50, carroPrincipal.x));
        carroAmarelo.x = Math.max(largura / 2 - 75, Math.min(largura / 2 + 50, carroAmarelo.x));
    }

    private void moverCarrosOponentes() {
        for (Rectangle carroOponente : carrosOponentes) {
            if (carroOponente != null) {
                carroOponente.y += 2;
                if (carroOponente.y > altura) {
                    carroOponente.y = -60;
                    int x;
                    do {
                        x = (int) (Math.random() * (200 - 30)) + (largura / 2 - 100);
                    } while (!posicaoValida(x, -1));
                    carroOponente.x = x;
                }
            }
        }
    }



    private void verificarColisoes() {
        for (Rectangle buraco : buracos) {
            if (buraco != null) {
                if (carroPrincipal.intersects(buraco)) {
                    carroPrincipal.setLocation(largura / 2 - 20, altura - 120);
                    voltasAzul = 0;
                }
                if (carroAmarelo.intersects(buraco)) {
                    carroAmarelo.setLocation(largura / 2 + 20, altura - 120);
                    voltasAmarelo = 0;
                }
            }
        }

        for (Rectangle carroOponente : carrosOponentes) {
            if (carroOponente != null) {
                if (carroPrincipal.intersects(carroOponente)) {
                    carroPrincipal.setLocation(largura / 2 - 20, altura - 120);
                    voltasAzul = 0;
                }
                if (carroAmarelo.intersects(carroOponente)) {
                    carroAmarelo.setLocation(largura / 2 + 20, altura - 120);
                    voltasAmarelo = 0;
                }
            }
        }

        if (carroPrincipal.intersects(carroAmarelo)) {
            int deltaX = carroPrincipal.x - carroAmarelo.x;
            int deltaY = carroPrincipal.y - carroAmarelo.y;
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    carroPrincipal.x = carroAmarelo.x + carroAmarelo.width;
                } else {
                    carroAmarelo.x = carroPrincipal.x + carroPrincipal.width;
                }
            } else {
                if (deltaY > 0) {
                    carroPrincipal.y = carroAmarelo.y + carroAmarelo.height;
                } else {
                    carroAmarelo.y = carroPrincipal.y + carroPrincipal.height;
                }
            }
        }
    }

    private void verificarVoltas() {
        if (carroPrincipal.y + carroPrincipal.height < 0) {
            voltasAzul++;
            carroPrincipal.y = altura;
        }
        if (carroAmarelo.y + carroAmarelo.height < 0) {
            voltasAmarelo++;
            carroAmarelo.y = altura;
        }
    }

    private void verificarVitoria() {
        if (voltasAzul > 3) {
            JOptionPane.showMessageDialog(this, "Carro Azul venceu!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
            reiniciarJogo();
        } else if (voltasAmarelo > 3) {
            JOptionPane.showMessageDialog(this, "Carro Amarelo venceu!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
            reiniciarJogo();
        }
    }

    private boolean posicaoValida(int x, int indice) {
        for (int i = 0; i < indice; i++) {
            if (carrosOponentes[i] != null && Math.abs(carrosOponentes[i].x - x) < 30) {
                return false;
            }
        }
        return true;
    }

    private void tocarMusica(String caminho) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(caminho));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Erro ao tocar música: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogo de Corrida");
        JogoCorrida jogo = new JogoCorrida();
        frame.add(jogo);
        frame.setSize(jogo.largura, jogo.altura);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        jogo.requestFocus();
    }
}