package com.sulty.appventa.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sulty.appventa.ADMIN.DetallesArticuloADMIN;
import com.sulty.appventa.R;
import com.sulty.appventa.USUARIOS.DetallesArticuloUSUARIO;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterUSUARIOS extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Articulo> articuloList;

    public MyAdapterUSUARIOS(Context context, List<Articulo> articuloList) {
        this.context = context;
        this.articuloList = articuloList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(articuloList.get(position).getImagen()).into(holder.recImagen);
        holder.recNombre.setText(articuloList.get(position).getNombre());
        holder.recDescripcion.setText(articuloList.get(position).getDescripcion());
        holder.recEstado.setText(articuloList.get(position).getEstado());
        holder.recPrecio.setText(articuloList.get(position).getPrecio());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetallesArticuloUSUARIO.class);
                intent.putExtra("Imagen", articuloList.get(holder.getAdapterPosition()).getImagen());
                intent.putExtra("Nombre", articuloList.get(holder.getAdapterPosition()).getNombre());
                intent.putExtra("Descripcion", articuloList.get(holder.getAdapterPosition()).getDescripcion());
                intent.putExtra("Estado", articuloList.get(holder.getAdapterPosition()).getEstado());
                intent.putExtra("Precio", articuloList.get(holder.getAdapterPosition()).getPrecio());
                intent.putExtra("Key", articuloList.get(holder.getAdapterPosition()).getKey());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articuloList.size();
    }

    public void buscarDatos(ArrayList<Articulo> buscarList){
        articuloList = buscarList;
        notifyDataSetChanged();
    }
}

class   MyViewHolderUSUARIOS extends RecyclerView.ViewHolder{

    ImageView recImagen;
    TextView recNombre, recDescripcion, recEstado, recPrecio;
    CardView recCard;

    public MyViewHolderUSUARIOS(@NonNull View itemView) {
        super(itemView);

        recImagen = itemView.findViewById(R.id.recImagen);
        recCard = itemView.findViewById(R.id.recCard);
        recNombre = itemView.findViewById(R.id.recNombre);
        recDescripcion = itemView.findViewById(R.id.recDescripcion);
        recEstado = itemView.findViewById(R.id.recEstado);
        recPrecio = itemView.findViewById(R.id.recPrecio);
    }
}
