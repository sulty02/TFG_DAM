package com.sulty.appventa.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sulty.appventa.ADAPTER.MyAdapter;
import com.sulty.appventa.ADAPTER.Articulo;
import com.sulty.appventa.LOGIN.InicioSesionUSUARIOS;
import com.sulty.appventa.R;

import java.util.ArrayList;
import java.util.List;

public class PantallaPrincipalADMIN extends AppCompatActivity {

    FloatingActionButton bt_anadir, bt_cerrar;

    RecyclerView recyclerView;
    List<Articulo> articuloList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchView;
    MyAdapter adapter;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantallaprincipaladmin);

        firebaseAuth = FirebaseAuth.getInstance();

        bt_anadir = findViewById(R.id.bt_anadir);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.buscar);
        searchView.clearFocus();

        GridLayoutManager  gridLayoutManager = new GridLayoutManager(PantallaPrincipalADMIN.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(PantallaPrincipalADMIN.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_bar);
        AlertDialog dialog = builder.create();
        dialog.show();

        articuloList = new ArrayList<>();

        adapter = new MyAdapter(PantallaPrincipalADMIN.this, articuloList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("PRODUCTOS");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articuloList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Articulo articulo = itemSnapshot.getValue(Articulo.class);
                    articulo.setKey(itemSnapshot.getKey());
                    articuloList.add(articulo);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)  {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarLista(newText);
                return true;
            }
        });

        bt_anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaPrincipalADMIN.this, SubirArticuloADMIN.class));
            }
        });

        bt_cerrar = findViewById(R.id.bt_cerrar);

        bt_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                irInicio();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            irInicio();
        }
    }

    private void irInicio() {
        startActivity(new Intent(PantallaPrincipalADMIN.this, InicioSesionUSUARIOS.class));
        finish();
    }

    public void buscarLista(String texto){
        ArrayList<Articulo> buscarenLista = new ArrayList<>();
        for (Articulo articulo : articuloList){
            if (articulo.getNombre().toLowerCase().contains(texto.toLowerCase())){
                buscarenLista.add(articulo);
            }
        }
        adapter.buscarDatos(buscarenLista);
    }
}