package ar.com.nbcargo.nbcargo_choferes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferencias;
    SharedPreferences.Editor upreferencias;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerview_comb;
    private LinearLayoutManager layoutManager;
    private Context context;
    public String Chofer1;
    public String Unidad1;

/*    public MainActivity(Fragment frag) {
        this.frag = frag;
    }

    public Fragment frag;*/


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_combustible:
                    //((turnos_combustible)frag).requestJsonObject("combustible");
                    //requestJsonObject("combustible");
                    return true;
                case R.id.navigation_dashboard:
                    //((turnos_combustible)frag).requestJsonObject("lavadero");
                    //requestJsonObject("lavadero");
                    return true;
                case R.id.navigation_novedades:
                    Intent i = new Intent(getApplicationContext(), anuncio_combustible.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferencias = getSharedPreferences("MisPreferencias", getApplicationContext().MODE_PRIVATE);
        upreferencias = preferencias.edit();
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerview_comb = findViewById(R.id.recycler_comb);
        recyclerview_comb.setLayoutManager(layoutManager);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menuNavigation = navigation.getMenu();
        MenuItem mnuNovedades = menuNavigation.findItem(R.id.navigation_novedades);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Chofer1 = preferencias.getString("nombre", "Error nombre");
        Unidad1 = preferencias.getString("unidad", "Error unidad");
        if (preferencias.getString("dni", "").equals("92722133")) {
            mnuNovedades.setEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnu_cerrarSesion) {
            upreferencias.putString("sesion", "cerrada");
            upreferencias.commit();
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);

        }

        return true;
    }

}
