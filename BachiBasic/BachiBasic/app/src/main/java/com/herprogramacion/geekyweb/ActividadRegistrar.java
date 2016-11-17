package com.herprogramacion.geekyweb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
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
import com.herprogramacion.geekyweb.ui.fragmentos.ConfirmDialogFragment;
import com.herprogramacion.geekyweb.ui.fragmentos.DatePickerFragment;
import com.herprogramacion.geekyweb.web.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActividadRegistrar extends Base implements DatePickerFragment.OnDateSelectedListener, ConfirmDialogFragment.ConfirmDialogListener {
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";
    public String file2 = "";
    static final int DATE_DIALOG_ID = 999;
    private TextView fecha_text;
    private int year;
    private int month;
    private int day;
    private String encoded_string="";
    private String email="";
    private String name = "";
    private File file = new File(ruta_fotos);

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
        setContentView(R.layout.activity_actividad_registrar);

        CambiarColorFondoActivity(Color.GRAY);

        ImageView imgv = (ImageView) findViewById(R.id.imageViewbachi);
        Intent intento = new Intent(getApplicationContext(), Main.class);
        onclickImagenCambiarVista(imgv, intento);

        imgv = (ImageView) findViewById(R.id.imageViewperfil);
        name = Environment.getExternalStorageDirectory() + "/test.jpg";
        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoSiNo(findViewById(R.id.imageViewperfil));
            }
        });

        Button miboton = (Button) findViewById(R.id.btnregistrar);
        miboton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {
                if (agregarUsuario()) {

                } else {
                    Mensaje("El usuario no ha sido insertado!, verifique los datos ingresados");
                }
            }
        });
        fecha_text = (TextView) findViewById(R.id.fecha_ejemplo_text);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        fecha_text.setText(new StringBuilder()
                .append(day).append("/").append(month + 1).append("/")
                .append(year).append(" "));
        addListenerOnButton();
    }
    public boolean agregarUsuario() {
        CrearBD();
        TextView Mi_textview = (TextView) findViewById(R.id.txtnombre);
        String nombre = Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtap1);
        String ap1 = Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtap2);
        String ap2 = Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtemail);
         email = Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtpass);
        String pass = Mi_textview.getText().toString();
        ImageView imgv = (ImageView) findViewById(R.id.imageViewperfil);
        Bitmap bitmap = ((BitmapDrawable) imgv.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] image = stream.toByteArray();
        encoded_string = Base64.encodeToString(image, 0);
        //http://stackoverflow.com/questions/7331310/how-to-store-image-as-blob-in-sqlite-how-to-retrieve-it
        if (validateEmail(email)) {

            guardarUsuario(email, nombre, ap1, ap2, pass, "NA", fecha_text.getText().toString());
                return true;

        } else {
            Mensaje("Correo incorrecto");
            return false;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoCode = "pic_" + date;
        return photoCode;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    ImageView iv = (ImageView) findViewById(R.id.imageViewperfil);
                    iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                }
            } else {
                ImageView iv = (ImageView) findViewById(R.id.imageViewperfil);
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
                ImageView iv = (ImageView) findViewById(R.id.imageViewperfil);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
            }
        }
    }

    public void DialogoSiNo(View view) {
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
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        InsertFragment insertFragment = (InsertFragment)
                getSupportFragmentManager().findFragmentByTag("InsertFragment");

        if (insertFragment != null) {
            insertFragment.actualizarFecha(year, month, day);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        InsertFragment insertFragment = (InsertFragment)
                getSupportFragmentManager().findFragmentByTag("InsertFragment");

        if (insertFragment != null) {
            finish(); // Finalizar actividad descartando cambios
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        InsertFragment insertFragment = (InsertFragment)
                getSupportFragmentManager().findFragmentByTag("InsertFragment");
        if (insertFragment != null) {
            // Nada por el momento
        }
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
                return new DatePickerDialog(this, datePickerListener, year, month,day);
        }
        return null;
    }
    public void guardarUsuario(String email,String nombre,String apellido1,String apellido2,String pass,String image,String nac) {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("email", email);
        map.put("nombre",nombre );
        map.put("apellido1",apellido1 );
        map.put("apellido2",apellido2);
        map.put("pass",pass );
        map.put("rutafoto",image);
        map.put("nacimiento",nac);
        JSONObject jobject = new JSONObject(map);
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERTUSUARIO,
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
                    if(mensaje.equals("true")){
                        Toast.makeText(
                                this,
                                "Usuario agregado correctamente",
                                Toast.LENGTH_LONG).show();
                        Intent intento = new Intent(getApplicationContext(), ActividadLogin.class);
                        startActivity(intento);
                    }else{
                        Toast.makeText(
                                this,
                                mensaje,
                                Toast.LENGTH_LONG).show();
                    }
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
    private class Encode_image extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids){
            return null;
        }
        @Override
        protected void onPreExecute() {
            makeRequest();
        }
    }
    private void makeRequest(){
        RequestQueue requestque= Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.POST,
                Constantes.GET+"/proyecto1/obtenerimagen.php",
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

