/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import Logica.Baraja;
import Logica.Carta;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author DAVID
 */
public class Modelo implements Runnable {

    private Baraja barajaJuego;
    private BufferedImage dobleBuffer;
    private Graphics lapizInvisible, lapiz;
    private Vista VentanaJuego;
    private Thread hiloDibujo;
    private int OrigenTablero;
    private int OrigenZonaLibre;
    private int OrigenZonaFinal;
    private String Origen;

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String Origen) {
        this.Origen = Origen;
    }

    public int getOrigenZonaLibre() {
        return OrigenZonaLibre;
    }

    public void setOrigenZonaLibre(int OrigenZonaLibre) {
        this.OrigenZonaLibre = OrigenZonaLibre;
    }

    public int getOrigenZonaFinal() {
        return OrigenZonaFinal;
    }

    public void setOrigenZonaFinal(int OrigenZonaFinal) {
        this.OrigenZonaFinal = OrigenZonaFinal;
    }

    public int getOrigenTablero() {
        return OrigenTablero;
    }

    public void setOrigenTablero(int OrigenTablero) {
        this.OrigenTablero = OrigenTablero;
    }

    public Modelo() {
        hiloDibujo = new Thread(this);
        OrigenTablero = -1;
        OrigenZonaLibre = -1;
        OrigenZonaFinal = -1;
        Origen = "Vacio";
    }

    public Baraja getBaraja() {
        if (barajaJuego == null) {
            barajaJuego = new Baraja();
        }
        return barajaJuego;
    }

    public Vista getVentanaJuego() {
        if (VentanaJuego == null) {
            VentanaJuego = new Vista(this);
        }
        return VentanaJuego;
    }

    public void iniciar() {
        Canvas lienzo = getVentanaJuego().getLienzo();
        dobleBuffer = new BufferedImage(lienzo.getWidth(), lienzo.getHeight(), BufferedImage.TYPE_INT_ARGB);
        try {
            dobleBuffer = new BufferedImage(2000, 1000, BufferedImage.TYPE_INT_ARGB);
            lapiz = lienzo.getGraphics();
            lapizInvisible = dobleBuffer.getGraphics();
            getBaraja().generarBaraja();
            getBaraja().generarZonaLibre();
            getBaraja().generarZonaFinal();
            getVentanaJuego().setVisible(true);
        } catch (Exception ex) {
            System.err.println("Error " + ex.getMessage());
        }
        hiloDibujo.start();
    }

    public void Dibujar() {
        //Dibujando el Tablero Principal
        lapizInvisible.setColor(new Color(70, 130, 180));
        lapizInvisible.fillRect(0, 0, getVentanaJuego().getLienzo().getWidth(), getVentanaJuego().getLienzo().getHeight());
        //Generacion Zona Libre
        for (int j = 0; j < 4; j++) {
            ArrayList<Carta> Lista = getBaraja().getZonaLibre().get(j);
            for (int i = 0; i < Lista.size(); i++) {
                Carta CartaTemporal = Lista.get(i);
                lapizInvisible.drawImage(CartaTemporal.getImagen(), CartaTemporal.getPosX(), CartaTemporal.getPosY(),
                        getVentanaJuego().getLienzo());
            }
        }

        //Generacion Zona Final
        for (int i = 0; i < 4; i++) {//< getBaraja().getZonaFinal().size(); i++) {
            ArrayList<Carta> Lista = getBaraja().getZonaFinal().get(i);
            for (int k = 0; k < Lista.size(); k++) {
                Carta CartaTemporal = Lista.get(k);//getBaraja().getZonaFinal().get(i);
                lapizInvisible.drawImage(CartaTemporal.getImagen(), CartaTemporal.getPosX(), CartaTemporal.getPosY(),
                        getVentanaJuego().getLienzo());
            }
        }
        //
        for (int j = 0; j < 8; j++) {
            ArrayList<Carta> Lista = getBaraja().getBaraja().get(j);
            for (int i = 0; i < Lista.size(); i++) {
                lapizInvisible.drawImage(Lista.get(i).getImagen(), Lista.get(i).getPosX(), Lista.get(i).getPosY(),
                        getVentanaJuego().getLienzo());
            }
        }
        //Dibujando la Baraja Temporal
        for (int i = 0; i < getBaraja().getEspacioTemporal().size(); i++) {
            Carta CartaTemporal = getBaraja().getEspacioTemporal().get(i);
            lapizInvisible.drawImage(CartaTemporal.getImagen(), CartaTemporal.getPosX(), CartaTemporal.getPosY(),
                    getVentanaJuego().getLienzo());
        }
        lapiz.drawImage(dobleBuffer, 0, 0, getVentanaJuego().getLienzo());
    }

    public void DelCartasAEspacios(int x, int y) {
        if ((0 < x) && (y > 208)) {
            DelCartasTemporales(x, y);
        } else if ((650 < x) && (x < 1170) && (50 < y) && (y < 208)) {
            addCartaZonaFinal(x, y);
        } else if ((650 > x) && (0 < y) && (y < 208)) {
            addCartaZonaLibre(x, y);
        }
    }

    public void DelCartasTemporales(int x, int y) {
        int Columna = BuscarColumna(x, y);
        if ((Columna >= 0) && (Columna <= 7)) {
            if (getBaraja().validaMovimiento(Columna)) {
                getBaraja().LiberarEspacioTemporal(Columna);
            } else {
                DevolverMovimiento();
            }
        } else {
            DevolverMovimiento();
        }
    }

    public void AddCartasATemporal(int x, int y) {
        if ((0 < x) && (y > 208)) {
            AddCartasTemporales(x, y);
        } else if ((650 < x) && (x < 1170) && (50 < y) && (y < 208)) {
            AddCartaTemporalZonaFinal(x, y);
        } else if ((650 > x) && (0 < y) && (y < 208)) {
            AddCartaTemporalZonaLibre(x, y);
        }
    }

    public void AddCartasTemporales(int x, int y) {
        int Columna = BuscarColumna(x, y);
        int nCartas = 0;
        if ((Columna >= 0) && (Columna <= 7)) {
            setOrigen("Tablero");
            setOrigenTablero(Columna);
            nCartas = BuscarNCartas(Columna, y);
            if (nCartas > 0) {
                getBaraja().setEspacioTemporal(Columna, nCartas);
                if (!getBaraja().EvaluarContEspTemp()) {
                    getBaraja().LiberarEspacioTemporal(Columna);
                }
            }
        } else {
            setOrigenTablero(-1);
        }
    }

    public void AddCartaTemporalZonaLibre(int x, int y) {
        int Zona = BuscarPosicionZona(x, y);
        if ((Zona >= 0) && (Zona <= 3)) {
            setOrigenZonaLibre(Zona);
            setOrigen("ZonaLibre");
            getBaraja().setEspacioTemporalZonaLibre(Zona);
        } else {
            setOrigenZonaLibre(-1);
        }
    }

    public void AddCartaTemporalZonaFinal(int x, int y) {
        int Zona = BuscarPosicionZonaF(x, y);
        if ((Zona >= 0) && (Zona <= 3)) {
            setOrigen("ZonaFinal");
            setOrigenZonaFinal(Zona);
            getBaraja().setEspacioTemporalZonaFinal(Zona);
        } else {
            setOrigenZonaFinal(-1);
        }
    }

    public void DesplazarCartasTeporales(int x, int y) {
        for (int i = 0; i < getBaraja().getEspacioTemporal().size(); i++) {
            getBaraja().getEspacioTemporal().get(i).setPosX(x);
            getBaraja().getEspacioTemporal().get(i).setPosY(y + (i * 40));
        }
    }

    public int BuscarColumna(int x, int y) {
        Carta miCarta;
        int res = -1;
        for (int i = 0; i < 8; i++) {
            miCarta = getBaraja().getBaraja().get(i).get(0);
            if ((miCarta.getPosX() <= x) && (x <= (miCarta.getPosX() + 113)) && (y > 280)) {
                res = i;
            }
        }
        return res;
    }

    public int BuscarNCartas(int index, int y) {
        Carta miCarta;
        int res = -1;
        ArrayList<Carta> Seccion = getBaraja().getBaraja().get(index);
        miCarta = Seccion.get(0);
        for (int i = 1; i < Seccion.size(); i++) {
            miCarta = Seccion.get(i);
            if ((miCarta.getPosY() <= y) && (y <= miCarta.getPosY() + 40)) {
                res = Seccion.size() - i;
            }
        }
        if ((res == -1) && (y <= miCarta.getPosY() + 158)) {
            res = 1;
        }
        return res;
    }

    public void addCartaZonaLibre(int x, int y) {
        int Zona = BuscarPosicionZona(x, y);
        if ((Zona >= 0) && (Zona <= 3)) {
            if (getBaraja().ValidarZonaLibre(Zona)) {
                getBaraja().setZonaLibre(Zona);
            } else {
                DevolverMovimiento();
            }
        } else {
            DevolverMovimiento();
        }

    }

    //Mala evaluacion en setZonaFinal, al procesar el parametro. Hace una doble Evaluacion
    public void addCartaZonaFinal(int x, int y) {
        int Zona = BuscarPosicionZonaF(x, y);
        System.err.println("Zona F " + Zona);
        if ((Zona >= 0) && (Zona <= 3)) {
            if (getBaraja().ValidarZonaFinal(Zona)) {
                getBaraja().setZonaFinal(Zona);
            } else {
                DevolverMovimiento();
            }
        } else {
            DevolverMovimiento();
        }
    }

    public void DevolverMovimiento() {
        if (getOrigen().equals("Tablero") && getOrigenZonaLibre() == -1 && getOrigenZonaFinal() == -1) {
            getBaraja().LiberarEspacioTemporal(getOrigenTablero());
        } else if (getOrigen().equals("ZonaLibre") && getOrigenTablero() == -1 && getOrigenZonaFinal() == -1) {
            getBaraja().LiberarEspacioTemporalZonaLibre(getOrigenZonaLibre());
        } else if (getOrigen().equals("ZonaFinal") && getOrigenTablero() == -1 && getOrigenZonaLibre() == -1) {
            getBaraja().LiberarEspacioTemporalZonaFinal(getOrigenZonaFinal());
        }
    }

    public int BuscarPosicionZona(int x, int y) {
        Carta miCarta;
        int res = -1;
        for (int i = 0; i < 4; i++) {
            miCarta = getBaraja().getZonaLibre().get(i).get(0);
            if ((miCarta.getPosX() <= x) && (x <= (miCarta.getPosX() + 113))
                    && (miCarta.getPosY() <= y) && (y <= (miCarta.getPosY() + 158))) {
                res = i;
            }
        }
        return res;
    }

    public int BuscarPosicionZonaF(int x, int y) {
        Carta miCarta;
        int res = -1;
        for (int i = 0; i < 4; i++) {
            miCarta = getBaraja().getZonaFinal().get(i).get(0);
            if ((miCarta.getPosX() <= x) && (x <= (miCarta.getPosX() + 113))
                    && (miCarta.getPosY() <= y) && (y <= (miCarta.getPosY() + 158))) {
                res = i;
            }
        }
        return res;
    }

    public void Coordenadas(int x, int y) {
        getBaraja().setPosX(x);
        getBaraja().setPosY(y);
    }

    @Override
    public void run() {
        while (true) {
            Dibujar();
        }
    }
}
