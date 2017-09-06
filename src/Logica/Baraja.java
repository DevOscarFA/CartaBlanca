/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.util.ArrayList;
import Logica.Carta;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author DAVID
 */
public class Baraja {

    //Posiciones de la Bajara Temporal.
    private int PosX, PosY;
    List[] ValCartas = {
        new ArrayList(Arrays.asList("AC", "2C", "3C", "4C", "5C", "6C", "7C", "8C", "9C", "DC", "JC", "QC", "KC",
        "AD", "2D", "3D", "4D", "5D", "6D", "7D", "8D", "9D", "DD", "JD", "QD", "KD",
        "AP", "2P", "3P", "4P", "5P", "6P", "7P", "8P", "9P", "DP", "JP", "QP", "KP",
        "AT", "2T", "3T", "4T", "5T", "6T", "7T", "8T", "9T", "DT", "JT", "QT", "KT"))
    };
    ArrayList<Carta> ListadoCartas;
    ArrayList<ArrayList<Carta>> Baraja;
    ArrayList<Carta> EspacioTemporal;
    ArrayList<ArrayList<Carta>> ZonaLibre;
    ArrayList<ArrayList<Carta>> ZonaFinal;

    public Baraja() {
        ListadoCartas = new ArrayList<Carta>();
        EspacioTemporal = new ArrayList<Carta>();
        Baraja = new ArrayList<ArrayList<Carta>>();
        ZonaLibre = new ArrayList<ArrayList<Carta>>();
        ZonaFinal = new ArrayList<ArrayList<Carta>>();
    }

