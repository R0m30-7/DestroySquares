package ProgettiMiei.Java.DestroySquares;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //? Se viene premuto lo spazio
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if(!Game.getisPaused()){
                Game.setPaused(true);
            } else {
                Game.setPaused(false);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
