/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.awt.Image;

/**
 *
 * @author DAVID
 */
public class Carta {
    private char Valor;
    private char familia;
    private int PosX,PosY;
    private char Sucesor;
    private Image Imagen;
    
    /**
     * Realiza la conversión del valor númericos de la carta al valor en letras
     * 
     * @param getValorChar Valor númerico de la carta
     * @return Retorna el equivalente en letras
     */
    public char getValor() {
        return Valor;
    }

    public void setValor(char Valor) {
        this.Valor = Valor;
    }

    public char getFamilia() {
        return familia;
    }

    public void setFamilia(char familia) {
        this.familia = familia;
    }

    public int getPosX() {
        return PosX;
    }

    public void setPosX(int PosX) {
        this.PosX = PosX;
    }

    public int getPosY() {
        return PosY;
    }

    public void setPosY(int PosY) {
        this.PosY = PosY;
    }

    public char getSucesor() {
        switch (Valor)
        {
            case 'A' :
                Sucesor = '2';
                break;
            case '9':
                Sucesor = 'D';
                break;
            case 'D' :
                Sucesor = 'J';
                break;
            case 'J':
                Sucesor = 'Q';
                break;
            case 'Q':
                Sucesor = 'K';
                break;
            case 'K':
                Sucesor = 'Z';
                break;
            default : 
                Sucesor = (char) (((Valor - '0') + 1) + '0');
                break;
        }
        return Sucesor;
    }

    public Image getImagen() {
        return Imagen;
    }

    public void setImagen(Image Imagen) {
        this.Imagen = Imagen;
    }
    
    
    
}
