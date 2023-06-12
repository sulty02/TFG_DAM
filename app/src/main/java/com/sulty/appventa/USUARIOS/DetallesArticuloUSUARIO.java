package com.sulty.appventa.USUARIOS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sulty.appventa.ADMIN.DetallesArticuloADMIN;
import com.sulty.appventa.ADMIN.EditarArticuloADMIN;
import com.sulty.appventa.ADMIN.PantallaPrincipalADMIN;
import com.sulty.appventa.EnviaCorreo;
import com.sulty.appventa.R;

public class DetallesArticuloUSUARIO extends AppCompatActivity {

    TextView detallesNombre, detallesDescripcion, detallesEstado, detallesPrecio;
    ImageView detallesImagen;
    String key="";
    String imagenUrl="";
    Button bt_correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_articulo_usuario);

        detallesNombre = findViewById(R.id.detallesNombre);
        detallesDescripcion = findViewById(R.id.detallesDescripcion);
        detallesPrecio = findViewById(R.id.detallesPrecio);
        detallesImagen = findViewById(R.id.detallesImagen);
        detallesEstado = findViewById(R.id.detallesEstado);

        bt_correo = findViewById(R.id.bt_correo);

        bt_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetallesArticuloUSUARIO.this, EnviaCorreo.class);
                intent.putExtra("Articulo", detallesNombre.getText().toString());
                intent.putExtra("Precio", detallesPrecio.getText().toString());
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            detallesDescripcion.setText(bundle.getString("Descripcion"));
            detallesNombre.setText(bundle.getString("Nombre"));
            detallesPrecio.setText(bundle.getString("Precio"));
            detallesEstado .setText(bundle.getString("Estado"));
            key = bundle.getString("Key");
            imagenUrl = bundle.getString("Imagen");
            Glide.with(this).load(bundle.getString("Imagen")).into(detallesImagen);
        }

    }
}