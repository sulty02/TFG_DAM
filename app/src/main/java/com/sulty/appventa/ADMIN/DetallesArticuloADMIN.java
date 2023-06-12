package com.sulty.appventa.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.sulty.appventa.R;

public class DetallesArticuloADMIN extends AppCompatActivity {

    TextView detallesNombre, detallesDescripcion, detallesEstado, detallesPrecio;
    ImageView detallesImagen;
    FloatingActionButton bt_borrar, bt_editar;
    String key="";
    String imagenUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallesarticuloadmin);

        detallesNombre = findViewById(R.id.detallesNombre);
        detallesDescripcion = findViewById(R.id.detallesDescripcion);
        detallesPrecio = findViewById(R.id.detallesPrecio);
        detallesImagen = findViewById(R.id.detallesImagen);
        detallesEstado = findViewById(R.id.detallesEstado);
        bt_borrar = findViewById(R.id.bt_borrar);
        bt_editar = findViewById(R.id.bt_editar);

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

        bt_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PRODUCTOS");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imagenUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetallesArticuloADMIN.this, "BORRADO", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), PantallaPrincipalADMIN.class));
                        finish();
                    }
                });
            }
        });

        bt_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetallesArticuloADMIN.this, EditarArticuloADMIN.class)
                        .putExtra("Nombre", detallesNombre.getText().toString())
                        .putExtra("Descripcion", detallesDescripcion.getText().toString())
                        .putExtra("Estado", detallesEstado.getText().toString())
                        .putExtra("Precio", detallesPrecio.getText().toString())
                        .putExtra("Imagen", imagenUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
    }
}