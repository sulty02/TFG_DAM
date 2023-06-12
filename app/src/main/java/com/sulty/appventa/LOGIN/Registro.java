package com.sulty.appventa.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sulty.appventa.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    EditText email, contrasena, recontrasena;
    Button bt_iniciar, bt_registrar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        contrasena = findViewById(R.id.contrasena);
        recontrasena = findViewById(R.id.recontrasena);
        bt_iniciar = findViewById(R.id.bt_iniciar);
        bt_registrar = findViewById(R.id.bt_registar);

        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();
                String pass = contrasena.getText().toString().trim();
                String repass = recontrasena.getText().toString();

                if (mail.isEmpty() || pass.isEmpty() || repass.isEmpty()){
                    Toast.makeText(Registro.this, "INGRESE TODOS LOS DATOS", Toast.LENGTH_LONG).show();
                }else{
                    if (emailValido(mail)){
                        if (pass.equals(repass)){
                            if (pass.length()<6){
                                Toast.makeText(Registro.this, "LA CONTRASEÑA DEBE TENER AL MENOS 6 CARACTERES", Toast.LENGTH_LONG).show();
                            }else {
                                firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Registro.this, "REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Registro.this, InicioSesionUSUARIOS.class));
                                            finish();
                                        }else {
                                            Toast.makeText(Registro.this, "ESTA CUENTA YA EXISTE", Toast.LENGTH_LONG).show();
                                            email.setText("");
                                            contrasena.setText("");
                                            recontrasena.setText("");
                                        }
                                    }
                                });
                            }
                        }else {
                            Toast.makeText(Registro.this, "LAS CONTRASEÑAS NO COINCIDEN", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(Registro.this, "EMAIL INVALIDO", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        bt_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registro.this, InicioSesionUSUARIOS.class));
            }
        });
    }

    private boolean emailValido(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}