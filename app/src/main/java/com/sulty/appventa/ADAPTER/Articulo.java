package com.sulty.appventa.ADAPTER;

public class Articulo {
    private String nombre;
    private String descripcion;
    private String estado;
    private String precio;
    private String imagen;
    private String key;

    public Articulo(){

    }

    public Articulo(String nombre, String descripcion, String estado, String precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.precio = precio;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public String getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
