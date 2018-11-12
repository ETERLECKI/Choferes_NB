package ar.com.nbcargo.nbcargo_choferes;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class anuncio_combustible extends AppCompatActivity {

    //ImageView para mostrar la imagen seleccionada
    ImageView imageView;

    //edittext para obtener odometro
    EditText editTextTags;

    private String unidad;
    private String nombreArchivo;
    public Bitmap photo;
    private Button imagenBTN;
    private Button uploadBTN;
    private LinearLayoutCompat.LayoutParams params;
    public SharedPreferences preferencias;

    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_combustible);

        preferencias = getSharedPreferences("MisPreferencias", getApplicationContext().MODE_PRIVATE);

        imageView = (ImageView) findViewById(R.id.imageView);
        editTextTags = (EditText) findViewById(R.id.editTextTags);
        unidad = preferencias.getString("unidad", "null");
        imagenBTN = (Button) findViewById(R.id.buttonUploadImage);
        uploadBTN= (Button) findViewById(R.id.buttonAnuncio);
        params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,0);


        //chequea permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        imagenBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the tags edittext is empty
                //we will throw input error
                if (editTextTags.getText().toString().trim().isEmpty()) {
                    editTextTags.setError("Por favor ingrese primero el odómetro");
                    editTextTags.requestFocus();
                    return;
                }

                //si todo está bien toma la foto
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        });

        uploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadBitmap(photo);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            uploadBTN.setEnabled(true);

            //uploadBitmap(photo);
        }

    }


    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        Date date = new Date();
        CharSequence df = android.text.format.DateFormat.format("yy-MM-dd hhmmss", date.getTime());
        nombreArchivo=df.toString()+ " " + unidad;

        //getting the tag from the edittext
        final String tags = editTextTags.getText().toString().trim();

        //our custom volley request

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {

                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = nombreArchivo;
                params.put("pic", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
