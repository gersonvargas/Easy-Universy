package com.herprogramacion.geekyweb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

public class Actividad_Elegir_test extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad__elegir_test);
        CambiarColorFondoActivity(Color.GRAY);


        ImageView imgv = (ImageView) findViewById(R.id.imageViewbachi);
        Intent intento = new Intent(getApplicationContext(), Main.class);
        onclickImagenCambiarVista(imgv, intento);

        ImageView imgv2 = (ImageView) findViewById(R.id.imageButton);
        Intent intento2 = new Intent(getApplicationContext(), ActividadPreguntas.class);
        onclickImagenCambiarVista(imgv2, intento2);
    }

}
