package com.herprogramacion.geekyweb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Base extends ActionBarActivity {
    public static int opcMateria=0;
    //Ejemplo de uso:
    public void CambiarColorFondoActivity(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }
    public void onclickImagenCambiarVista(ImageView img,final Intent intent){
        img.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {


                startActivity(intent);
            }
        });

    }
    //Colocar en la clase Base
    DBAdapter db;

    public void CrearBD() {
        db = new DBAdapter(this);
        //db = new DBAdapter(this);
    }

    public void Mensaje(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    };

    public void AgregarDato(Pregunta pregunta) {
        db.open();
        if (db.insertDato(pregunta) >= 0) {
            //Mensaje("Pregunta agregada correctamente: "+pregunta.getDescripcion());
        }

        db.close();
    }
    public boolean AgregarUsuario(String nombre,String pass,String ap1,String ap2,String email,byte[] logoImage) {
        db.open();
        boolean bandera=false;
        if (db.insertUsuario(nombre, pass, ap1, ap2, email, logoImage) >= 0) {
            Mensaje("Usuario agregado correctamente: "+email);
           bandera=true;
        }
        db.close();
        return bandera;
    }
    public boolean BorrarDatos(){
        db.open();
        return db.BorrarDatos();
    }
    public ArrayList<Pregunta> ObtenerDatos() {
        //--cargamos todos los datos---
        ArrayList<Pregunta> listapreguntas=new ArrayList<>();
        db.open();
        Cursor c = db.CargarTodosLosDatos();
        if (c.moveToFirst())
        {
            do {
                listapreguntas.add(MostarDato(c));
            } while (c.moveToNext());
        }
        db.close();
        return listapreguntas;
    }
    public void ObtenerDato() {
        //---cargar un contacto ---
        db.open();
        Cursor c = db.ObtenerDato(2);
        if (c.moveToFirst())
            MostarDato(c);
        else
            Mensaje("No se encontró el dato");
        db.close();
    }
    public String[] ObtenerUsuariovalores(String email) {
        //---cargar un contacto ---
        String[]valores=null;
        db.open();
        Cursor c = db.ObtenerUsuario(email);
        if (c.moveToFirst())
           valores= MostarDato2(c);
        else
            Mensaje("No se encontró el dato");
        db.close();
        return valores;
    }
    public boolean ObtenerUsuario(String email,String pass) {
        //---cargar un contacto ---
        boolean bandera=false;
        db.open();
        Cursor c = db.ObtenerUsuario(email);
        //Mensaje(c.getString(2));
        if (c.moveToFirst()&&c.getString(1).equals(pass)) {
            bandera=true;
        }
        db.close();
        return bandera;
    }
    public boolean ActualizarUsuario(String nombre,String pass, String ap1,String ap2,String email,byte[] logoImage)
    {
        CrearBD();
        db.open();
        boolean res= db.ActualizarUsuario(nombre, pass, ap1, ap2, email, logoImage);
        db.close();
        return res;
    }
    public void ActualizarDato(Pregunta pregunta) {
        //---update a contact---
        db.open();
        if (db.ActualizarDato(pregunta))
            Mensaje("Se actualizó la pregunta: "+pregunta.getDescripcion());

        db.close();
    }
    public void BorrarDato() {
        db.open();
        if (db.BorrarDato(1))
            Mensaje("Borrado exitoso");
        else
            Mensaje("Fallo al intentar borrar");
        db.close();
    }
    public Pregunta MostarDato(Cursor c)
    {
        String []opciones={c.getString(2),c.getString(3),c.getString(4),c.getString(5)};
        Pregunta pregunta=new Pregunta(c.getString(1),opciones,
                Integer.parseInt(c.getString(0)),Integer.parseInt(c.getString(6)),Integer.parseInt(c.getString(7)));

        return pregunta;
    }
    public String[] MostarDato2(Cursor c)
    {
        String []opciones={c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4)};
        return opciones;
    }
    MediaPlayer misonido;

    public void ReproducirAudio(int opc){
        misonido = MediaPlayer.create(this,opc);
        misonido.start();

    }
    public void PararReproducirAudio(){
        if(misonido!=null)
        misonido.stop();

    }
    public void getimage(String i,ImageView imageView){
        CrearBD();
        db.open();
        db.getimage(i,imageView);
        db.close();
    }

    //Expresion regular validar correo
    private  final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public boolean validateEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}