    public void generarBaraja() {
        Carta nueva;
        int index;
        Random r = new Random();
        while (ValCartas[0].size() > 0) {
            try {
                index = (int) ((r.nextInt(ValCartas[0].size())));
                nueva = new Carta();
                nueva.setValor(ValCartas[0].get(index).toString().charAt(0));
                nueva.setFamilia(ValCartas[0].get(index).toString().charAt(1));
                nueva.setImagen(ImageIO.read(getClass().getResource("/Images/" + ValCartas[0].get(index).toString() + ".png")));
                ListadoCartas.add(nueva);
                ValCartas[0].remove(index);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        Barajar();
    }

    ///////////////////
    public void generarZonaLibre() {
        ArrayList<Carta> Linea;
        Carta miJocker;
        try {
            for (int i = 0; i < 4; i++) {
                Linea = new ArrayList<Carta>();
                miJocker = new Carta();
                miJocker.setFamilia('Y');
                miJocker.setImagen(ImageIO.read(getClass().getResource("/Images/" + "K1" + ".png")));
                miJocker.setPosX((i * 130) + 100);
                miJocker.setPosY(50);
                miJocker.setValor('0');
                Linea.add(miJocker);
                ZonaLibre.add(Linea);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void generarZonaFinal() {
        ArrayList<Carta> Linea;
        Carta miFinal;
        try {
            for (int i = 0; i < 4; i++) {
                Linea = new ArrayList<Carta>();
                miFinal = new Carta();
                miFinal.setFamilia('Y');
                miFinal.setImagen(ImageIO.read(getClass().getResource("/Images/" + "K1" + ".png")));
                miFinal.setPosX((i * 130) + 650);
                miFinal.setPosY(50);
                miFinal.setValor('0');
                Linea.add(miFinal);
                ZonaFinal.add(Linea);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    //////////////////
    public void Barajar() {
        ArrayList<Carta> Linea;
        Carta miCarta;
        int z = 100;
        //Generación de las 4 Pilas de 7
        for (int j = 0; j < 4; j++) {
            Linea = new ArrayList<Carta>();
            Linea.add(getCartaControl(z, 280));
            for (int i = 7; i < 14; i++) {
                miCarta = getListadoCartas().get(0);
                miCarta.setPosX(0 + z);
                miCarta.setPosY(i * 40);
                Linea.add(miCarta);
                getListadoCartas().remove(0);
            }
            Baraja.add(Linea);
            z = z + 130;
        }
        //Generacion de las 4 pilas de 6
        z = 50;
        for (int j = 0; j < 4; j++) {
            Linea = new ArrayList<Carta>();
            Linea.add(getCartaControl(600 + z, 280));
            for (int i = 7; i < 13; i++) {
                miCarta = getListadoCartas().get(0);
                miCarta.setPosX(600 + z);
                miCarta.setPosY(i * 40);
                Linea.add(miCarta);
                getListadoCartas().remove(0);
            }
            Baraja.add(Linea);
            z = z + 130;
        }
    }

    public void setEspacioTemporal(int index, int nCartas) {
        if (EspacioTemporal.isEmpty()) {
            ArrayList<Carta> aux;
            aux = Baraja.get(index);
            if (aux.size() > nCartas) {
                for (int i = aux.size() - nCartas; i < aux.size(); i++) {
                    EspacioTemporal.add(aux.get(i));
                }
                for (int i = 0; i < nCartas; i++) {
                    aux.remove(aux.size() - 1);
                }
            }
        }
    }

    public boolean EvaluarContEspTemp() {
        boolean res = true;
        Carta miCarta, Sig;
        if (!EspacioTemporal.isEmpty() && EspacioTemporal.size() == 1) {
            res = true;
        } else {
            for (int i = EspacioTemporal.size() - 1; i > 0; i--) {
                miCarta = EspacioTemporal.get(i);
                Sig = EspacioTemporal.get(i - 1);
                if (EvaluaSucesor(miCarta.getSucesor(), Sig.getValor())) {
                    if (!EvaluaColor(miCarta.getFamilia(), Sig.getFamilia())) {
                        res = false;
                        break;
                    }
                } else {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    //Funcion Auxiliar para ver la continuidad de las Cartas
    public boolean EvaluaColor(char Familia, char Opuesto) {
        if (Familia == Opuesto) {
            return false;
        } else if (Familia == 'C' && Opuesto != 'D') {
            return true;
        } else if (Familia == 'D' && Opuesto != 'C') {
            return true;
        } else if (Familia == 'P' && Opuesto != 'T') {
            return true;
        } else if (Familia == 'T' && Opuesto != 'P') {
            return true;
        } else {
            return false;
        }

    }

    //Funcion Auxiliar para ver la continuidad de las Cartas
    public boolean EvaluaSucesor(char Valor, char Sucesor) {
        if (Valor == Sucesor) {
            return true;
        } else {
            return false;
        }
    }

    
    public void setZonaLibre(int index) {
        ArrayList<Carta> Linea;
        Carta miCarta;
        if (!EspacioTemporal.isEmpty() && EspacioTemporal.size() == 1) {
            Linea = ZonaLibre.get(index);
            miCarta = EspacioTemporal.get(0);
            miCarta.setPosX((index * 130) + 100);
            miCarta.setPosY(50);
            if (Linea.size() > 1) {
                Linea.remove(1);
            }
            Linea.add(miCarta);
            EspacioTemporal.remove(0);
        }
    }

    public boolean ValidarZonaLibre(int index) {
        ArrayList<Carta> Linea = ZonaLibre.get(index);
        Carta miCarta;
        if (Linea.size() > 1) {
            miCarta = Linea.get(1);
        } else {
            miCarta = Linea.get(0);
        }
        if (miCarta.getValor() == '0') {
            return true;
        } else {
            return false;
        }
    }

    public boolean ValidarZonaFinal(int index) {
        boolean res = false;
        Carta miCarta, CartaBase;
        if (!EspacioTemporal.isEmpty() && EspacioTemporal.size() == 1) {
            miCarta = EspacioTemporal.get(0);
            CartaBase = ZonaFinal.get(index).get(ZonaFinal.get(index).size() - 1);
            if (ZonaFinal.get(index).size() == 1) {
                if (miCarta.getValor() == 'A') {
                    res = true;
                } else {
                    res = false;
                }
            } else {
                
                if (EvaluaSucesor(CartaBase.getSucesor(), miCarta.getValor())) {
                    if (miCarta.getFamilia() == CartaBase.getFamilia()) {
                        res = true;
                    } else {
                        res = false;
                    }
                } else {
                    res = false;
                }
            }
        }
        return res;
    }

    public void setZonaFinal(int index) {
        ArrayList<Carta> Linea;
        Carta miCarta;
        if (!EspacioTemporal.isEmpty() && EspacioTemporal.size() == 1) {
            Linea = ZonaFinal.get(index);
            miCarta = EspacioTemporal.get(0);
            miCarta.setPosX((index * 130) + 650);
            miCarta.setPosY(50);
            Linea.add(miCarta);
            EspacioTemporal.remove(0);
        }
    }

    public ArrayList<ArrayList<Carta>> getZonaLibre() {
        return ZonaLibre;
    }

    public ArrayList<ArrayList<Carta>> getZonaFinal() {
        return ZonaFinal;
    }

    public ArrayList<Carta> getEspacioTemporal() {
        return EspacioTemporal;
    }

    public void LiberarEspacioTemporal(int index) {
        if (index >= 0) {
            Carta Base, Nueva;
            int Final = EspacioTemporal.size();
            for (int i = 0; i < Final; i++) {
                Base = Baraja.get(index).get(Baraja.get(index).size() - 1);
                Nueva = EspacioTemporal.get(0);
                Nueva.setPosX(Base.getPosX());
                if (Baraja.get(index).size() == 1) {
                    Nueva.setPosY(Base.getPosY());
                } else {
                    Nueva.setPosY(Base.getPosY() + 40);
                }
                Baraja.get(index).add(Nueva);
                EspacioTemporal.remove(0);
            }
        }
    }

    public void LiberarEspacioTemporalZonaLibre(int index) {
        ArrayList<Carta> Linea;
        Carta miCarta;
        if (!EspacioTemporal.isEmpty() && EspacioTemporal.size() == 1 && index != -1) {
            Linea = ZonaLibre.get(index);
            miCarta = EspacioTemporal.get(0);
            miCarta.setPosX((index * 130) + 100);
            miCarta.setPosY(50);
            Linea.add(miCarta);
            EspacioTemporal.remove(0);
        }
    }
    
    public void LiberarEspacioTemporalZonaFinal(int index) {
        ArrayList<Carta> Linea;
        Carta miCarta;
        if (!EspacioTemporal.isEmpty() && EspacioTemporal.size() == 1 && index != -1) {
            Linea = ZonaFinal.get(index);
            miCarta = EspacioTemporal.get(0);
            miCarta.setPosX((index * 130) + 650);
            miCarta.setPosY(50);
            Linea.add(miCarta);
            EspacioTemporal.remove(0);
        }
    }
    
    public void setEspacioTemporalZonaLibre(int Zona) {
        ArrayList<Carta> Linea;
        Carta miCarta;
        if (EspacioTemporal.isEmpty()) {
            Linea = ZonaLibre.get(Zona);
            if (Linea.size() > 1) {
                miCarta = Linea.get(1);
                EspacioTemporal.add(miCarta);
                Linea.remove(1);
            }
        }
    }

    public void setEspacioTemporalZonaFinal(int Zona) {
        ArrayList<Carta> Linea;
        Carta miCarta;
        int index = -1;
        if (EspacioTemporal.isEmpty()) {
            Linea = ZonaFinal.get(Zona);
            if (Linea.size() > 1) {
                index = Linea.size() - 1;
                miCarta = Linea.get(index);
                EspacioTemporal.add(miCarta);
                Linea.remove(index);
            }
        }
    }

    public boolean validaMovimiento(int Destino) {
        boolean res = false;
        if (!EspacioTemporal.isEmpty()) {
            Carta CartaDestino = Baraja.get(Destino).get(Baraja.get(Destino).size() - 1);
            Carta CartaInicial = EspacioTemporal.get(0);
            if (CartaDestino.getValor() == '0') {
                res = true;
            } else {
                if (EvaluaSucesor(CartaDestino.getValor(), CartaInicial.getSucesor())) {
                    if (EvaluaColor(CartaDestino.getFamilia(), CartaInicial.getFamilia())) {
                        res = true;
                    } else {
                        res = false;
                    }
                } else {

                    res = false;
                }
            }
        }
        return res;
    }

    private ArrayList<Carta> getListadoCartas() {
        return ListadoCartas;
    }

    public ArrayList<ArrayList<Carta>> getBaraja() {
        return Baraja;
    }

    private Carta getCartaControl(int PosX, int PosY) {
        Carta Control = new Carta();
        Control.setFamilia('N');
        Control.setPosX(PosX);
        Control.setPosY(PosY);
        Control.setValor('0');
        try {
            Control.setImagen(ImageIO.read(getClass().getResource("/Images/" + "K1" + ".png")));
        } catch (Exception ex) {
            System.err.println("Ocurrio un error " + ex.getMessage());
        }
        return Control;
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

}
