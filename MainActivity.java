package ar.com.nbcargo.nbcargo_choferes;

import android.content.Context;
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


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_combustible:
                    requestJsonObject("combustible");
                    return true;
                case R.id.navigation_dashboard:
                    requestJsonObject("lavadero");
                    return true;
                case R.id.navigation_notifications:

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
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerview_comb = findViewById(R.id.recycler_comb);
        recyclerview_comb.setLayoutManager(layoutManager);
        Log.d("Tag3", "Llegue hasta acá");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Chofer1 = preferencias.getString("nombre", "Error nombre");
        Unidad1 = preferencias.getString("unidad", "Error unidad");
        Log.d("Tag4", "Nombre: " + Chofer1);
        Log.d("Tag4", "Unidad: " + Unidad1);

        requestJsonObject("combustible");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Tag3", "Llegue hasta acá 2");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnu_cerrarSesion) {
            upreferencias.putString("sesion", "cerrada");
        }



        return true;
    }*/

    public void requestJsonObject(String consulta) {

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.5.199/arma_horario.php?consulta=" + consulta + "&fecha=" + turnos_combustible.valorCalendario;
        Log.d("Tag2", "Pagina: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag3", "Llegue hasta acá 3");
                if (response.length() != 6) {
                    Log.d("Tag2", "response: " + response);
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    List<ItemObject> posts = new ArrayList<ItemObject>();
                    posts.clear();
                    posts = Arrays.asList(mGson.fromJson(response, ItemObject[].class));
                    adapter = new RecyclerViewAdapter(MainActivity.this, posts);
                    recyclerview_comb.setAdapter(adapter);
                    //getSupportActionBar().setSubtitle("");
                    //getSupportActionBar().setSubtitle(subTituloA + " (" + String.valueOf(adapter.getItemCount()) + ")");
                    Log.d("Tag3", "Llegue hasta acá 4");

                } else {
                    Toast.makeText(MainActivity.this, "Actualmente no hay novedades a mostrar de este tipo", Toast.LENGTH_LONG).show();
                    //getSupportActionBar().setSubtitle(subTituloA + " (0)");
                    recyclerview_comb.setVisibility(View.GONE);
                }


            }

        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag2", "Error " + error.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

}
