package com.herprogramacion.geekyweb;

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
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ActividadUsuario extends Base {
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;

    private String name = "";
    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";

    private File file = new File(ruta_fotos);
    public String file2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_usuario);
        CambiarColorFondoActivity(Color.GRAY);

        ImageView imgv = (ImageView) findViewById(R.id.imageViewavatar2);
        name = Environment.getExternalStorageDirectory() + "/test.jpg";
        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoSiNo(findViewById(R.id.imageViewavatar2));
            }
        });
        Button miboton= (Button) findViewById(R.id.btneditarperfil);
        miboton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                actualizaruser();
            }
        });
         imgv = (ImageView) findViewById(R.id.imageViewbachi);
        Intent intento = new Intent(getApplicationContext(), Main.class);
        onclickImagenCambiarVista(imgv, intento);
        if(!VariablesGlobales.getInstance().getSessionemail().equals("")) {
            ImageView imagen = (ImageView) findViewById(R.id.imageViewavatar2);
            getimage(VariablesGlobales.getInstance().getSessionemail(), imagen);
        }
        inicializar();
    }
    private void inicializar(){
     String []valores=ObtenerUsuariovalores(VariablesGlobales.getInstance().getSessionemail());
        if(valores!=null){
            TextView texto=(TextView)findViewById(R.id.txtnombre);
           texto.setText(valores[0]);
            texto=(TextView)findViewById(R.id.txtap1);
            texto.setText(valores[2]);
            texto=(TextView)findViewById(R.id.txtap2);
            texto.setText(valores[3]);
            texto=(TextView)findViewById(R.id.txtemail);
            texto.setText(valores[4]);
        }
    }
private void actualizaruser(){
    //imageViewavatar2
    //txtnombre
    //txtap1
    //txtap2
    //txtemail
    //txtpass
    String nombre="",ap1="",ap2="",email="",pass="";

    ImageView imgv = (ImageView) findViewById(R.id.imageViewavatar2);
    Bitmap bitmap = ((BitmapDrawable)imgv.getDrawable()).getBitmap();
    ByteArrayOutputStream stream=new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

    byte[] image=stream.toByteArray();

    TextView texto=(TextView)findViewById(R.id.txtnombre);
    nombre=texto.getText().toString();
    texto=(TextView)findViewById(R.id.txtap1);
    ap1=texto.getText().toString();
    texto=(TextView)findViewById(R.id.txtap2);
    ap2=texto.getText().toString();
    texto=(TextView)findViewById(R.id.txtemail);
    email=texto.getText().toString();
    texto=(TextView)findViewById(R.id.txtpass);
    pass=texto.getText().toString();
    //String nombre,String pass, String ap1,String ap2,String email,byte[] logoImage
    if(!email.equals("")&& !nombre.equals("")&&!pass.equals("")&&!ap1.equals("")&&!ap2.equals("")) {
        if (validateEmail(email)) {
            if (ActualizarUsuario(nombre, pass, ap1, ap2, email, image)) {
                Intent intento = new Intent(getApplicationContext(), ActividadLogin.class);
                startActivity(intento);
            } else {
                Mensaje("Verifique que ha llenado todos los campos!");
            }
        }else{
            Mensaje("Correo incorrecto");
        }
    }
    else {Mensaje("Verifique que ha llenado todos los campos!");}

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    ImageView iv = (ImageView)findViewById(R.id.imageViewavatar2);
                    iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                }
            } else {
                ImageView iv = (ImageView)findViewById(R.id.imageViewavatar2);
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
                ImageView iv = (ImageView)findViewById(R.id.imageViewavatar2);
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
