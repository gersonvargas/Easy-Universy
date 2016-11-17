package com.herprogramacion.geekyweb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.herprogramacion.geekyweb.tools.Constantes;
import com.herprogramacion.geekyweb.web.VolleySingleton;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*https://www.youtube.com/watch?v=ibDstPTwBdw&list=RDibDstPTwBdw#t=0*/
public class ActividadPreguntas extends Base {
    static ArrayList<Pregunta> preguntas;// ObtenerDatos();

    static int opcionSeleccionada = -1;
    static int i = 0;
    static boolean continua = true;
    static EditText texto;
    TextView resultado;
    ImageView imagenuser;
    String comentario = "";
    int numeropregunta = 0;
    boolean estado = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_preguntas);
        CambiarColorFondoActivity(Color.GRAY);
        resultado = (TextView) findViewById(R.id.editTextpreguntas);
        preguntas = new ArrayList<>();
        ajustarEventos();
        Chronometer Mi_chronometer = (Chronometer) findViewById(R.id.chronometer);
        Mi_chronometer.start();
        advertir();
        imagenuser = (ImageView) findViewById(R.id.imageViewdatosuser);
        new DownloadImage().execute("");
        ImageView imgv = (ImageView) findViewById(R.id.imageViewbachi);
        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogoSiNo();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DemeTexto(findViewById(R.id.fab));
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ObtenerComenatarios().execute();
                    }
                }
        );


        imagenuser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ObtenerDatosUsuario().execute(VariablesGlobales.getInstance().getSessionemail(),
                                VariablesGlobales.getInstance().getSessionpass());
                    }
                }
        );

    }

    public void aumentar(int n) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView progreso = (TextView) findViewById(R.id.textViewtiempotranscurrido);
        progreso.setText(n + "%");
        progressBar.setProgress(n);
    }

    public void opcionSeleccionada() {
        RadioButton rb1 = (RadioButton) findViewById(R.id.radioButtonopc1);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radioButtonopc2);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3opc3);
        RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4opc4);
        RadioButton rb5 = (RadioButton) findViewById(R.id.radioButton4opc5);
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
        if (rb5.isChecked()) {
            opcionSeleccionada = 5;
            // Mensaje(opcionSeleccionada + "");
        }


    }

    public void advertir() {

        Thread threadCliente;
        final Chronometer Mi_chronometer = (Chronometer) findViewById(R.id.chronometer);
        threadCliente = new Thread() {
            @Override
            public void run() {
                // boolean continua=true;
                while (true) {
                    while (continua) {
                        final String[] crono = Mi_chronometer.getText().toString().split(":");
                        if (Integer.parseInt(crono[1]) > 30 || Integer.parseInt(crono[0]) > 1) {
                            ReproducirAudio(R.raw.suspenso);
                            continua = false;
                        }
                    }
                    try {
                        Thread.sleep(30000);
                        continua = true;
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };

        threadCliente.start();
    }

    @Override
    public void onBackPressed() {
        DialogoSiNo();
    }

    public void ajustarEventos() {
        preguntas = VariablesGlobales.getInstance().getPreguntas();
        if (preguntas.size() > 0) {
            estado = false;
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

                        if (i < preguntas.size()) {
                            Pregunta pre;

                            pre = preguntas.get(i);

                            // Log.d("pregunta"+i,pre.getDescripcion());
                            if (pre.getRespuesta() != opcionSeleccionada) {
                                vg.setIncorretas(vg.getIncorretas() + 1);
                                ReproducirAudio(R.raw.puapua);
                                determinaropcioncorrecta(pre);

                            } else {
                                vg.setCorrectas(vg.getCorrectas() + 1);
                                ReproducirAudio(R.raw.correcto);
                            }
                            int x = i;
                            int res = (100 * x + 1) / preguntas.size();
                            aumentar(res);
                            if ((i + 1) < preguntas.size()) {
                                pre = preguntas.get(i + 1);
                                cambiarPreguntas(pre.getDescripcion());
                                ImageView midib = (ImageView) findViewById(R.id.imageView2);
                                midib.setImageResource(pre.getImagen());
                                cambiarOpciones(pre.getOpciones());
                                opcionSeleccionada = -1;
                            } else cambiar(vg);

                        } else {
                            cambiar(vg);
                        }
                        i++;
                    } else {
                        Mensaje("Debe elegir una opción");
                    }
                }

            });
        } else {
            Mensaje("No existen preguntas para practicar para PAA");
        }
    }

    public void cambiar(VariablesGlobales vg) {
        i = -1;
        vg.setCantidadPreguntas(preguntas.size());
        PararReproducirAudio();
        Chronometer Mi_chronometer = (Chronometer) findViewById(R.id.chronometer);
        Mi_chronometer.setBase(SystemClock.elapsedRealtime());
        Intent intento = new Intent(getApplicationContext(), ActividadResultado.class);
        startActivity(intento);
        Mensaje("Cuestionario Finalizado!" + " correctas: " + vg.getCorrectas() + " Incorrectas " +
                vg.getIncorretas() + " cantidadPreguntas" + vg.getCantidadPreguntas());
    }

    public void determinaropcioncorrecta(Pregunta p) {
        Mensaje("Respuesta correcta: " + p.getOpciones()[p.getRespuesta() - 1]);
    }

    public void cambiarPreguntas(String texto) {
        TextView preguta = (TextView) findViewById(R.id.editTextpreguntas);
        preguta.setText(texto);
    }

    public void cambiarOpciones(String[] opcs) {
        if (opcs.length > 3) {
            RadioButton rb1 = (RadioButton) findViewById(R.id.radioButtonopc1);
            rb1.setText(opcs[0]);
            RadioButton rb2 = (RadioButton) findViewById(R.id.radioButtonopc2);
            rb2.setText(opcs[1]);
            RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3opc3);
            rb3.setText(opcs[2]);
            RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4opc4);
            rb4.setText(opcs[3]);
            RadioButton rb5 = (RadioButton) findViewById(R.id.radioButton4opc5);
            rb5.setText(opcs[4]);
        }
    }

    ;

    public void Mensaje(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    ;

    public void DialogoSiNo() {
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
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Mensaje("negativo");
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public ArrayList<Pregunta> CargarInfo() {
        CrearBD();
        VariablesGlobales vg = VariablesGlobales.getInstance();
        InputStream miarchivo;
        if (vg.getTipoTest() == 1) {
            miarchivo = getResources().openRawResource(R.raw.biologia);
        } else if (vg.getTipoTest() == 2) {
            miarchivo = getResources().openRawResource(R.raw.matematica);
        } else if (vg.getTipoTest() == 3) {
            miarchivo = getResources().openRawResource(R.raw.espanol);
        } else if (vg.getTipoTest() == 4) {
            miarchivo = getResources().openRawResource(R.raw.ingles);
        } else if (vg.getTipoTest() == 5) {
            miarchivo = getResources().openRawResource(R.raw.civica);
        } else if (vg.getTipoTest() == 6) {
            miarchivo = getResources().openRawResource(R.raw.sociales);
        } else {
            miarchivo = getResources().openRawResource(R.raw.biologia);
        }
        ArrayList<Pregunta> lista = ObtenerDatos();//DeInputStringaString(miarchivo);
        return lista;
    }

    public void DemeTexto(View view) {
        // Uso:   DemeTexto(findViewById(R.id.btnNombreBoton))
        texto = new EditText(view.getContext());
        texto.setTextColor(Color.BLUE);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        builder1.setView(inflater.inflate(R.layout.abc_alert_dialog_material, null));
        builder1.setMessage("Mensage:");
        texto.selectAll();
        builder1.setView(texto);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        implementarHilo();
                    }
                });
        builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Mensaje("Cancelado");
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
       /* AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater2 = getLayoutInflater();

        builder.setView(inflater2.inflate(R.layout.dialogopersonalizado, null))
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/
    }

    public int validarJSON(String valor) {
        try {
            JSONArray json = new JSONArray(valor);
            return json.length();
        } catch (Exception ex) {
        }
        return 0;
    }

    public void implementarHilo() {
        Thread tr = new Thread() {
            public void run() {

                // final String resultado = enviarDatos(texto.getText().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        guardarMeta(texto.getText().toString());
                    }
                });
            }
        };
        tr.start();

    }

    public void guardarMeta(String mensaje) {
        numeropregunta = preguntas.get(i).getNumero();
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("mensaje", mensaje);
        map.put("email", VariablesGlobales.getInstance().getSessionemail());
        map.put("pregunta", numeropregunta + "");

        Log.d("correo", VariablesGlobales.getInstance().getSessionemail());

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        // Actualizar datos en el servidor
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERT,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("error", "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            this,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            this,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class DownloadImage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... mImageUrl) {
            URL url = null;
            int count;
            try {
                String ruta = null;
                url = new URL(Constantes.GET + "/proyecto1/obtenerImagenUsuario.php?email=" +
                        VariablesGlobales.getInstance().getSessionemail());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);        // Paso toda la entrada al StringBuilder
                    }
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    JSONArray resultJSON = respuestaJSON.getJSONArray("valor");   // usuarios es el nombre del campo en el JSON

                    String direccion = "";
                    for (int i = 0; i < resultJSON.length(); i++) {
                        direccion = resultJSON.getJSONObject(i).getString("ruta");
                        String descripcion = resultJSON.getJSONObject(i).getString("nombre");
                        ruta = Constantes.GET + "/proyecto1/" + direccion + "/" + descripcion;
                    }
                }
                URL url2 = new URL(ruta);
                URLConnection conection = url2.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url2.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream("/sdcard/downloadedImage.jpg");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String mImageUrl) {
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedImage.jpg";
            // setting downloaded into image view
            imagenuser.setImageDrawable(Drawable.createFromPath(imagePath));
        }

    }

    public class ObtenerComenatarios extends AsyncTask<String, Integer, String> {
        private ArrayList<Pregunta> preg = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {
            String devuelve = "";
            URL url = null; // Url de donde queremos obtener información
            try {
                numeropregunta = preguntas.get(i).getNumero();
                url = new URL(Constantes.GET + "/proyecto1/obtenerComentarios.php?numero=" + numeropregunta);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);        // Paso toda la entrada al StringBuilder
                    }
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    String estado = respuestaJSON.getString("estado");


                    if (estado.equals("1")) {
                        JSONArray resultJSON = respuestaJSON.getJSONArray("mensaje");   // usuarios es el nombre del campo en el JSON
                        String direccion = "";
                        int count = resultJSON.length();
                        if (resultJSON.length() > 5) {
                            count = 5;
                        }
                        for (int i = 0; i < count; i++) {
                            comentario += "Usuario: " + resultJSON.getJSONObject(i).getString("nombre") + "\n";
                            comentario += "Mensaje: " + resultJSON.getJSONObject(i).getString("mensaje") + "\n";
                            comentario += "Fecha: " + resultJSON.getJSONObject(i).getString("fecha") + "\n";

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DemeTextoObtenerMensajes(findViewById(R.id.fab2), comentario);
                                comentario = "";
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Mensaje("No hay comentarios.");
                            }
                        });
                    }
                    // ajustarEventos();
                } else {
                    Log.d("iderror", connection.getResponseMessage());
                    //  Mensaje(connection.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return devuelve;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            //  resultado.setText(aVoid);
        }

        @Override
        protected void onPreExecute() {
            //  resultado.setText("");
            super.onPreExecute();
        }

        public ArrayList<Pregunta> getPreguntas() {
            return preg;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        public void DemeTextoObtenerMensajes(View view, String m) {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater2 = getLayoutInflater();
            builder.setMessage(m);
            builder.setView(inflater2.inflate(R.layout.dialogopersonalizado, null))
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        }
    }

    public class ObtenerDatosUsuario extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String devuelve = "";
            URL url = null; // Url de donde queremos obtener información
            try {
                url = new URL(Constantes.GET + "/proyecto1/obtener_usuario_por_id.php?email=" + params[0] + "&pass=" + params[1]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);        // Paso toda la entrada al StringBuilder
                    }
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    String estado = respuestaJSON.getString("estado");
                    if (estado.equals("1")) {
                        JSONObject resultJSON = respuestaJSON.getJSONObject("mensaje");   // usuarios es el nombre del campo en el JSON

                        comentario += "Nombre: " + resultJSON.getString("nombre") + "\n";
                        comentario += "Apellidos: " + resultJSON.getString("apellido1") + " "
                                + resultJSON.getString("apellido2") + "\n";

                        comentario += "F.Nac: " + resultJSON.getString("nacimiento") + "\n";


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DemeTextoObtenerUsuario(findViewById(R.id.imageViewdatosuser), comentario);
                                comentario = "";
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Mensaje("No Se ha podio consultar el usuario.");
                            }
                        });
                    }
                    // ajustarEventos();
                } else {
                    Log.d("iderror", connection.getResponseMessage());
                    //  Mensaje(connection.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return devuelve;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            //  resultado.setText(aVoid);
        }

        @Override
        protected void onPreExecute() {
            //  resultado.setText("");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
        }

        public void DemeTextoObtenerUsuario(View view, String m) {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater2 = getLayoutInflater();
            builder.setMessage(m);
            builder.setView(inflater2.inflate(R.layout.dialogopersonalizado, null))
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        }
    }
}

