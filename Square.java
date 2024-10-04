package DestroySquares;

import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;

public class Square {
    Graphics g;
    Random random = new Random();

    int x, y;
    int xGoal, yGoal;
    int width = 50, height = 50;
    int lifePoints;
    double speed, velX, velY;
    double maxVel = 100, minVel = 10;

    public Square(Graphics g){

        this.speed = random.nextDouble(maxVel - minVel + 1) + minVel;
        GenerateSpawn();
        GeneratePosGoal();

        //! Cambiare il calcolo dell'angolo perché non funziona
        double theta = Math.atan2(yGoal - y, xGoal - x);
        velX = speed * Math.cos(theta);
        velY = speed * Math.sin(theta);
        
        System.out.println("Spawn: " + x + " || " + y);
        System.out.println("Vel: " + (float) speed + " || " + (float) velX + " || " + (float) velY);
        System.out.println("Theta: " + (float) Math.toDegrees(theta) + "\n");
    }

    protected void draw(Graphics g){
        g.setColor(new Color(255, 68, 25, 50));
        g.fillRect(x - width / 2, y - height / 2, width, height);
        g.setColor(new Color(156, 8, 0));
        g.drawRect(x - width / 2, y - height / 2, width, height);
        width = width - 2;
        height = height - 2;
        g.drawRect(x - width / 2, y - height / 2, width, height);
        width = width + 2;
        height = height + 2;
    }

    protected void Move(Graphics g){
        x += velX / Game.getFPSGoal();
        y += velY / Game.getFPSGoal();

        g.setColor(Color.WHITE);
        g.drawString(x + " || " + y, x, y);
    }

    private void GenerateSpawn(){
        int bound = 20;

        if(random.nextBoolean()){
            if(random.nextBoolean()){
                //? x < 0
                x = - (random.nextInt(bound) + width / 2);
            } else{
                //? x > panelWidth
                x = random.nextInt(bound) + GamePanel.panelWidth + width / 2;
            }
            y = random.nextInt(GamePanel.panelHeight)  + height / 2;
        } else {
            if(random.nextBoolean()){
                //? y < 0
                y = - (random.nextInt(bound) + height / 2);
            } else{
                //? y > panelHeight
                y = random.nextInt(bound) + GamePanel.panelHeight + height / 2;
            }
            x = random.nextInt(GamePanel.panelWidth) + width / 2;
        }
    }

    private void GeneratePosGoal(){
        xGoal = GamePanel.panelWidth / 2;
        yGoal = GamePanel.panelHeight / 2;
    }
}
