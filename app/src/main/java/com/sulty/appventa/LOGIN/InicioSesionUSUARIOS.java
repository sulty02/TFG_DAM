package com.sulty.appventa.LOGIN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.sulty.appventa.InicioSesionADMIN;
import com.sulty.appventa.PantallaCarga;
import com.sulty.appventa.R;
import com.sulty.appventa.USUARIOS.PantallaPrincipalUSUARIO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InicioSesionUSUARIOS extends AppCompatActivity {

    EditText email, contrasena;
    ImageView imagen;
    String mail, pass;
    Button bt_iniciar, bt_registrar, bt_google;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion_usuarios);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, options);

        imagen = findViewById(R.id.imagen);
        email = findViewById(R.id.email);
        contrasena = findViewById(R.id.contrasena);
        bt_iniciar = findViewById(R.id.bt_iniciar);
        bt_registrar = findViewById(R.id.bt_registrar);
        bt_google = findViewById(R.id.bt_google);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InicioSesionUSUARIOS.this, InicioSesionADMIN.class));
                finish();
            }
        });

        bt_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = email.getText().toString().trim();
                pass = contrasena.getText().toString().trim();


                if (mail.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(InicioSesionUSUARIOS.this, "INGRESE EMAIL Y CONTRASEÑA", Toast.LENGTH_LONG).show();
                } else {
                    if (emailValido(mail)) {
                        firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(InicioSesionUSUARIOS.this, "INICIANDO SESIÓN", Toast.LENGTH_SHORT).show();
                                    irHome();
                                } else {
                                    Toast.makeText(InicioSesionUSUARIOS.this, "CREDENCIALES INCORRECTAS", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(InicioSesionUSUARIOS.this, "EMAIL NO VÁLIDO", Toast.LENGTH_LONG).show();
                    }
                }
            }

        });

        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InicioSesionUSUARIOS.this, Registro.class));
                finish();
            }
        });

        bt_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 1234);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(InicioSesionUSUARIOS.this, "INICIANDO SESIÓN", Toast.LENGTH_SHORT).show();
                                    irHome();
                                } else {
                                    Toast.makeText(InicioSesionUSUARIOS.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void irHome() {
        Intent intent = new Intent(InicioSesionUSUARIOS.this, PantallaPrincipalUSUARIO.class);
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