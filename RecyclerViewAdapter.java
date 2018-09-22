package ar.com.nbcargo.nbcargo_choferes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {


    private List<ItemObject> itemList;
    private Context context;
    private View layoutView;
    private Integer ocupacionCarril;
    public String url;
    public String fechaCal;
    private String chofer1;
    public String unidad1;
    private String message;
    static String confirmacion;
    private String turno;
    private String terror;
    private String carril;
    private String disponible;
    final String[] items = {"Confirmar turno", "Cancelar turno", "Traspasar turno"};
    private String modificar_turno;
    public Fragment frag;


    SharedPreferences preferencias;


    public RecyclerViewAdapter(Context context, List<ItemObject> itemList, Fragment frag) {
        this.itemList = itemList;
        this.context = context;
        this.frag = frag;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Tag2","Entro al adpater");
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comb, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        ocupacionCarril = 0;
        Intent intent = new Intent(context, RecyclerViewAdapter.class);
        preferencias = context.getSharedPreferences("MisPreferencias", context.getApplicationContext().MODE_PRIVATE);
        chofer1 = preferencias.getString("nombre", "error n");
        unidad1 = preferencias.getString("unidad", "error u");

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
                confirmacion = "no";
                carril = "1";
                disponible = holder.disponible1.getText().toString();
                generaTurno(carril, disponible, chofer1, unidad1);
            }
        });

        holder.carril2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmacion = "no";
                carril = "2";
                disponible = holder.disponible1.getText().toString();
                generaTurno(carril, disponible, chofer1, unidad1);
            }
        });
