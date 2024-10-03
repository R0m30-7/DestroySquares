package ProgettiMiei.Java.DestroySquares;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

    protected static int panelWidth = 1300; // ?Larghezza schermo
    protected static int panelHeight = 630; // ?Altezza schermo
    protected static int mouseX = 0;
    protected static int mouseY = 0;
    protected static int FPSToDisplay = 0;  //? Visualizzo gli FPS a schermo
    private static long lastSec = System.nanoTime();    //? Salva il secondo precedente
    private int cicli = 0;
    protected static boolean isPaused = false;

    protected static int caso = 1;
    /*
     * Caso = 0: Menù iniziale
     * Caso = 1: Momento gaming
     * Caso = 2: Pausa
     * Caso = 4: Potenziamenti
    */

    public GamePanel() {
        setPanelSize();

        this.setBackground(Color.DARK_GRAY);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(panelWidth, panelHeight);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) { // Scrivo in questo void le cose che voglio disegnare
        super.paintComponent(g);
        
        mouseX = (int) MouseInfo.getPointerInfo().getLocation().getX() - Game.getxLoc();
        mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY() - Game.getyLoc();

        //? Disegno le scritte
        WriteTextOnScreen(g);
        
        switch (caso) {
            case 0: // Menù iniziale
                DrawMenu(g);
                break;
            case 1: // Momento gaming
                DrawGame(g);
                break;
            case 2: // Pausa
                DrawPause(g);
                break;
            case 3: // Potenziamenti
                DrawUpgrades(g);
                break;
        }

        if (System.nanoTime() - lastSec > 1000000000) { // ? Entro in questo if una volta al secondo
            lastSec = System.nanoTime();
            FPSToDisplay = cicli;

            panelWidth = (int) GameWindow.getjFrameSize().getWidth() - 16;
            panelHeight = (int) GameWindow.getjFrameSize().getHeight() - 39;

            cicli = 0;
        }

        cicli++;
    }

    private void DrawMenu(Graphics g){
        //? Tre pulsanti "Play", "Upgrades", "Quit Game"
    }

    private void DrawGame(Graphics g){
        DrawMouseRectangle(g);
    }

    private void DrawPause(Graphics g){
        int width = 100, height = 50;
        //? Tre semplici pulsanti con scritto "Resume", "Quit to title", "Quit Game"
        g.setColor(Color.GRAY);
        g.fillRect(panelWidth / 2 - width / 2, panelHeight / 2 - height / 2, width, height);
    }

    private void DrawUpgrades(Graphics g){

    }

    private void DrawMouseRectangle(Graphics g){
        int width = 100, height = 100;

        g.setColor(new Color(122, 226, 255));
        g.fillRect(mouseX - width / 2, mouseY - height / 2, width, height);
        g.setColor(new Color(0, 98, 255));
        g.drawRect(mouseX - width / 2, mouseY - height / 2, width, height);
        width = width - 2;
        height = height - 2;
        g.drawRect(mouseX - width / 2, mouseY - height / 2, width, height);
    }

    public static void setPaused(boolean isPaused) {
        GamePanel.isPaused = isPaused;
    }

    private void WriteTextOnScreen(Graphics g){
        g.setColor(Color.WHITE);
        g.drawString("Dimension: " + panelWidth + " x " + panelHeight, 3, 15);
        g.drawString("FPS: " + FPSToDisplay, panelWidth - 45, 15);
    }
}