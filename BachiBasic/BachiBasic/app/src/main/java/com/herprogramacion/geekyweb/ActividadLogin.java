package com.herprogramacion.geekyweb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
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

public class ActividadLogin extends Base
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
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
                    if (ObtenerUsuario(Mi_textview.getText().toString(),Mi_textview2.getText().toString())) {

                    if (valido) {
                        vg.setSessionpass(Mi_textview2.getText().toString());
                        vg.setSessionemail(Mi_textview.getText().toString());

                        Intent intento = new Intent(getApplicationContext(), ActividadPreguntas.class);
                        startActivity(intento);
                    } else
                        Mensaje("Debe elegir una Materia.");
                }else{
                    Mensaje("No se ha encontrado el usuario!");
                }
            }
        });

        ImageView imgv = (ImageView) findViewById(R.id.imageViewbachi);
        Intent intento = new Intent(getApplicationContext(), Main.class);
        onclickImagenCambiarVista(imgv, intento);
        CargarSpinner();
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
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
    private void CargarSpinner() {
        Spinner s1;
        final String[] presidents = {
                "Elija la materia",
                "Biología",
                "Matemáticas",
                "Español",
                "Inglés",
                "Cívica",
                "Est. Sociales"
        };
        //---Spinner View---
        s1 = (Spinner) findViewById(R.id.spinnermateria);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, presidents);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position != 0) {
                    VariablesGlobales.getInstance().setTipoTest(position);
                    valido = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s1.setAdapter(adapter);


    }

}
