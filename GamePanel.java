package DestroySquares;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.List;

import java.awt.FontMetrics;
import java.awt.Font;

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

    protected static int scena = 0;
    /*
     * Caso = 0: Menù iniziale
     * Caso = 1: Momento gaming
     * Caso = 2: Pausa
     * Caso = 4: Potenziamenti
    */

    private static List<Square> Enemies = new ArrayList<>();

    //? Variabili player
    protected static double playerMaxHealth = 100, playerHealth = playerMaxHealth;
    private static double playerDamage = 20;
    private static int mouseWidth = 100, mouseHeight = 100;
    protected static boolean dead = false;

    //? Font
    private Font originalFont;
    private String text;
    private int textSize;
    private Font font;
    private FontMetrics metrics;

    public GamePanel() {
        setPanelSize();

        this.setBackground(new Color(23, 23, 23));
    }

    private void setPanelSize() {
        Dimension size = new Dimension(panelWidth, panelHeight);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) { // Scrivo in questo void le cose che voglio disegnare
        originalFont = g.getFont();
        super.paintComponent(g);
        
        mouseX = (int) MouseInfo.getPointerInfo().getLocation().getX() - Game.getxLoc();
        mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY() - Game.getyLoc();

        //? Disegno le scritte
        
        switch (scena) {
            case 0: // Menù iniziale
                DrawMenu(g);
                Enemies.clear();;    //! Debug
                InizializeAttributes();
            break;
            case 1: // Momento gaming
                DrawGame(g);
                if(dead){
                    scena = 4;
                }
            break;
            case 2: // Pausa
                DrawPause(g);
            break;
            case 3: // Potenziamenti
                DrawUpgrades(g);
            break;
            case 4: // Morto
                DrawDeathScreen(g);
            break;
        }
        
        WriteTextOnScreen(g);

        if (System.nanoTime() - lastSec > 1000000000) { // ? Entro in questo if una volta al secondo
            lastSec = System.nanoTime();
            FPSToDisplay = cicli;

            panelWidth = (int) GameWindow.getjFrameSize().getWidth() - 16;
            panelHeight = (int) GameWindow.getjFrameSize().getHeight() - 39;

            if(scena == 1){
                SpawnEnemies(g);
            }

            cicli = 0;
        }

        cicli++;
    }

    protected static void Hit(){
        boolean collides = false;
        Square enemy;

        for(int i = 0; i < Enemies.size(); i++){
            enemy = Enemies.get(i);
            //? Devo controllare che il nemico sia dentro il quadrato del mouse, anche parzialmente

            // Coordinate del quadrato enemy
            double enemyLeft = enemy.getX() - enemy.getWidth() / 2;
            double enemyRight = enemyLeft + enemy.getWidth();
            double enemyTop = enemy.getY() - enemy.getHeight() / 2;
            double enemyBottom = enemyTop + enemy.getHeight();

            // Coordinate del quadrato mouse
            double mouseLeft = mouseX - mouseWidth / 2;
            double mouseRight = mouseX + mouseWidth / 2;
            double mouseTop = mouseY - mouseHeight / 2;
            double mouseBottom = mouseY + mouseHeight / 2;

            // Verifica collisione
            collides = enemyRight >= mouseLeft && enemyLeft <= mouseRight && enemyBottom >= mouseTop && enemyTop <= mouseBottom;

            if(collides){
                enemy.TakeDamage(playerDamage);
            }
        }
    }

    private void InizializeAttributes(){    // Imposto ad originali tutte le variabili quando inizio a giocare
        playerHealth = playerMaxHealth;
        dead = false;
    }

    private void DrawMenu(Graphics g){
        //? Tre pulsanti "Play", "Upgrades", "Quit Game"
    }

    private void DrawGame(Graphics g){
        DrawEnemies(g);
        DrawMouseRectangle(g);
        MoveEnemies(g);
        DrawPlayerLife(g);
    }

    private void DrawPause(Graphics g){
        int width = 100, height = 50;
        //? Tre semplici pulsanti con scritto "Resume", "Quit to title", "Quit Game"

        DrawEnemies(g);
        DrawPlayerLife(g);

        g.setColor(Color.GRAY);
        g.fillRect(panelWidth / 2 - width / 2, panelHeight / 2 - height / 2, width, height);
    }

    private void DrawUpgrades(Graphics g){

    }

    private void DrawDeathScreen(Graphics g){
        textSize = 48;
        font = new Font("Arial", Font.BOLD, textSize);
        metrics = g.getFontMetrics(font);
        g.setFont(font);

        text = "Sei Morto!";
        g.setColor(Color.RED);
        g.drawString(text, panelWidth / 2 - metrics.stringWidth(text) / 2, panelHeight / 2 - textSize / 2);
    }

    private void SpawnEnemies(Graphics g){
        Enemies.add(new Square());
    }

    private void DrawEnemies(Graphics g){
        for(int i = 0; i < Enemies.size(); i++){
            if(Enemies.get(i).HasToDespawn(g)){
                Enemies.remove(i);
            } else{
                Enemies.get(i).draw(g);
            }
        }
    }

    private void MoveEnemies(Graphics g){
        for(int i = 0; i < Enemies.size(); i++){
            Enemies.get(i).Move(g);
        }
    }

    private void DrawPlayerLife(Graphics g){
        int xStart = 15;
        int yStart = 15;
        int width = 150, height = 30;
        textSize = 18;
        font = new Font("Arial", Font.BOLD, textSize);
        metrics = g.getFontMetrics(font);
        
        g.setFont(font);
        g.setColor(Color.RED);
        g.fillRect(xStart, yStart, (int) (width * playerHealth / playerMaxHealth), height);
        g.setColor(new Color(55, 0, 0));
        g.drawRect(xStart, yStart, width, height);
        g.setColor(Color.WHITE);
        text = playerHealth + "/" + playerMaxHealth;
        g.drawString(text, xStart + (width / 2) - metrics.stringWidth(text) / 2, (int) (2.5 * yStart));
    }

    private void DrawMouseRectangle(Graphics g){

        g.setColor(new Color(122, 226, 255, 50));
        g.fillRect(mouseX - mouseWidth / 2, mouseY - mouseHeight / 2, mouseWidth, mouseHeight);
        g.setColor(new Color(0, 98, 255));
        g.drawRect(mouseX - mouseWidth / 2, mouseY - mouseHeight / 2, mouseWidth, mouseHeight);
        mouseWidth = mouseWidth - 2;
        mouseHeight = mouseHeight - 2;
        g.drawRect(mouseX - mouseWidth / 2, mouseY - mouseHeight / 2, mouseWidth, mouseHeight);
        g.setColor(Color.WHITE);
        g.drawRect(mouseX - 4, mouseY - 4, 8, 8);
        g.fillRect(mouseX - 1, mouseY - 1, 3, 3);
        mouseWidth = mouseWidth + 2;
        mouseHeight = mouseHeight + 2;
    }

    public static void setPaused(boolean isPaused) {
        GamePanel.isPaused = isPaused;
    }

    private void WriteTextOnScreen(Graphics g){
        g.setFont(originalFont);
        metrics = g.getFontMetrics(originalFont);

        g.setColor(Color.WHITE);
        text = "Dimension: " + panelWidth + " x " + panelHeight;
        g.drawString(text, 3, 60);
        text = "Enemies on screen: " + Enemies.size();
        g.drawString(text, 3, 75);

        text = "FPS: " + FPSToDisplay;
        g.drawString(text, panelWidth - metrics.stringWidth(text), 15);
        text = "Scena: " + scena;
        g.drawString(text, panelWidth - metrics.stringWidth(text), 30);
    }
}