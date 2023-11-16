package com.example.lecheriaapp.Modelo;

import java.util.List;

import java.util.Map;

public class ReservaModel {
    private String reservaId;
    private String estado;
    private String fecha;
    private Map<String, ProductoModel> productos; // Map to represent the nested products
    private int subtotal;
    private int total;
    private String usuarioId;

    // Constructors, getters, and setters

    public ReservaModel() {
        // Default constructor required for Firebase
    }

    public ReservaModel(String reservaId, String estado, String fecha, Map<String, ProductoModel> productos, int subtotal, int total, String usuarioId) {
        this.reservaId = reservaId;
        this.estado = estado;
        this.fecha = fecha;
        this.productos = productos;
        this.subtotal = subtotal;
        this.total = total;
        this.usuarioId = usuarioId;
    }

    public String getReservaId() {
        return reservaId;
    }

    public void setReservaId(String reservaId) {
        this.reservaId = reservaId;
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

    public Map<String, ProductoModel> getProductos() {
        return productos;
    }

    public void setProductos(Map<String, ProductoModel> productos) {
        this.productos = productos;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
