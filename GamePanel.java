package DestroySquares;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    protected static List<LootPiece> Loot = new ArrayList<>();

    //? Variabili player
    protected static double playerMaxHealth = 100, playerHealth = playerMaxHealth;
    private static double playerDamage = 100;
    private static int mouseWidth = 100, mouseHeight = 100;
    protected static boolean dead = false;

    //? Font
    private Font originalFont;
    private String text;
    private int textSize;
    private Font font;
    private FontMetrics metrics;

    //? Variabili per il menu
    private static int menuWidth, menuHeight, menuSpaceBetweenRects;
    private static int xSpawnMenu, ySpawnMenu;

    //? Variabili per il menu pausa
    private static int pauseWidth, pauseHeight, pauseSpaceBetweenRects;
    private static int xSpawnPause, ySpawnPause;

    //? Variabili per il menu DeathScreen
    private static int deathWidth, deathHeight, DeathSpaceBetweenRects;
    private static int xSpawnDeath, ySpawnDeath;

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

            {   //? Aggiorno le variabili dei menu
                //? Variabili per il menu
                menuWidth = 360;
                menuHeight = 85;
                menuSpaceBetweenRects = 15;
                xSpawnMenu = panelWidth / 2 - menuWidth / 2;
                ySpawnMenu = panelHeight / 2 - menuHeight / 2;

                //? Variabili per il menu pausa
                pauseWidth = menuWidth;
                pauseHeight = menuHeight;
                pauseSpaceBetweenRects = menuSpaceBetweenRects;
                xSpawnPause = panelWidth / 2 - menuWidth / 2;
                ySpawnPause = panelHeight / 2 - menuHeight / 2;

                //? Variabili per il menu DeathScreen
                deathWidth = menuWidth;
                deathHeight = menuHeight;
                DeathSpaceBetweenRects = menuSpaceBetweenRects;
                xSpawnDeath = panelWidth / 2 - deathWidth / 2;
                ySpawnDeath = panelHeight / 2 - deathHeight / 2;
            }

            if(scena == 1){
                SpawnEnemies(g);
            }

            cicli = 0;
        }

        cicli++;
    }

    protected static void Hit(){
        boolean collides = false;

        if(scena == 0){ // Menu
            if(mouseX > xSpawnMenu && mouseX < xSpawnMenu + menuWidth){
                if(mouseY > ySpawnMenu - menuHeight - menuSpaceBetweenRects && mouseY < ySpawnMenu - menuSpaceBetweenRects){   //? Primo rettangolo
                    scena = 1;
                } else if(mouseY > ySpawnMenu && mouseY < ySpawnMenu + menuHeight){ //? Secondo rettangolo
                    scena = 3;
                } else if(mouseY > ySpawnMenu + menuHeight + menuSpaceBetweenRects && mouseY < ySpawnMenu + 2 * menuHeight + menuSpaceBetweenRects){    //? Terzo rettangolo
                    GameWindow.jFrame.dispose();
                }
            }

        } else if(scena == 1){  // Momento gaming
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
        } else if(scena == 2){  // Pausa
            if(mouseX > xSpawnPause && mouseX < xSpawnPause + pauseWidth){
                if(mouseY > ySpawnPause - pauseHeight - pauseSpaceBetweenRects && mouseY < ySpawnPause - pauseSpaceBetweenRects){   //? Primo rettangolo
                    scena = 1;
                    isPaused = false;
                } else if(mouseY > ySpawnPause && mouseY < ySpawnPause + pauseHeight){ //? Secondo rettangolo
                    scena = 0;
                    isPaused = false;
                } else if(mouseY > ySpawnPause + pauseHeight + pauseSpaceBetweenRects && mouseY < ySpawnPause + 2 * pauseHeight + pauseSpaceBetweenRects){    //? Terzo rettangolo
                    GameWindow.jFrame.dispose();
                }
            }
        } else if(scena == 3){  // Potenziamenti

        } else if(scena == 4){  // Morto
            if(mouseX > xSpawnDeath && mouseX < xSpawnDeath + deathWidth){
                if(mouseY > ySpawnDeath && mouseY < ySpawnDeath + deathHeight){   //? Primo rettangolo
                    scena = 0;
                    isPaused = false;
                } else if(mouseY > ySpawnDeath + deathHeight + DeathSpaceBetweenRects && mouseY < ySpawnDeath + 2 * deathHeight + DeathSpaceBetweenRects){ //? Secondo rettangolo
                    GameWindow.jFrame.dispose();
                }
            }
        }
        
    }

    private void InizializeAttributes(){    // Imposto ad originali tutte le variabili quando inizio a giocare
        playerHealth = playerMaxHealth;
        dead = false;
    }

    private void DrawMenu(Graphics g){
        //? Tre pulsanti "Play", "Upgrades", "Quit"

        textSize = 60;
        font = new Font("Arial", Font.PLAIN, textSize);
        metrics = g.getFontMetrics(font);
        g.setFont(font);

        {   // Disegno i rettangoli
        g.setColor(Color.DARK_GRAY);
        g.fillRect(xSpawnMenu, ySpawnMenu - menuHeight - menuSpaceBetweenRects, menuWidth, menuHeight);
        g.fillRect(xSpawnMenu, ySpawnMenu, menuWidth, menuHeight);
        g.fillRect(xSpawnMenu, ySpawnMenu + menuHeight + menuSpaceBetweenRects, menuWidth, menuHeight);
        }

        {   // Disegno le scritte
        g.setColor(Color.WHITE);
        text = "Play";
        g.drawString(text, xSpawnMenu + menuWidth / 2 - metrics.stringWidth(text) / 2, ySpawnMenu - menuHeight / 2 - menuSpaceBetweenRects + metrics.getHeight() / 3);
        text = "Upgrades";
        g.drawString(text, xSpawnMenu + menuWidth / 2 - metrics.stringWidth(text) / 2, ySpawnMenu + menuHeight / 2 + metrics.getHeight() / 3);
        text = "Quit";
        g.drawString(text, xSpawnMenu + menuWidth / 2 - metrics.stringWidth(text) / 2, ySpawnMenu + menuHeight * 3 / 2 + menuSpaceBetweenRects + metrics.getHeight() / 3);
        }
    }

    private void DrawGame(Graphics g){
        DrawEnemies(g);
        DrawMouseRectangle(g);
        MoveEnemies(g);
        DrawPlayerLife(g);
    }

    private void DrawPause(Graphics g){
        //? Tre semplici pulsanti con scritto "Resume", "Quit to title", "Quit Game"

        DrawEnemies(g);
        DrawPlayerLife(g);

        textSize = 60;
        font = new Font("Arial", Font.PLAIN, textSize);
        metrics = g.getFontMetrics(font);
        g.setFont(font);

        {   // Disegno i rettangoli
        g.setColor(Color.DARK_GRAY);
        g.fillRect(xSpawnPause, ySpawnPause - pauseHeight - pauseSpaceBetweenRects, pauseWidth, pauseHeight);
        g.fillRect(xSpawnPause, ySpawnPause, pauseWidth, pauseHeight);
        g.fillRect(xSpawnPause, ySpawnPause + pauseHeight + pauseSpaceBetweenRects, pauseWidth, pauseHeight);
        }

        {   // Disegno le scritte
        g.setColor(Color.WHITE);
        text = "Resume";
        g.drawString(text, xSpawnMenu + menuWidth / 2 - metrics.stringWidth(text) / 2, ySpawnMenu - menuHeight / 2 - menuSpaceBetweenRects + metrics.getHeight() / 3);
        text = "Quit to title";
        g.drawString(text, xSpawnMenu + menuWidth / 2 - metrics.stringWidth(text) / 2, ySpawnMenu + menuHeight / 2 + metrics.getHeight() / 3);
        text = "Quit Game";
        g.drawString(text, xSpawnMenu + menuWidth / 2 - metrics.stringWidth(text) / 2, ySpawnMenu + menuHeight * 3 / 2 + menuSpaceBetweenRects + metrics.getHeight() / 3);
        }
    }

    private void DrawUpgrades(Graphics g){

    }

    private void DrawDeathScreen(Graphics g){
        {   //? Disegno la scritta "Sei Morto!"
        textSize = 48;
        font = new Font("Arial", Font.BOLD, textSize);
        metrics = g.getFontMetrics(font);
        g.setFont(font);

        text = "Sei Morto!";
        g.setColor(Color.RED);
        g.drawString(text, xSpawnDeath + deathWidth / 2 - metrics.stringWidth(text) / 2, ySpawnDeath - deathHeight / 2 - DeathSpaceBetweenRects + metrics.getHeight() / 3);
        }

        textSize = 60;
        font = new Font("Arial", Font.PLAIN, textSize);
        metrics = g.getFontMetrics(font);
        g.setFont(font);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(xSpawnDeath, ySpawnDeath, deathWidth, deathHeight);
        g.fillRect(xSpawnDeath, ySpawnDeath + deathHeight + DeathSpaceBetweenRects, deathWidth, deathHeight);

        g.setColor(Color.WHITE);
        text = "Quit to title";
        g.drawString(text, xSpawnDeath + deathWidth / 2 - metrics.stringWidth(text) / 2, ySpawnDeath + deathHeight / 2 + metrics.getHeight() / 3);
        text = "Quit Game";
        g.drawString(text, xSpawnDeath + deathWidth / 2 - metrics.stringWidth(text) / 2, ySpawnDeath + deathHeight * 3 / 2 + DeathSpaceBetweenRects + metrics.getHeight() / 3);
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
        DrawParticleLoot(g);
    }

    private void DrawParticleLoot(Graphics g){
        for(int i = 0; i < Loot.size(); i++){
            Loot.get(i).Draw(g);
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