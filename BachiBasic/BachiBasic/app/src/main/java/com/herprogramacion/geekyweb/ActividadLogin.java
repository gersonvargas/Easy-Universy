package com.herprogramacion.geekyweb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.herprogramacion.geekyweb.tools.Constantes;
import com.herprogramacion.geekyweb.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActividadLogin extends Base
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    private boolean valido=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_login);
        CambiarColorFondoActivity(Color.GRAY);

        VariablesGlobales vg=VariablesGlobales.getInstance();
        if(!vg.getSessionemail().equals("")&&!vg.getSessionpass().equals("")) {
            TextView Mi_textview = (TextView) findViewById(R.id.loginemail);
            TextView Mi_textview2 = (TextView) findViewById(R.id.loginpassword);

            Mi_textview.setText(vg.getSessionemail());
            Mi_textview2.setText(vg.getSessionpass());
        }
        Button miboton= (Button) findViewById(R.id.btniniciar);
        miboton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {
                CrearBD();
                TextView Mi_textview = (TextView) findViewById(R.id.loginemail);
                TextView Mi_textview2 = (TextView) findViewById(R.id.loginpassword);
                //Mensaje(Mi_textview.getText().toString());
              VariablesGlobales vg=VariablesGlobales.getInstance();
                if(vg.getSessionemail().equals("")&&vg.getSessionpass().equals("")) {
                    vg.setSessionpass(Mi_textview2.getText().toString());
                    vg.setSessionemail(Mi_textview.getText().toString());
                }
                  //  if (ObtenerUsuario(Mi_textview.getText().toString(),Mi_textview2.getText().toString())) {
                     //   vg.setSessionpass(Mi_textview2.getText().toString());
                      //  vg.setSessionemail(Mi_textview.getText().toString());
                    new ObtenerWebService().execute(Mi_textview.getText().toString(),Mi_textview2.getText().toString());
                       // Intent intento = new Intent(getApplicationContext(), Actividad_Elegir_test.class);
                       // startActivity(intento);
                //}else{
                 //   Mensaje("No se ha encontrado el usuario!");
               // }
            }
        });

        ImageView imgv = (ImageView) findViewById(R.id.imageViewbachi);
        Intent intento = new Intent(getApplicationContext(), Main.class);
        onclickImagenCambiarVista(imgv, intento);
        //CargarSpinner();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_actividad_login, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ActividadLogin) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public class ObtenerWebService extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String devuelve = "";
            URL url = null; // Url de donde queremos obtener información
            try {
                url = new URL(Constantes.GET+"/proyecto1/obtener_usuario_por_id.php?email="+params[0]+"&pass="+params[1]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);        // Paso toda la entrada al StringBuilder
                    }
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    String email= respuestaJSON.getString("estado");
                    if (email.equals("1")){
                        Intent intento = new Intent(getApplicationContext(), Actividad_Elegir_test.class);
                        startActivity(intento);
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Mensaje("Usuario no encontrado.");
                            }
                        });
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Mensaje("Ha ocurrido un error con la conexión al servidor.");
                        }
                    });
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
    }

}
