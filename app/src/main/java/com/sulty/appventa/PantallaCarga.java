package com.sulty.appventa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sulty.appventa.LOGIN.InicioSesionUSUARIOS;

import java.util.Timer;
import java.util.TimerTask;

public class PantallaCarga extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantallacarga);

        TimerTask carga = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(PantallaCarga.this, InicioSesionUSUARIOS.class);
                startActivity(intent);
                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(carga, 3000);


    }
}