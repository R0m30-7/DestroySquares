package DestroySquares;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    private int scenaPrec = 10;

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("PREMO UN TASTO");
        //? Se viene premuto il tasto ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            //System.out.println("PRONTI");
            if(!GamePanel.isPaused){
                scenaPrec = GamePanel.scena;
                GamePanel.scena = 2;
            } else{
                GamePanel.scena = scenaPrec;
            }
            GamePanel.setPaused(!GamePanel.isPaused);
        }

        //! Debug, cambio scena con i numeri da tastiera
        switch (e.getKeyCode()) {
            case 49:
                GamePanel.scena = 0;
                break;
            case 50:
                GamePanel.scena = 1;
                break;
            case 51:
                GamePanel.scena = 2;
                break;
            case 52:
                GamePanel.scena = 3;
                break;
            case 53:
                GamePanel.scena = 4;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
