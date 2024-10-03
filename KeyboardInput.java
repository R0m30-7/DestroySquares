package ProgettiMiei.Java.DestroySquares;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    private int casoPrec = 10;

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("PREMO UN TASTO");
        //? Se viene premuto il tasto ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.out.println("PRONTI");
            if(!GamePanel.isPaused){
                casoPrec = GamePanel.caso;
                GamePanel.caso = 2;
            } else{
                GamePanel.caso = casoPrec;
            }
            GamePanel.setPaused(!GamePanel.isPaused);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
