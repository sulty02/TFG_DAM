package com.sulty.appventa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sulty.appventa.ADMIN.PantallaPrincipalADMIN;
import com.sulty.appventa.LOGIN.InicioSesionUSUARIOS;
import com.sulty.appventa.LOGIN.Registro;
import com.sulty.appventa.USUARIOS.PantallaPrincipalUSUARIO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InicioSesionADMIN extends AppCompatActivity {

    EditText email, contrasena;
    String mail, pass;
    Button bt_iniciar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion_admin);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        contrasena = findViewById(R.id.contrasena);
        bt_iniciar = findViewById(R.id.bt_iniciar);

        bt_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = email.getText().toString().trim();
                pass = contrasena.getText().toString().trim();


                if (mail.equals("sulty@gmail.com") && pass.equals("mohamedadmin")) {
                    firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(InicioSesionADMIN.this, "INICIANDO SESIÓN", Toast.LENGTH_SHORT).show();
                                irHome();
                            } else {
                                Toast.makeText(InicioSesionADMIN.this, "CREDENCIALES INCORRECTAS", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(InicioSesionADMIN.this, "NO ERES BIENVENIDO", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(InicioSesionADMIN.this, InicioSesionUSUARIOS.class));
                }
            }

        });

    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            irHome();
        }
    }

    private void irHome() {
        Intent intent = new Intent(InicioSesionADMIN.this, PantallaPrincipalADMIN.class);
        startActivity(intent);
        finish();
    }

    private boolean emailValido(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}