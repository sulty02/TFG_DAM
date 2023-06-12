package com.sulty.appventa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sulty.appventa.USUARIOS.PantallaPrincipalUSUARIO;

public class EnviaCorreo extends AppCompatActivity {

    Button bt_contactar;
    EditText nombre, mensaje, precio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envia_correo);

        bt_contactar = findViewById(R.id.bt_contactar);
        nombre = findViewById(R.id.nombre);
        mensaje = findViewById(R.id.mensaje);
        precio = findViewById(R.id.precio);

        Bundle bundle = getIntent().getExtras();
        nombre.setText(bundle.getString("Articulo"));
        precio.setText(bundle.getString("Precio"));

        bt_contactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enviarasunto = nombre.getText().toString();
                String enviarmensaje = mensaje.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { "m.sulty02@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, enviarasunto);
                intent.putExtra(Intent.EXTRA_TEXT, enviarmensaje);

                intent.setType("message/rfc822");

                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Elije un cliente de Correo:"));

                mensaje.setText("");
            }


        });


    }
}