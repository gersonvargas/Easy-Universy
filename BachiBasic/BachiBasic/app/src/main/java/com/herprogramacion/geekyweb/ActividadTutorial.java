package com.herprogramacion.geekyweb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActividadTutorial extends Base
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_tutorial);
/*
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));*/
        populateCarList();
        populateListView();
        registerClickCallback();
                ImageView imgv = (ImageView) findViewById(R.id.imageViewbachi);
        Intent intento = new Intent(getApplicationContext(), Main.class);
        onclickImagenCambiarVista(imgv, intento);
    }
    class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,
                                                String url) {
            return (false);
        }

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
            View rootView = inflater.inflate(R.layout.fragment_actividad_tutorial, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ActividadTutorial) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    private List<Video> myCars = new ArrayList<Video>();

    private void populateCarList() {
        myCars.add(new Video("Estudios Sociales", "https://www.youtube.com/watch?v=N_ZbTG7AYGU&list=PLERIktx0fWx5--ts3vSfpPjj2QM8XCVN9",R.drawable.tierra));
        myCars.add(new Video("Matemáticas",  "https://www.youtube.com/watch?v=KKzFzR0qFOo",R.drawable.mate));
        myCars.add(new Video("Educación Cívica",  "https://www.youtube.com/watch?v=5QURzx5m-hI&list=PLHSMBZFwrC9Z35uAnsX4OHGcDGrZVoshG",R.drawable.civica));
        myCars.add(new Video("Biología",  "https://www.youtube.com/watch?v=n8lxbZCE8t4&list=PLHSMBZFwrC9ZiP5mIexUTtKxqSfi1JjCd",R.drawable.biologia));
        myCars.add(new Video("Español",  "https://www.youtube.com/watch?v=bGxhZfvAbLw&list=PL5WRVdz1U50UUvLTZaNcTwnnrjINx3iRK",R.drawable.espanol));
        myCars.add(new Video("Inglés",  "https://www.youtube.com/watch?v=dkgjfI_ezkc",R.drawable.ingles));
    }

    private void populateListView() {
        ArrayAdapter<Video> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.cars_listView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.cars_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Video clickedCar = myCars.get(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedCar.getDireccion())));

            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Video> {
        public MyListAdapter()
        {
            super(ActividadTutorial.this, R.layout.misvideos, myCars);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.misvideos, parent, false);
            }

            // Find the car to work with.
            Video currentCar = myCars.get(position);

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.imageViewCarro);
            imageView.setImageResource(currentCar.getIconID());
            // Make:
            TextView makeText = (TextView) itemView.findViewById(R.id.textViewFabricante);
            makeText.setText("Materia: "+currentCar.getNombre());
            // Year:
           // TextView yearText = (TextView) itemView.findViewById(R.id.textViewYear);
           // yearText.setText("" + currentCar.getDireccion());

            return itemView;
        }
    }

    public void Mensaje(String msg){Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();};
}
