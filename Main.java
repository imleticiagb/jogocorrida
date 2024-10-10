import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogo de Corrida");
        JogoCorrida jogo = new JogoCorrida();

        frame.add(jogo);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        // Regras do jogo
        String regras = "Regras do Jogo:\n" +
                "1. Use as teclas W (cima) e S (baixo), A (esquerda) e D (direita) para mover o carro amarelo.\n" +
                "2. Use as setas para mover o carro azul.\n" +
                "3. Buracos reiniciam as voltas do carro que colidir.\n" +
                "4. O primeiro carro a completar 3 voltas é o vencedor.\n" +
                "5. Pressione C para acelerar o carro azul e F para acelerar o carro amarelo.\n" +
                "6. O jogo termina quando um carro completa 3 voltas.\n";
        JOptionPane.showMessageDialog(frame, regras, "Instruções", JOptionPane.INFORMATION_MESSAGE);
    }
}
