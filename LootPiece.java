import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class LootPiece {
    double x;
    double y;

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
    }

    protected draw(Graphics g){
        g.drawImage(pieceLoot, (int) x, (int) y, null);
    }
}