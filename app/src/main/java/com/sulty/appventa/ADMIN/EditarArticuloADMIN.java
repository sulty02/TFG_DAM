package com.sulty.appventa.ADMIN;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sulty.appventa.ADAPTER.Articulo;
import com.sulty.appventa.R;

public class EditarArticuloADMIN extends AppCompatActivity {

    ImageView imagen;
    Button bt_guardar;
    EditText nombre, descripcion, estado, precio;
    String sNombre, sDescripcion, sEstado, sPrecio;
    String imagenUrl;
    String key, oldImagenUrl;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editararticuloadmin);

        imagen = findViewById(R.id.imagen);
        bt_guardar = findViewById(R.id.bt_guardar);
        nombre = findViewById(R.id.nombre);
        descripcion = findViewById(R.id.descripcion);
        estado = findViewById(R.id.estado);
        precio = findViewById(R.id.precio);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            imagen.setImageURI(uri);
                        }else {
                            Toast.makeText(EditarArticuloADMIN.this, "Ninguna imagen seleccionada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            Glide.with(EditarArticuloADMIN.this).load(bundle.getString("Imagen")).into(imagen);
            nombre.setText(bundle.getString("Nombre"));
            descripcion.setText(bundle.getString("Descripcion"));
            estado.setText(bundle.getString("Estado"));
            precio.setText(bundle.getString("Precio"));
            key = bundle.getString("Key");
            oldImagenUrl = bundle.getString("Imagen");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("PRODUCTOS").child(key);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photo = new Intent(Intent.ACTION_PICK);
                photo.setType("image/*");
                activityResultLauncher.launch(photo);
            }
        });
        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent(EditarArticuloADMIN.this, PantallaPrincipalADMIN.class);
                startActivity(intent);
            }
        });
    }

    public void guardarDatos(){
        storageReference = FirebaseStorage.getInstance().getReference().child("PRODUCTOS").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(EditarArticuloADMIN.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_bar);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uriImagen = uriTask.getResult();
                imagenUrl = uriImagen.toString();
                actualizarDatos();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

    }
    public void actualizarDatos(){
        sNombre = nombre.getText().toString().trim();
        sDescripcion = descripcion.getText().toString().trim();
        sEstado = estado.getText().toString().trim();
        sPrecio = precio.getText().toString().trim().concat("€");

        Articulo articulo = new Articulo(sNombre, sDescripcion, sEstado, sPrecio, imagenUrl);

        databaseReference.setValue(articulo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImagenUrl);
                    reference.delete();
                    Toast.makeText(EditarArticuloADMIN.this, "ACTUALIZADO", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarArticuloADMIN.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}