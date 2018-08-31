package ar.com.nbcargo.nbcargo_choferes;

import android.content.Context;
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

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerview_comb;
    private LinearLayoutManager layoutManager;
    private Context context;


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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerview_comb = findViewById(R.id.recycler_comb);
        recyclerview_comb.setLayoutManager(layoutManager);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        requestJsonObject("combustible");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void requestJsonObject(String consulta) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.5.199/arma_horario.php?consulta=" + consulta + "&fecha=" + turnos_combustible.valorCalendario;
        Log.d("Tag2", "Pagina: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

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
