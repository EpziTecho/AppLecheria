package com.example.lecheriaapp.Modelo;

import java.util.List;

public class ReservaModel {
    private String id; // ID único de la reserva
    private String userId; // ID del usuario que hizo la reserva
    private List<ProductoModel> productos; // Lista de productos en la reserva

    private int cantidad; // Cantidad de productos reservados
    private String estado; // Estado de la reserva (por ejemplo, "RESERVA TEMPORAL")
    private String fecha; // Fecha de la reserva
    private String local; // Local donde se realiza la reserva
    private double subtotal; // Subtotal de la reserva
    private double descuentos; // Descuentos aplicados a la reserva
    private double total; // Total de la reserva
    private String hora; // Hora de la reserva

    public ReservaModel(String fecha, String nombreUsuario, double subtotal, double total, List<ProductoModel> productos) {
        // Constructor vacío requerido por Firebase Realtime Database
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public ReservaModel(String id, String userId, List<ProductoModel> productos, String estado, String fecha, String local, double subtotal, double descuentos, double total, String hora) {
        this.id = id;
        this.userId = userId;
        this.productos = productos;
        this.estado = estado;
        this.fecha = fecha;
        this.local = local;
        this.subtotal = subtotal;
        this.descuentos = descuentos;
        this.total = total;
        this.hora = hora;
    }

    // Agrega los métodos getter y setter para todas las propiedades


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ProductoModel> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoModel> productos) {
        this.productos = productos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(double descuentos) {
        this.descuentos = descuentos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