//Si el primer carril (no es el carril 1, es el primer carril reservado) tiene reserva
        if (itemList.get(position).getCarril1() != "") {
            ocupacionCarril++;
            switch (itemList.get(position).getCarril1()) {
                case "1": {
                    holder.chofer1.setText(itemList.get(position).getChofer1());
                    holder.t_patente11.setText(itemList.get(position).getunidad1());
                    if (itemList.get(position).getunidad1().length() == 6) {
                        holder.i_patente1.setImageResource(R.drawable.patentevieja);
                        holder.t_patente11.setTextColor(Color.parseColor("#ffffff"));
                    }
                    holder.patente1.setVisibility(View.VISIBLE);
                    holder.disponible1.setVisibility(View.GONE);
                    //Si el turno está confirmado inhabilito el click y coloco fondo rojo
                    if (itemList.get(position).getEstado1().equals("c")) {
                        holder.carril1_card.setCardBackgroundColor(Color.parseColor("#ef9a9a"));
                        holder.carril1_card.setClickable(false);
                    } else {
                        holder.carril1_card.setCardBackgroundColor(Color.parseColor("#fff590"));
                    }
                    break;
                }


                case "2": {
                    holder.chofer2.setText(itemList.get(position).getChofer1());
                    holder.t_patente12.setText(itemList.get(position).getunidad1());
                    if (itemList.get(position).getunidad2().length() == 6) {
                        holder.i_patente2.setImageResource(R.drawable.patentevieja);
                        holder.t_patente12.setTextColor(Color.parseColor("#ffffff"));
                    }
                    holder.patente2.setVisibility(View.VISIBLE);
                    holder.disponible2.setVisibility(View.GONE);
                    //Si el turno está confirmado inhabilito el click
                    if (itemList.get(position).getEstado1().equals("c")) {
                        holder.carril2_card.setCardBackgroundColor(Color.parseColor("#ef9a9a"));
                        holder.carril2_card.setClickable(false);
                    } else {
                        holder.carril2_card.setCardBackgroundColor(Color.parseColor("#fff590"));
                    }
                    break;
                }

            }
        }
        if (itemList.get(position).getCarril2() != "") {
            ocupacionCarril++;
            switch (itemList.get(position).getCarril2()) {
                case "1": {
                    holder.chofer1.setText(itemList.get(position).getChofer2());
                    holder.t_patente11.setText(itemList.get(position).getunidad2());
                    if (itemList.get(position).getunidad1().length() == 6) {
                        holder.i_patente1.setImageResource(R.drawable.patentevieja);
                        holder.t_patente11.setTextColor(Color.parseColor("#ffffff"));
                    }
                    holder.patente1.setVisibility(View.VISIBLE);
                    holder.disponible1.setVisibility(View.GONE);
                    //Si el turno está confirmado inhabilito el click
                    if (itemList.get(position).getEstado2().equals("c")) {
                        holder.carril1_card.setCardBackgroundColor(Color.parseColor("#ef9a9a"));
                        holder.carril1_card.setClickable(false);
                    } else {
                        holder.carril1_card.setCardBackgroundColor(Color.parseColor("#fff590"));
                    }
                    break;
                }

                case "2": {
                    holder.chofer2.setText(itemList.get(position).getChofer2());
                    holder.t_patente12.setText(itemList.get(position).getunidad2());
                    if (itemList.get(position).getunidad2().length() == 6) {
                        holder.i_patente2.setImageResource(R.drawable.patentevieja);
                        holder.t_patente12.setTextColor(Color.parseColor("#ffffff"));
                    }
                    holder.patente2.setVisibility(View.VISIBLE);
                    holder.disponible2.setVisibility(View.GONE);

                    //Si el turno está confirmado inhabilito el click
                    if (itemList.get(position).getEstado2().equals("c")) {
                        holder.carril2_card.setCardBackgroundColor(Color.parseColor("#ef9a9a"));
                        holder.carril2_card.setClickable(false);
                    } else {
                        holder.carril2_card.setCardBackgroundColor(Color.parseColor("#fff590"));
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

    public void generaTurno(final String carril, final String hora, final String chofer, final String unidad) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        fechaCal = turnos_combustible.valorCalendario;

        url = "http://192.168.5.199/modifica_turnos.php?tipo=combustible&fecha=" + fechaCal + "&hora=" + hora + "&carril=" + carril + "&chofer=" + chofer + "&unidad=" + unidad + "&estado=r&confirmacion=" + confirmacion + "&accion=" + modificar_turno;

        url = url.replace(" ", "+");
        url = url.replace("á", "a");
        url = url.replace("é", "e");
        url = url.replace("í", "i");
        url = url.replace("ó", "o");
        url = url.replace("ú", "u");

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {
                Log.d("Tag2", response);
                try {
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    JSONObject jsonObject = new JSONObject(response);

                    message = jsonObject.getString("message");
                    turno = jsonObject.getString("error");
                    terror = jsonObject.getString("terror");

                    //verifico que no haya turno para el mismo chofer/unidad
                    if (turno.equals("false")) {
                        //si no hay turno pido confirmacion
                        if (terror.equals("sinturno")) {

                            //Genero cuadro de diálogo

                            builder.setTitle("Confirmación de turno")
                                    .setMessage(message);
                            DialogInterface.OnClickListener confirmacionClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            confirmacion = "si";
                                            generaTurno(carril, disponible, chofer1, unidad1);
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:

                                    }
                                }
                            };

                            builder.setPositiveButton("Aceptar", confirmacionClickListener);
                            builder.setNegativeButton("Cancelar", confirmacionClickListener);

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            //Enviar refresh a turnos
                            Log.d("Tag2", "Antes de mandaRefresh");
                            //Log.d("Tag2",((turnos_combustible)frag).onRefresh());
                            ((turnos_combustible)frag).onResume();
                        }

                        //Si ya tiene un turno, pregunto si quiere modificarlo
                    } else {
                        switch (jsonObject.getString("terror")) {
                            //Si selecciona el mismo día que reservó
                            case "reservadoenfecha":

                                builder.setTitle("Modificar Turno")
                                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                switch (items[i]) {
                                                    case "Confirmar turno":
                                                        modificar_turno = "confirma";
                                                        break;

                                                    case "Cancelar turno":
                                                        modificar_turno = "cancela";
                                                        break;

                                                    case "Traspasar turno":
                                                        modificar_turno = "traspasa";
                                                        break;
                                                }
                                            }
                                        })
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                generaTurno(carril, hora, chofer, unidad);
                                            }
                                        })
                                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                AlertDialog dialog1 = builder.create();
                                dialog1.show();

                                break;

                            //Si selecciona otra fecha vacia
                            case "reservadootrafecha":

                                builder.setTitle("Cambiar fecha de reserva")
                                        .setMessage(message)
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                generaTurno(carril, hora, chofer, unidad);
                                            }
                                        })
                                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                                break;
                            //Si selecciona una fecha ocupada por otro
                            case "reservadootro":

                                builder.setTitle("Solicitar cambio de turno")
                                        .setMessage(message)
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                generaTurno(carril, hora, chofer, unidad);
                                            }
                                        })
                                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                AlertDialog dialog2 = builder.create();
                                dialog2.show();
                                break;
                            //Error de conexión
                            case "conexion":
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                } catch (JSONException e) {
                    Log.d("Tag2", "Error en catch:" + e.getMessage().toString());
                    e.printStackTrace();
                }
                confirmacion = "no";
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Tag2", "Zona de error btn_realizado");
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

