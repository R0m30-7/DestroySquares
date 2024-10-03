package ProgettiMiei.Java.DestroySquares;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GameWindow {
    protected static JFrame jFrame;

    public GameWindow(GamePanel gamePanel) {
        jFrame = new JFrame();

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Quando chiudo la finestra interrompe il programma
        jFrame.add(gamePanel);
        jFrame.setResizable(true);

        jFrame.pack();
        jFrame.setVisible(true);
    }

    public static Dimension getjFrameSize(){
        return jFrame.getSize();
    }
}
