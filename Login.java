package ar.com.nbcargo.nbcargo_choferes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    Button btnIngreso;
    Button comprobar_dni;
    Button unidad_btn;
    EditText EditDNI;
    EditText EditUsuario;
    AutoCompleteTextView patente;
    ArrayList<String> itemsUnidad;
    String URLusuario;
    String usuario;
    int errorConexion;
    InputMethodManager imm;


    @Override
    protected void onRestart() {
        super.onRestart();
        preferencias = getSharedPreferences("MisPreferencias", getApplicationContext().MODE_PRIVATE);
        sesion = preferencias.getString("sesion", "cerrada");
        upreferencias = preferencias.edit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
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
        URLusuario = EndPoints.ROOT_URL + "login_choferes.php";
        btnIngreso = findViewById(R.id.log_btn_in);
        comprobar_dni = findViewById(R.id.log_btn_dni);
        unidad_btn=findViewById(R.id.log_btn_unidad);
        EditUsuario = findViewById(R.id.log_nombre);
        EditDNI = findViewById(R.id.log_dni);
        errorConexion = 0;

        EditDNI.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Loadunidad();

        comprobar_dni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorConexion = 0;
                comprobarDni();
            }
        });

        btnIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresar();
            }
        });

        unidad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(EditDNI.getWindowToken(), 0);
            }
        });

        if (sesion.equals("abierta")) {
            startActivity(new Intent(this, Principal.class));
        }
    }

    private void Loadunidad() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.ROOT_URL + "qunidades.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Tag2", response);

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
                    Log.d("Tag1", "error en catch");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag1", "Error volley");
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void comprobarDni() {
        imm.hideSoftInputFromWindow(EditDNI.getWindowToken(), 0);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.ROOT_URL + "dni.php" + "?dni=" + EditDNI.getText().toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("success") == 1) {
                        String DNI = jsonObject.getString("nombre");
                        Log.d("Tag2", "nombre usuario:" + DNI);
                        EditUsuario.setText(DNI);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Tag2", "Entro a exception");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag2",EndPoints.ROOT_URL + "dni.php" + "?dni=" + EditDNI.getText().toString());
                errorConexion = errorConexion + 1;
                if (errorConexion > 5) {
                    Toast.makeText(Login.this, "No se puede conectar a la base de datos, por favor verifique su conexión a internet", Toast.LENGTH_LONG).show();
                } else {
                    if (EndPoints.ROOT_URL.equals("http://192.168.5.14/")) {
                        EndPoints.ROOT_URL = "http://143.0.245.9:49999/";
                        upreferencias.putString("url", "http://143.0.245.9:49999/");
                    } else {
                        EndPoints.ROOT_URL = "http://192.168.5.14/";
                        upreferencias.putString("url", "http://192.168.5.14/");
                    }
                    upreferencias.commit();
                    comprobarDni();
                }
            }
        });
        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void ingresar() {

        if (!EditUsuario.getText().toString().equals("")) {
            if (!patente.getText().toString().equals("")) {
                Toast.makeText(this, "Acceso correcto", Toast.LENGTH_LONG).show();
                upreferencias.putString("dni", EditDNI.getText().toString());
                upreferencias.putString("nombre", EditUsuario.getText().toString());
                upreferencias.putString("unidad", patente.getText().toString());
                startActivity(new Intent(this, Principal.class));
                    upreferencias.putString("sesion", "abierta");
                upreferencias.commit();
                //guardarUsuario();
            } else {
                Toast.makeText(this, "Error de patente", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "No se ha comprobado su DNI o no existe una persona asociada al mismo", Toast.LENGTH_LONG).show();
        }


    }

    private void guardarUsuario() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.ROOT_URL + "dni.php" + "?dni=" + EditDNI.getText().toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tag2", "Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("success") == 1) {
                        Log.d("Tag2", "Entro al success");
                        //JSONArray jsonArray = jsonObject.getJSONArray("Unidad");
                        //for (int i = 0; i < jsonArray.length(); i++) {
                        //JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String DNI = jsonObject.getString("nombre");
                        Log.d("Tag2", "nombre usuario:" + DNI);
                        EditUsuario.setText(DNI);
                        //itemsUnidad.add(unidad);
                        //}
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Tag2", "Entro a exception");
                    Log.d("Tag2", "error: " + e.getMessage().toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Tag2", "Entro a error");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

}
