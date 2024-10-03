package DestroySquares;

import java.util.Random;

public class Square {
    Graphics g;
    Random random = new Random();

    int x, y;
    int xGoal, yGoal;
    int width, height;
    int lifePoints;
    double speed;
    double maxVel = 100, minVel = 10;

    public Square(Graphics g){

        this.speed = random.nextDouble(maxVel - minVel + 1) + minVel;
        GenerateSpawn(this);
        GeneratePosGoal(this);
    }

    protected void draw(Graphics g){
        g.setColor(new Color(255, 68, 25, 50));
        g.fillRect(x - width / 2, y - height / 2, width, height);
        g.setColor(new Color(156, 8, 0));
        g.drawRect(x - width / 2, y - height / 2, width, height);
        width = width - 2;
        height = height - 2;
        g.drawRect(x - width / 2, y - height / 2, width, height);
    }

    protected void Move(){
        if(x < xGoal){
            x += speed / Game.getFPSGoal();
        } else{
            x -= speed / Game.getFPSGoal();
        }
        if(y < yGoal){
            y += speed / Game.getFPSGoal();
        } else{
            y -= speed / Game.getFPSGoal();
        }
    }

    private GenerateSpawn(Square a){
        int xMax = -width / 2, xMin = -(10 + width / 2);
        int yMax = -height / 2, yMin = -(10 + height / 2);
        a.x = random.nextInt(xMax - xMin + 1) + xMin;
        a.y = random.nextInt(yMax - yMin + 1) + yMin;
    }

    private GeneratePosGoal(Square a){
        a.xGoal = GamePanel.panelWidth / 2;
        a.yGoal = GamePanel.panelHeight / 2;
    }
}
