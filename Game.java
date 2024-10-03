package DestroySquares;

import java.awt.*;
import java.swing.*;

public class Game implements Runnable {

    protected GameWindow gameWindow;
    protected GamePanel gamePanel;
    protected Thread gameThread;
    protected final static int FPSGoal = 60;

    protected static int xLoc = 0; // ? Mi dice le coordinate del punto in
    protected static int yLoc = 0; // ? alto a sinistra del gamePanel

    public Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);

        gamePanel.requestFocus();
        gamePanel.addMouseMotionListener(new MouseInput()); // ? Aggiungo il mouse motion listener al gamePanel
        gamePanel.addMouseListener(new MouseInput()); // ? Aggiungo il mouse listener al gamePanel
        gamePanel.addMouseWheelListener(new MouseInput());  //? Aggiungo il wheel listener al gamePanel
        gamePanel.addKeyListener(new KeyboardInput());  //? Aggiungo il keyboard listener al gamePanel
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long now = System.nanoTime();
        long lastFrame = now;
        long lastCheck = now;
        double timePerFrame = 1000000000.0 / FPSGoal;

        while (true) {
            // FPS counter
            now = System.nanoTime();
            if (now - lastFrame > timePerFrame) {
                gamePanel.repaint();
                lastFrame = now;
            }

            //? Nascondo il cursore
            if(GamePanel.scena == 1){
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image immagine = toolkit.createImage("");
                Point hotspot = new Point(0, 0);
                Cursor cursoreTrasparente = toolkit.createCustomCursor(immagine, hotspot, "Cursore Trasparente");
                GameWindow.jFrame.setCursor(cursoreTrasparente);
            } else{
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image immagine = toolkit.getImage("cursor.png"); // Sostituisci con il percorso della tua immagine
                Point hotspot = new Point(0, 0);
                Cursor cursorePersonalizzato = toolkit.createCustomCursor(immagine, hotspot, "Cursore Personalizzato");
                GameWindow.jFrame.setCursor(cursorePersonalizzato);
            }

            if (now - lastCheck >= 1000000000) {
                lastCheck = now;
                // ! System.out.println("FPS: " + frames);  //!  togliere il commento se si vuole vedere il numero di FPS

                xLoc = (int) gamePanel.getLocationOnScreen().getX(); // ? Mi dice le coordinate del punto in alto a sinistra del gamePanel
                yLoc = (int) gamePanel.getLocationOnScreen().getY();
            }
        }
    }

    public static int getFPSGoal() {
        return FPSGoal;
    }

    public static int getxLoc() {
        return xLoc;
    }

    public static int getyLoc() {
        return yLoc;
    }
}
