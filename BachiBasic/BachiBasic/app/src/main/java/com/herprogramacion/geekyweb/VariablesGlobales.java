package com.herprogramacion.geekyweb;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

/**
 * Created by Guaria on 5/20/2016.
 */
public class VariablesGlobales {
    private static VariablesGlobales instance = null;

   private int correctas;
    private int incorretas;
    private int cantidadPreguntas;
    private String sessionemail;
    private String sessionpass;
    private int tipoTest;
    private byte[] imagen;

    protected VariablesGlobales() {
        this.correctas=0;
        this.incorretas=0;
        this.cantidadPreguntas=0;
        this.sessionemail="";
        this.sessionpass="";
        imagen=null;
        tipoTest=0;
    }
    public static VariablesGlobales getInstance() {
        if(instance == null) {
            instance = new VariablesGlobales();
        }
        return instance;
    }
    //Colocar en la clase Base


    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public int getTipoTest() {
        return tipoTest;
    }

    public void setTipoTest(int tipoTest) {
        this.tipoTest = tipoTest;
    }

    public String getSessionemail() {
        return sessionemail;
    }

    public void setSessionemail(String sessionemail) {
        this.sessionemail = sessionemail;
    }

    public String getSessionpass() {
        return sessionpass;
    }

    public void setSessionpass(String sessionpass) {
        this.sessionpass = sessionpass;
    }

    public int getCorrectas() {
        return correctas;
    }

    public void setCorrectas(int correctas) {
        this.correctas = correctas;
    }

    public int getIncorretas() {
        return incorretas;
    }

    public void setIncorretas(int incorretas) {
        this.incorretas = incorretas;
    }

    public int getCantidadPreguntas() {
        return cantidadPreguntas;
    }

    public void setCantidadPreguntas(int cantidadPreguntas) {
        this.cantidadPreguntas = cantidadPreguntas;
    }
}
