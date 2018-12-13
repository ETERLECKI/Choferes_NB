package ar.com.nbcargo.nbcargo_choferes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Principal extends AppCompatActivity {

    CardView turnos;
    CardView capacitaciones;
    CardView cargaRuta;
    CardView novedades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        turnos = findViewById(R.id.principal_turnos_card);
        capacitaciones = findViewById(R.id.principal_capacitacion_card);
        cargaRuta = findViewById(R.id.principal_cargaRuta_card);
        novedades = findViewById(R.id.principal_novedades_card);
        getSupportActionBar().hide();

        turnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        capacitaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mensajeNC();
            }
        });

        cargaRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ypf_en_ruta.class));
            }
        });

        novedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Capacitacion.class));
            }
        });


    }

    public AlertDialog mensajeNC() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        builder.setTitle("Notificación")
                .setMessage("Esta función no se encuentra aún disponible, en breve podrá aprovechar los usos de la misma")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

        return builder.create();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
}
