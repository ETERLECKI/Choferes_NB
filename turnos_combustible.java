package ar.com.nbcargo.nbcargo_choferes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class turnos_combustible extends Fragment {
    @Override
    public void onResume() {
        requestJsonObject("combustible");
        super.onResume();
    }

    public HorizontalCalendar calendario_turnos;
    public static String valorCalendario = "";
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerview_comb;
    SwipeRefreshLayout swipeRefreshLayout;
    int errorConexion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_turnos_combustible, container, false);


        /** end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 6);
        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        calendario_turnos = new HorizontalCalendar.Builder(rootView, R.id.calendarView_turnos)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(startDate.getTime());
        valorCalendario = formattedDate;

        calendario_turnos.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = formatter.format(date.getTime());
                valorCalendario = formattedDate.toString();
                requestJsonObject("combustible");
            }
        });

        swipeRefreshLayout = rootView.findViewById(R.id.combustible_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                requestJsonObject("combustible");
            }
        });

        return rootView;


    }

    public void requestJsonObject(String consulta) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = EndPoints.ROOT_URL + "arma_horario.php?consulta=" + consulta + "&fecha=" + turnos_combustible.valorCalendario;
        Log.d("Tag2", "Pagina: " + url);
        recyclerview_comb = getActivity().findViewById(R.id.recycler_comb);

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
                    adapter = new RecyclerViewAdapter(getContext(), posts,turnos_combustible.this);

                    recyclerview_comb.setAdapter(adapter);

                    //getSupportActionBar().setSubtitle("");
                    //getSupportActionBar().setSubtitle(subTituloA + " (" + String.valueOf(adapter.getItemCount()) + ")");

                } else {
                    Toast.makeText(getContext(), "Actualmente no hay novedades a mostrar de este tipo", Toast.LENGTH_LONG).show();
                    //getSupportActionBar().setSubtitle(subTituloA + " (0)");
                    recyclerview_comb.setVisibility(View.GONE);
                }


            }

        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag2", "Error " + error.getMessage());
                errorConexion = errorConexion + 1;
                if (errorConexion > 5) {
                    Toast.makeText(getContext(), "No se puede conectar a la base de datos, por favor verifique su conexión a internet", Toast.LENGTH_LONG).show();
                } else {
                    if (EndPoints.ROOT_URL.equals("http://192.168.5.14/")) {
                        EndPoints.ROOT_URL = "http://143.0.245.9:49999/";
                    } else {
                        EndPoints.ROOT_URL = "http://192.168.5.14/";
                    }
                    requestJsonObject("combustible");
                }
            }
        });
        queue.add(stringRequest);
    }

}