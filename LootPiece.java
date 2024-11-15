package DestroySquares;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class LootPiece {
    double x;
    double y;
    Point goal = new Point(0, 0);
    double velX, velY;
    double minVel = 15;     // Velocità minima dalla cui si ferma
    double friction = 0.98; // Velocità con cui si ferma
    boolean picked = false;
    double velocity;

    protected BufferedImage pieceLoot;
    {   //? Carico l'immagine
        try {
            pieceLoot = ImageIO.read(new File("particle.png"));
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento dell'immagine: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public LootPiece(double x, double y){
        this.x = x;
        this.y = y;
        GenerateParameters();
    }

    protected void Update(Graphics g, int index){
        Draw(g);

        if(!picked){
            if(velX != 0 && velY != 0){
                Move();
            } else if(DistanzaFra(new Point((int) x, (int) y), new Point((int) GamePanel.mouseX, (int) GamePanel.mouseY)) <= Math.sqrt(Math.pow(GamePanel.mouseWidth, 2) + Math.pow(GamePanel.mouseHeight, 2)) / 2){
                picked = true;
                velocity = minVel * 2;    // Velocità con cui parte quando viene raccolto
            }
        }

        if(picked){
            GoTo(new Point((int) GamePanel.mouseX, (int) GamePanel.mouseY));

            if(DistanzaFra(new Point((int) GamePanel.mouseX, (int) GamePanel.mouseY), new Point((int) x, (int) y)) < 12){
                GamePanel.Loot.remove(index);
                // TODO Aumentare il counter del loot raccolto
            }
        }
    }

    private void Draw(Graphics g){
        g.drawImage(pieceLoot, (int) x, (int) y, null);
        //! g.drawString(x + " || " + y, (int) x, (int) y - 30);
        //! g.drawString(velX + " | " + velY, (int) x, (int) y - 40);
    }

    private void Move(){
        x += velX / Game.FPSGoal;
        y += velY / Game.FPSGoal;
        
        velX *= friction;
        velY *= friction;

        if(Math.abs(velY) < minVel && Math.abs(velX) < minVel){
            velX = 0;
            velY = 0;
            //! System.out.println("Porto a zero, velocità troppo piccole");
        }
    }

    private void GoTo(Point goal){
        double theta = Math.atan2(goal.getY() - y, goal.getX() - x);
        velocity *= 1.08;   // Velocità con cui aumenta la velocità quando viene raccolto
        
        velX = velocity * Math.cos(theta);
        velY = velocity * Math.sin(theta);

        x += velX / Game.FPSGoal;
        y += velY / Game.FPSGoal;
    }
    
    private void GenerateParameters(){
        Random random = new Random();
        int minRadius = 0;
        int maxRadius = 100 - minRadius;
        
        int rand1 = (int) x + random.nextInt(maxRadius) + minRadius - maxRadius / 2;
        int rand2 = (int) y + random.nextInt(maxRadius) + minRadius - maxRadius / 2;
        goal.setLocation(rand1, rand2);

        double theta = Math.atan2(goal.getY() - y, goal.getX() - x);
        double velocity = random.nextDouble() * 200;
        
        velX = velocity * Math.cos(theta);
        velY = velocity * Math.sin(theta);
    }

    private double DistanzaFra(Point a, Point b){
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }
}