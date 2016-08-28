package com.herprogramacion.geekyweb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
/*https://www.youtube.com/watch?v=ibDstPTwBdw&list=RDibDstPTwBdw#t=0*/
public class ActividadPreguntas extends Base  {
    ArrayList<Pregunta> preguntas;// ObtenerDatos();
    static int opcionSeleccionada=-1;
    static int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_preguntas);
        CambiarColorFondoActivity(Color.GRAY);

        preguntas =CargarInfo();
        ajustarEventos();
        Chronometer Mi_chronometer = (Chronometer) findViewById(R.id.chronometer);
        Mi_chronometer.start();
        advertir();
        ImageView imagen=(ImageView)findViewById(R.id.imageViewuser);
        getimage(VariablesGlobales.getInstance().getSessionemail(), imagen);

        ImageView imgv = (ImageView) findViewById(R.id.imageViewbachi);
        imgv.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {
                DialogoSiNo();
            }
        });
    }
public void aumentar(int n){
    ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
    TextView progreso=(TextView)findViewById(R.id.textViewtiempotranscurrido);
    progreso.setText(n+"%");
    progressBar.setProgress(n);
}
    public void opcionSeleccionada(){
                RadioButton rb1 = (RadioButton) findViewById(R.id.radioButtonopc1);
                RadioButton rb2 = (RadioButton) findViewById(R.id.radioButtonopc2);
                RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3opc3);
                RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4opc4);
                if (rb1.isChecked()) {
                    opcionSeleccionada = 1;
                   // Mensaje(opcionSeleccionada + "");
                }
                if (rb2.isChecked()) {
                    opcionSeleccionada = 2;
                   // Mensaje(opcionSeleccionada + "");
                }
                if (rb3.isChecked()) {
                    opcionSeleccionada = 3;
                  //  Mensaje(opcionSeleccionada + "");
                }
                if (rb4.isChecked()) {
                    opcionSeleccionada = 4;
                   // Mensaje(opcionSeleccionada + "");
                }


    }
