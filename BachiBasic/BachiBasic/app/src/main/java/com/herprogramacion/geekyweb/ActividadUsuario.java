package com.herprogramacion.geekyweb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.herprogramacion.geekyweb.tools.Constantes;
import com.herprogramacion.geekyweb.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActividadUsuario extends Base {
    static final int DATE_DIALOG_ID = 999;
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    public String file2 = "";
    ImageView imgv;
    TextView txtnombre;
    TextView txtap1;
    TextView txtap2;
    TextView txtemail;
    TextView pass;
    private String encoded_string="";
    private String email="";
    private String name = "";
    private File file = new File(ruta_fotos);
    //*********************
    private int year;
    private int month;
    private int day;
    private TextView fecha_text;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            fecha_text.setText(new StringBuilder().append(day)
                    .append("/").append(month + 1).append("/").append(year)
                    .append(" "));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_usuario);
        CambiarColorFondoActivity(Color.GRAY);
        txtnombre = (TextView) findViewById(R.id.txtnombre);
        txtap1 = (TextView) findViewById(R.id.txtap1);
        txtap2 = (TextView) findViewById(R.id.txtap2);
        txtemail = (TextView) findViewById(R.id.txtemail);
        pass = (TextView) findViewById(R.id.txtpass);

        imgv = (ImageView) findViewById(R.id.imageViewavatar2);
        name = Environment.getExternalStorageDirectory() + "/test.jpg";
        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoSiNo(findViewById(R.id.imageViewavatar2));
            }
        });

        ImageView imgv2 = (ImageView) findViewById(R.id.imageViewbachi);
        Intent intento = new Intent(getApplicationContext(), Main.class);
        onclickImagenCambiarVista(imgv2, intento);
        if (!VariablesGlobales.getInstance().getSessionemail().equals("")) {
            ImageView imagen = (ImageView) findViewById(R.id.imageViewavatar2);
            getimage(VariablesGlobales.getInstance().getSessionemail(), imagen);
        }

        fecha_text = (TextView) findViewById(R.id.fecha_ejemplo_text);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        fecha_text.setText(new StringBuilder()
                .append(day).append("/").append(month + 1).append("/")
                .append(year).append(" "));
        addListenerOnButton();


        new DownloadImage().execute();
        new ObtenerDatosUsuario().execute(VariablesGlobales.getInstance().getSessionemail(),
                VariablesGlobales.getInstance().getSessionpass());
        Button miboton = (Button) findViewById(R.id.btneditarperfil);
        miboton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                actualizaruser();
            }
        });
    }

    public void addListenerOnButton() {
        fecha_text.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DATE_DIALOG_ID);

                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }


    private void actualizaruser() {
        String nombre = "", ap1 = "", ap2 = "", pass = "";

        TextView texto = (TextView) findViewById(R.id.txtnombre);
        nombre = texto.getText().toString();
        texto = (TextView) findViewById(R.id.txtap1);
        ap1 = texto.getText().toString();
        texto = (TextView) findViewById(R.id.txtap2);
        ap2 = texto.getText().toString();
        texto = (TextView) findViewById(R.id.txtemail);
        email = texto.getText().toString();
        texto = (TextView) findViewById(R.id.txtpass);
        pass = texto.getText().toString();
        //String nombre,String pass, String ap1,String ap2,String email,byte[] logoImage
        if (!email.equals("") && !nombre.equals("") && !pass.equals("") && !ap1.equals("") && !ap2.equals("")) {
            if (validateEmail(email)) {
                guardarUsuario(email, nombre, ap1, ap2, pass, fecha_text.getText().toString());
            } else {
                Mensaje("Correo incorrecto");
            }
        } else {
            Mensaje("Verifique que ha llenado todos los campos!");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    ImageView iv = (ImageView) findViewById(R.id.imageViewavatar2);
                    iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                }
            } else {
                ImageView iv = (ImageView) findViewById(R.id.imageViewavatar2);
                iv.setImageBitmap(BitmapFactory.decodeFile(name));
                /**
                 * Para guardar la imagen en la galer’a, utilizamos una conexi—n a un MediaScanner
                 */
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    private MediaScannerConnection msc = null;

                    {
                        msc = new MediaScannerConnection(getApplicationContext(), this);
                        msc.connect();
                    }

                    public void onMediaScannerConnected() {
                        msc.scanFile(name, null);
                    }

                    public void onScanCompleted(String path, Uri uri) {
                        msc.disconnect();
                    }
                };
            }
            /**
             * Recibimos el URI de la imagen y construimos un Bitmap a partir de un stream de Bytes
             */
        } else if (requestCode == SELECT_PICTURE) {
            Uri selectedImage = data.getData();
            Mensaje(selectedImage.getPath());
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                ImageView iv = (ImageView) findViewById(R.id.imageViewavatar2);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
            }
        }
    }

    public void DialogoSiNo(View view) {

// Uso:   DialogoSiNo(findViewById(R.id.btnNombreBoton))

        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());

        builder1.setCancelable(true);
        String[] array = {"1: Tomar Vista previa", "2: Imagen Completa", "3: Obtener de la galería"};
        builder1.setTitle("Seleccione el tipo de imagen.")
                .setItems(array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        int code = TAKE_PICTURE;
                        if (which == 0) {
                            Uri output = Uri.fromFile(new File(name));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                        } else if (which == 2) {
                            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            code = SELECT_PICTURE;
                        }
                        startActivityForResult(intent, code);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    ;

    public boolean agregarUsuario() {
        // CrearBD();
        TextView Mi_textview = (TextView) findViewById(R.id.txtnombre);
        String nombre = Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtap1);
        String ap1 = Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtap2);
        String ap2 = Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtemail);
        String email = VariablesGlobales.getInstance().getSessionemail();
        Mi_textview = (TextView) findViewById(R.id.txtpass);
        String pass = Mi_textview.getText().toString();

        //http://stackoverflow.com/questions/7331310/how-to-store-image-as-blob-in-sqlite-how-to-retrieve-it
        // guardarUsuario(email, nombre, ap1, ap2, pass, "NA", fecha_text.getText().toString());
        return true;
    }

    public void guardarUsuario(String email, String nombre, String apellido1, String apellido2, String pass, String nac) {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("email", email);
        map.put("nombre", nombre);
        map.put("ap1", apellido1);
        map.put("ap2", apellido2);
        map.put("fnac", nac);
        map.put("pass", pass);

        JSONObject jobject = new JSONObject(map);
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.UPDATEUSUARIO,
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
         new Encode_image().execute();
    }

    private void procesarRespuesta(JSONObject response) {
        try {
            String estado = response.getString("estado");
            String mensaje = response.getString("mensaje");
            switch (estado) {
                case "1":

                    Toast.makeText(
                            this,
                            "Usuario modificado correctamente",
                            Toast.LENGTH_LONG).show();
                    Intent intento = new Intent(getApplicationContext(), ActividadLogin.class);
                    startActivity(intento);

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

    private void makeRequest() {
        RequestQueue requestque = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,
                Constantes.GET + "/proyecto1/obtenerimagen.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Mensaje(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();// Mapeo previo


                Bitmap bitmap = ((BitmapDrawable) imgv.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] image = stream.toByteArray();
                String encoded_string = Base64.encodeToString(image, 0);

                String email = VariablesGlobales.getInstance().getSessionemail();
                String direccion = email.split("@")[0];
                Log.d("imagen", direccion);
                map.put("email", email);
                map.put("imagen", encoded_string);
                map.put("dominio", direccion);
                return map;
            }
        };
        requestque.add(request);
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
                        final JSONObject resultJSON = respuestaJSON.getJSONObject("mensaje");   // usuarios es el nombre del campo en el JSON

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    txtnombre.setText(resultJSON.getString("nombre"));
                                    txtap1.setText(resultJSON.getString("apellido1"));
                                    txtap2.setText(resultJSON.getString("apellido2"));
                                    txtemail.setText(resultJSON.getString("email"));
                                    String fecha = resultJSON.getString("nacimiento");
                                    String[] vecfechas = fecha.split("-");
                                    if (vecfechas.length >= 3) {
                                        year = Integer.parseInt(vecfechas[0]);
                                        month = Integer.parseInt(vecfechas[1]);
                                        day = Integer.parseInt(vecfechas[2]);
                                        fecha_text.setText(new StringBuilder().append(day)
                                                .append("/").append(month).append("/").append(year)
                                                .append(" "));
                                    }

                                } catch (JSONException e) {
                                    Mensaje("Error: " + e.getMessage());
                                }
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
            imgv.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }

    private class Encode_image extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids){
            return null;
        }
        @Override
        protected void onPreExecute() {
            makeRequest2();
        }
    }
    private void makeRequest2(){
   //********************************
        Bitmap bitmap = ((BitmapDrawable) imgv.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        encoded_string = Base64.encodeToString(image, 0);
        //********************************

        RequestQueue requestque= Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST,
                Constantes.GET+"/proyecto1/actualizar_imagen.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // Mensaje(response);
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();// Mapeo previo
                String direccion=email.split("@")[0];

                map.put("email", email);
                map.put("imagen",encoded_string);
                map.put("dominio",direccion);
                return map;
            }
        };
        requestque.add(request);
    }
}
