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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sulty.appventa.ADAPTER.Articulo;
import com.sulty.appventa.R;

import java.text.DateFormat;
import java.util.Calendar;

public class SubirArticuloADMIN extends AppCompatActivity {

    ImageView imagen;
    Button bt_guardar;
    EditText nombre, descripcion, estado, precio;
    String urlImagen;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subirarticuloadmin);

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
                            Intent datos = result.getData();
                            uri = datos.getData();
                            imagen.setImageURI(uri);
                        }else {
                            Toast.makeText(SubirArticuloADMIN.this, "No se ha subido la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                );

            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent foto = new Intent(Intent.ACTION_PICK);
                    foto.setType("image/*");
                    activityResultLauncher.launch(foto);
                }
            });

            bt_guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    guardarDatos();
                }
            });
    }

    public void guardarDatos(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder= new AlertDialog.Builder(SubirArticuloADMIN.this);
        builder .setCancelable(false);
        builder.setView(R.layout.progress_bar);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uriImage2 = uriTask.getResult();
                urlImagen = uriImage2.toString();
                subirDatos();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SubirArticuloADMIN.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void subirDatos(){
        String sNombre = nombre.getText().toString();
        String sEstado = estado.getText().toString();
        String sDescripcion = descripcion.getText().toString();
        String sPrecio = precio.getText().toString().concat("€");

        Articulo articulo = new Articulo(sNombre, sDescripcion, sEstado, sPrecio, urlImagen);

        String currentData = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        int num = (int)(Math.random()*1000+1);
        String numero = Integer.toString(num);

        FirebaseDatabase.getInstance().getReference("PRODUCTOS").child(numero)
                .setValue(articulo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SubirArticuloADMIN.this, "GUARDADO", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SubirArticuloADMIN.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}