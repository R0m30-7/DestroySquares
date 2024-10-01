package ProgettiMiei.Java.DestroySquares;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import java.awt.MouseInfo;

/* 
 * Codici dei tasti del mouse:
 * 0 - Nessun tasto premuto
 * 1 - Tasto sinistro
 * 2 - Tasto rotella
 * 3 - Tasto destro
 */

public class MouseInput implements MouseInputListener, MouseWheelListener {

    protected static int xMouse = 0;
    protected static int yMouse = 0;

    @Override
    public void mousePressed(MouseEvent e) {
        // ? Considero il tasto sinistro
        if (e.getButton() == 1) {

        }

        //? Considero il tasto destro
        if(e.getButton() == 3){

        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rot = e.getWheelRotation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        xMouse = (int) MouseInfo.getPointerInfo().getLocation().getX() - Game.getxLoc();
        yMouse = (int) MouseInfo.getPointerInfo().getLocation().getY() - Game.getyLoc();
        System.out.println(xMouse + "   " + yMouse);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
