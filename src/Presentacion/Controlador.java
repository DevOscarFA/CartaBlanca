/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author DAVID
 */
public class Controlador implements MouseListener, MouseMotionListener{

    private final Vista FormJuego;
    
    public Controlador(Vista miVista)
    {
        FormJuego = miVista; 
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        FormJuego.getModelo().AddCartasATemporal(e.getX(),e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       FormJuego.getModelo().DelCartasAEspacios(e.getX(),e.getY());  
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        FormJuego.getModelo().DesplazarCartasTeporales(e.getX(),e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
}
