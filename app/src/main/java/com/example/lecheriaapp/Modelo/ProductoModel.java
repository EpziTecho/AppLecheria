package com.example.lecheriaapp.Modelo;

public class ProductoModel {
    private String nombre;
    private String caloria;
    private String precio;
    private String estado;
    private String disponibilidad;
    private String ingredientes;
    private String imageUrl;
    private String categoria;
    private String codigoQR;

    public ProductoModel(String nombre, String caloria, String precio, String estado, String disponibilidad, String ingredientes, String imageUrl, String categoria, String codigoQR) {
        this.nombre = nombre;
        this.caloria = caloria;
        this.precio = precio;
        this.estado = estado;
        this.disponibilidad = disponibilidad;
        this.ingredientes = ingredientes;
        this.imageUrl = imageUrl;
        this.categoria = categoria;
        this.codigoQR = codigoQR;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public ProductoModel() {

    }

    public String getCaloria() {
        return caloria;
    }

    public void setCaloria(String caloria) {
        this.caloria = caloria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