static boolean continua=true;
    public void advertir(){

        Thread threadCliente;
        final Chronometer Mi_chronometer = (Chronometer) findViewById(R.id.chronometer);
            threadCliente = new Thread() {
                @Override
                public void run() {
                   // boolean continua=true;
                    while(true) {
                        while (continua) {
                            final String[] crono = Mi_chronometer.getText().toString().split(":");
                            if (Integer.parseInt(crono[1]) > 30||Integer.parseInt(crono[0]) > 1) {
                               ReproducirAudio(R.raw.suspenso);
                                continua = false;
                            }
                        }
                        try {
                            Thread.sleep(30000);
                            continua=true;
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            };

            threadCliente.start();
    }

    @Override
    public void onBackPressed(){
            DialogoSiNo();
    }
    public void ajustarEventos(){
       // final TextView preguta = (TextView) findViewById(R.id.editTextpreguntas);

        CrearBD();

        cambiarPreguntas(preguntas.get(0).getDescripcion());
        ImageView midib = (ImageView) findViewById(R.id.imageView2);
        midib.setImageResource(preguntas.get(0).getImagen());
        cambiarOpciones(preguntas.get(0).getOpciones());

        Button MiBoton = (Button) findViewById(R.id.btnSiguiente);
        MiBoton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {
                VariablesGlobales vg = VariablesGlobales.getInstance();
                opcionSeleccionada();
                if (opcionSeleccionada != -1) {
                    i++;
                    if (i < preguntas.size()) {
                        Pregunta pre = preguntas.get(i);
                        if (pre.getRespuesta() != opcionSeleccionada) {
                            ReproducirAudio(R.raw.puapua);
                            vg.setIncorretas(vg.getIncorretas() + 1);
                        } else {
                            ReproducirAudio(R.raw.correcto);
                            vg.setCorrectas(vg.getCorrectas() + 1);
                        }
                        int x = i;
                        int res = (100 * x) / preguntas.size();
                        aumentar(res);
                        cambiarPreguntas(pre.getDescripcion());
                        ImageView midib = (ImageView) findViewById(R.id.imageView2);
                        midib.setImageResource(pre.getImagen());
                        cambiarOpciones(pre.getOpciones());
                        opcionSeleccionada = -1;
                    } else {
                        i = -1;
                        vg.setCantidadPreguntas(preguntas.size());
                        PararReproducirAudio();
                        Chronometer Mi_chronometer = (Chronometer) findViewById(R.id.chronometer);
                        Mi_chronometer.setBase(SystemClock.elapsedRealtime());
                        Intent intento = new Intent(getApplicationContext(), ActividadResultado.class);
                        startActivity(intento);

                        // Mensaje("Cuestionario Finalizado!"+" correctas: "+vg.getCorrectas()+" Incorrectas "+
                        // vg.getIncorretas()+" cantidadPreguntas"+vg.getCantidadPreguntas());
                    }
                } else {
                    Mensaje("Debe elegir una opción");
                }
            }

        });
    }
    public void cambiarPreguntas(String texto){
        TextView preguta = (TextView) findViewById(R.id.editTextpreguntas);
        preguta.setText(texto);
    }

    public void cambiarOpciones(String []opcs){
        if(opcs.length>3) {
            RadioButton rb1 = (RadioButton) findViewById(R.id.radioButtonopc1);
            rb1.setText(opcs[0]);
            RadioButton rb2 = (RadioButton) findViewById(R.id.radioButtonopc2);
            rb2.setText(opcs[1]);
            RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3opc3);
            rb3.setText(opcs[2]);
            RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4opc4);
            rb4.setText(opcs[3]);
        }
    }
    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
    public void DialogoSiNo(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("¿Estas seguro de salir?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PararReproducirAudio();
                        Chronometer Mi_chronometer = (Chronometer) findViewById(R.id.chronometer);
                        Mi_chronometer.setBase(SystemClock.elapsedRealtime());
                       // Mi_chronometer.stop();
                        Intent intento = new Intent(getApplicationContext(), Main.class);
                        startActivity(intento);
                    } });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Mensaje("negativo");
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };
    public ArrayList<Pregunta> CargarInfo(){
        VariablesGlobales vg=VariablesGlobales.getInstance();
        InputStream miarchivo;
        if(vg.getTipoTest()==1) {
            miarchivo = getResources().openRawResource(R.raw.biologia);
        }else if(vg.getTipoTest()==2) {
            miarchivo = getResources().openRawResource(R.raw.matematica);
        }else if(vg.getTipoTest()==3) {
            miarchivo = getResources().openRawResource(R.raw.espanol);
        }else if(vg.getTipoTest()==4) {
            miarchivo = getResources().openRawResource(R.raw.ingles);
        }else if(vg.getTipoTest()==5) {
            miarchivo = getResources().openRawResource(R.raw.civica);
        }
        else if(vg.getTipoTest()==6) {
            miarchivo = getResources().openRawResource(R.raw.sociales);
        }else
         {
            miarchivo = getResources().openRawResource(R.raw.biologia);
        }
        ArrayList<Pregunta> lista=DeInputStringaString(miarchivo);


        return lista;
    }
    private ArrayList<Pregunta> DeInputStringaString(InputStream is) {
        BufferedReader br = null;
       // StringBuilder sb = new StringBuilder();
        String line;
        ArrayList<Pregunta> listacuriosa=new ArrayList();
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                String descripcion=line;
                String opciones[]={br.readLine(),br.readLine(),br.readLine(),br.readLine()};
                int correcta=Integer.parseInt(br.readLine());
                Pregunta pregunta=new Pregunta(descripcion,opciones,0,correcta,R.drawable.question);
               // sb.append(line);
                listacuriosa.add(pregunta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {br.close();}
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return listacuriosa;
    }
}

