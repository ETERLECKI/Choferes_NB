package ar.com.nbcargo.nbcargo_choferes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<ItemObject> itemList;
    private Context context;
    private View layoutView;
    private Integer ocupacionCarril;
    public String url;
    private String fechaCal;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public RecyclerViewAdapter(Context context, List<ItemObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comb, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        ocupacionCarril = 0;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RecyclerViewAdapter.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {


        holder.hora_txt.setText((itemList.get(position).getHora()));
        holder.disponible1.setText(itemList.get(position).getHora());
        holder.disponible2.setText(itemList.get(position).getHora());

        holder.carril1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generaTurno("1", holder.disponible1.getText().toString(), "Terlecki", "NUX099");
                // Alarma a las 8:30 a.m.
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 30);
            }
        });
        holder.carril1_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemList.get(position).getEstado1() == "r")
                    Toast.makeText(context, "Llamar a quien reservó carril 1", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        holder.carril2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generaTurno("2", holder.disponible2.getText().toString(), "Terlecki", "NUX099");
            }
        });
        holder.carril2_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemList.get(position).getEstado2() == "r")
                    Toast.makeText(context, "Llamar a quien reservó carril 2", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if (itemList.get(position).getCarril1() != "") {
            ocupacionCarril++;
            Log.d("Tag2", "Valor de getCarril1: " + itemList.get(position).getCarril1());
            switch (itemList.get(position).getCarril1()) {
                case "1": {
                    Log.d("Tag2", "Entro a GetCarril1 carril 1" + " Hora:" + itemList.get(position).getHora().toString());
                    holder.chofer1.setText(itemList.get(position).getChofer1());
                    holder.t_patente11.setText(itemList.get(position).getunidad1());
                    if (itemList.get(position).getunidad1().length() == 6) {
                        holder.i_patente1.setImageResource(R.drawable.patentevieja);
                        holder.t_patente11.setTextColor(Color.parseColor("#ffffff"));
                    }
                    holder.patente1.setVisibility(View.VISIBLE);
                    holder.disponible1.setVisibility(View.GONE);
                    holder.carril1_card.setCardBackgroundColor(Color.parseColor("#fff590"));
                    //Si el turno está confirmado inhabilito el click
                    if (itemList.get(position).getEstado1() == "c") {
                        holder.carril1_card.setClickable(false);
                    }

                }
                break;

                case "2": {
                    Log.d("Tag2", "Entro a GetCarril1 carril 2");
                    holder.chofer2.setText(itemList.get(position).getChofer1());
                    holder.t_patente12.setText(itemList.get(position).getunidad1());
                    if (itemList.get(position).getunidad2().length() == 6) {
                        holder.i_patente2.setImageResource(R.drawable.patentevieja);
                        holder.t_patente12.setTextColor(Color.parseColor("#ffffff"));
                    }
                    holder.patente2.setVisibility(View.VISIBLE);
                    holder.disponible2.setVisibility(View.GONE);
                    holder.carril2_card.setClickable(false);
                    holder.carril2_card.setCardBackgroundColor(Color.parseColor("#ef9a9a"));
                    //Si el turno está confirmado inhabilito el click
                    if (itemList.get(position).getEstado1() == "c") {
                        holder.carril1_card.setClickable(false);
                    }
                    break;
                }

            }
        }
        if (itemList.get(position).getCarril2() != "") {
            ocupacionCarril++;
            Log.d("Tag2", "Valor de getCarril2: " + itemList.get(position).getCarril1());
            switch (itemList.get(position).getCarril2()) {
                case "1": {
                    Log.d("Tag2", "Entro a GetCarril2 carril 1" + " Hora:" + itemList.get(position).getHora().toString());
                    holder.chofer1.setText(itemList.get(position).getChofer2());
                    holder.t_patente11.setText(itemList.get(position).getunidad2());
                    if (itemList.get(position).getunidad1().length() == 6) {
                        holder.i_patente1.setImageResource(R.drawable.patentevieja);
                        holder.t_patente11.setTextColor(Color.parseColor("#ffffff"));
                    }
                    holder.patente1.setVisibility(View.VISIBLE);
                    holder.disponible1.setVisibility(View.GONE);
                    holder.carril1_card.setClickable(false);
                    holder.carril1_card.setCardBackgroundColor(Color.parseColor("#fff590"));
                    //Si el turno está confirmado inhabilito el click
                    if (itemList.get(position).getEstado2() == "c") {
                        holder.carril2_card.setClickable(false);
                    }
                    break;
                }

                case "2": {
                    Log.d("Tag2", "Entro a GetCarril2 carril 2");
                    holder.chofer2.setText(itemList.get(position).getChofer2());
                    holder.t_patente12.setText(itemList.get(position).getunidad2());
                    if (itemList.get(position).getunidad2().length() == 6) {
                        holder.i_patente2.setImageResource(R.drawable.patentevieja);
                        holder.t_patente12.setTextColor(Color.parseColor("#ffffff"));
                    }
                    holder.patente2.setVisibility(View.VISIBLE);
                    holder.disponible2.setVisibility(View.GONE);
                    holder.carril2_card.setClickable(false);
                    holder.carril2_card.setCardBackgroundColor(Color.parseColor("#ef9a9a"));
                    //Si el turno está confirmado inhabilito el click
                    if (itemList.get(position).getEstado2() == "c") {
                        holder.carril2_card.setClickable(false);
                    }
                    break;
                }
            }
        }

        switch (ocupacionCarril) {
            case 1: {
                holder.hora.setBackgroundColor(Color.parseColor("#fff590"));
                ocupacionCarril = 0;
                break;
            }

            case 2: {
                holder.hora.setBackgroundColor(Color.parseColor("#ef9a9a"));
                ocupacionCarril = 0;
                break;
            }


        }

    }

    public void generaTurno(String carril, String hora, String chofer, String unidad) {

        fechaCal = turnos_combustible.valorCalendario;
        Toast.makeText(context, "Turno para carril " + carril + " el día " + fechaCal + " a las " + hora + " para el chofer " + chofer + " de la unidad " + unidad, Toast.LENGTH_SHORT).show();
        url = "http://192.168.5.199/modifica_turnos.php?tipo=combustible&fecha=" + fechaCal + "&hora=" + hora + "&carril=" + carril + "&chofer=" + chofer + "&unidad=" + unidad + "&estado=r";
        Log.d("Tag2", url);
        url = url.replace(" ", "+");
        url = url.replace("á", "a");
        url = url.replace("é", "e");
        url = url.replace("í", "i");
        url = url.replace("ó", "o");
        url = url.replace("ú", "u");

        Log.d("Tag2", url);

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("Tag2", "Valor de url: " + url);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("success") == 1) {
                        Toast.makeText(context, "Acción guardada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "No se pudo guardar acción", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("TAG2", "Zona de error btn_realizado");
                Toast.makeText(context, "Error al generar consulta", Toast.LENGTH_SHORT).show();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
