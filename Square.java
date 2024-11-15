package DestroySquares;

import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;

public class Square {
    private Random random = new Random();

    private double x, y;
    private int xGoal, yGoal;
    private double speed, velX, velY;
    private double maxVel = 100, minVel = 38;

    private int width, height;

    private double lifePoints, maxLifePoints;
    private double squareDamage;
    
    private boolean canDespawn = false;
    private boolean canDropLoot = false;

    public Square(){

        GenerateSpawn();
        GeneratePosGoal();
        setAttributes();
        
        /* System.out.println("Spawn: " + x + " || " + y);
        System.out.println("Vel: " + (float) speed + " || " + (float) velX + " || " + (float) velY);
        System.out.println("Theta: " + (float) Math.toDegrees(theta) + "\n"); */
    }

    protected void draw(Graphics g){
        g.setColor(new Color(255, 68, 25, 50));
        g.fillRect((int) x - width / 2, (int) y - height / 2, width, (int) (height * lifePoints / maxLifePoints));

        g.setColor(new Color(156, 8, 0));
        g.drawRect((int) x - width / 2, (int) y - height / 2, width, height);
        width = width - 2; height = height - 2;
        g.drawRect((int) x - width / 2, (int) y - height / 2, width, height);
        width = width + 2; height = height + 2;

        //TODO Fare la parte che rende i nemici "neon"
        //? L'idea è che disegno intorno al quadrato altri quadrati rossi con colori più accesi ma quasi del tutto trasparenti

        if(lifePoints <= 0){
            canDespawn = true;
            canDropLoot = true;
        }
    }

    protected void Move(Graphics g){
        x += velX / Game.getFPSGoal();
        y += velY / Game.getFPSGoal();

        CheckForDespawn();
    }

    private void setAttributes(){
        // Imposto le dimensioni
        width = 50;
        height = width;

        // Imposto la vita e il danno del quadrato quando viene hittato
        maxLifePoints = 100;
        lifePoints = maxLifePoints;
        squareDamage = 10;
        //lifePoints = random.nextInt(maxLifePoints) + 1;

        // Imposto le velocità
        speed = random.nextDouble() * (maxVel - minVel + 1) + minVel;
        double theta = Math.atan2(yGoal - y, xGoal - x);
        velX = speed * Math.cos(theta);
        velY = speed * Math.sin(theta);
    }

    private void DropLoot(Graphics g){  //TODO Droppare loot
        System.out.println("Droppo loot");
        GamePanel.Loot.add(new LootPiece(x, y));
    }

    private void CheckForDespawn(){
        if((int) x <= xGoal + width && (int) x >= xGoal - width){
            canDespawn = true;
        }
    }

    protected boolean HasToDespawn(Graphics g){
        if(canDespawn){
            if(x > GamePanel.panelWidth + width / 2 || x < -width / 2 || y > GamePanel.panelHeight + height / 2 || y < -height / 2){
                return true;
            } else if(canDropLoot){
                DropLoot(g);
                GamePanel.playerHealth -= DoDamage();
                if(GamePanel.playerHealth <= 0){
                    GamePanel.dead = true;
                }
                return true;
            }
        }
        return false;
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
        xGoal = random.nextInt(GamePanel.panelWidth);
        yGoal = random.nextInt(GamePanel.panelHeight);
    }

    protected void TakeDamage(double damage){
        lifePoints -= damage;
    }

    private double DoDamage(){
        return squareDamage;
    }

    protected double getX() {
        return x;
    }

    protected double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getLifePoints() {
        return lifePoints;
    }
}
