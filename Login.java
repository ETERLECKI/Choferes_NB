package ar.com.nbcargo.nbcargo_choferes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    public SharedPreferences preferencias;
    public SharedPreferences.Editor upreferencias;
    String sesion;
    CheckBox estadoSesion;
    Button btnIngreso;
    Button comprobar_dni;
    EditText EditDNI;
    EditText EditUsuario;
    AutoCompleteTextView patente;
    ArrayList<String> itemsUnidad;
    String URLunidad;
    String usuario;

    @Override
    protected void onRestart() {
        super.onRestart();
        preferencias = getSharedPreferences("MisPreferencias", getApplicationContext().MODE_PRIVATE);
        sesion = preferencias.getString("sesion", "cerrada");
        upreferencias = preferencias.edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferencias = getSharedPreferences("MisPreferencias", getApplicationContext().MODE_PRIVATE);
        sesion = preferencias.getString("sesion", "cerrada");
        upreferencias = preferencias.edit();
        getSupportActionBar().hide();
        patente = findViewById(R.id.log_patente);
        patente.setThreshold(1);
        itemsUnidad = new ArrayList<>();
        URLunidad = "http://192.168.5.199/qunidades.php";
        estadoSesion = findViewById(R.id.log_chk_sesion);
        btnIngreso = findViewById(R.id.log_btn_in);
        comprobar_dni = findViewById(R.id.log_btn_dni);
        EditUsuario =findViewById(R.id.log_nombre);
        EditDNI = findViewById(R.id.log_dni);

        Loadunidad(URLunidad);

        comprobar_dni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarDni();
            }
        });

        btnIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresar();
            }
        });


        if (sesion.equals("abierta")) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void Loadunidad(String urLunidad) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urLunidad, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("success") == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Unidad");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String unidad = jsonObject1.getString("Patente");
                            itemsUnidad.add(unidad);
                        }
                    }
                    patente.setAdapter(new ArrayAdapter<String>(Login.this, android.R.layout.simple_list_item_1, itemsUnidad));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void comprobarDni() {

    }

    private void ingresar() {

        if (EditUsuario.getText().toString() !=""){
            Toast.makeText(getApplicationContext(), "Acceso correcto", Toast.LENGTH_LONG).show();
            upreferencias.putString("dni", EditDNI.getText().toString());
            upreferencias.putString("nombre", EditUsuario.getText().toString());
            startActivity(new Intent(Login.this, MainActivity.class));
            if (estadoSesion.isChecked()) {
                upreferencias.putString("sesion", "abierta");
            }
            upreferencias.commit();
        } else {
            Toast.makeText(getApplicationContext(), "Por favor compruebe que el número de documento sea el correcto", Toast.LENGTH_LONG).show();
        }



    }

}
