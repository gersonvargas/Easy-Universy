package com.herprogramacion.geekyweb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActividadRegistrar extends Base {
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;

    /**
     * Variable que define el nombre para el archivo donde escribiremos
     * la fotograf’a de tama–o completo al tomarla.
     */
    private String name = "";

        private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";

     private File file = new File(ruta_fotos);
    public String file2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_registrar);

        CambiarColorFondoActivity(Color.GRAY);


        ImageView imgv = (ImageView) findViewById(R.id.imageViewbachi);
        Intent intento = new Intent(getApplicationContext(), Main.class);
        onclickImagenCambiarVista(imgv, intento);

        imgv = (ImageView) findViewById(R.id.imageViewavatar);
        name = Environment.getExternalStorageDirectory() + "/test.jpg";
              imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoSiNo(findViewById(R.id.imageViewavatar));
    }
});


        Button miboton= (Button) findViewById(R.id.btnregistrar);
        miboton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

                if(agregarUsuario()) {
                    Intent intento = new Intent(getApplicationContext(), ActividadLogin.class);
                    startActivity(intento);
                }else{Mensaje("El usuario no ha sido insertado!, verifique los datos ingresados");}
            }
        });
    }
    public boolean agregarUsuario(){
        CrearBD();
        TextView Mi_textview = (TextView) findViewById(R.id.txtnombre);
        String nombre=Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtap1);
        String ap1=Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtap2);
        String ap2=Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtemail);
        String email=Mi_textview.getText().toString();
        Mi_textview = (TextView) findViewById(R.id.txtpass);
        String pass=Mi_textview.getText().toString();
        ImageView imgv = (ImageView) findViewById(R.id.imageViewavatar);
        Bitmap bitmap = ((BitmapDrawable)imgv.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image=stream.toByteArray();
        //String img_str = Base64.encodeToString(image, 0);
      //http://stackoverflow.com/questions/7331310/how-to-store-image-as-blob-in-sqlite-how-to-retrieve-it
        if(validateEmail(email)){
        if(AgregarUsuario(nombre,pass,ap1,ap2,email,image)){
            return true;
        }else {
            return false;
        }}else{
            Mensaje("Correo incorrecto");
            return false;
        }
    }
    @SuppressLint("SimpleDateFormat")
      private String getCode()
      {
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
           String date = dateFormat.format(new Date());
           String photoCode = "pic_" + date;
           return photoCode;
          }

              @Override
      public boolean onCreateOptionsMenu(Menu menu) {
           // Inflate the menu; this adds items to the action bar if it is present.
           getMenuInflater().inflate(R.menu.menu_main, menu);
           return true;
          }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    ImageView iv = (ImageView)findViewById(R.id.imageViewavatar);
                    iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                }
            } else {
                ImageView iv = (ImageView)findViewById(R.id.imageViewavatar);
                iv.setImageBitmap(BitmapFactory.decodeFile(name));
                /**
                 * Para guardar la imagen en la galer’a, utilizamos una conexi—n a un MediaScanner
                 */
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    private MediaScannerConnection msc = null; {
                        msc = new MediaScannerConnection(getApplicationContext(), this); msc.connect();
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
        } else if (requestCode == SELECT_PICTURE){
            Uri selectedImage = data.getData();
            Mensaje(selectedImage.getPath());
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                ImageView iv = (ImageView)findViewById(R.id.imageViewavatar);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {}
        }
    }
    public void DialogoSiNo(View view){

// Uso:   DialogoSiNo(findViewById(R.id.btnNombreBoton))

            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());

            builder1.setCancelable(true);
        String[] array={"1: Tomar Vista previa","2: Imagen Completa","3: Obtener de la galería"};
        builder1.setTitle("Seleccione el tipo de imagen.")
                .setItems(array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        int code = TAKE_PICTURE;
                        if (which == 0) {
                            Uri output = Uri.fromFile(new File(name));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                        }else
                        if (which == 2) {
                            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            code = SELECT_PICTURE;
                        }
                        startActivityForResult(intent, code);
                    }
                });

        AlertDialog alert11 = builder1.create();
            alert11.show();
    };
}

