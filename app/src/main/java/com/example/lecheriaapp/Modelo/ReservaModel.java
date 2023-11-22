package com.example.lecheriaapp.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.lecheriaapp.Modelo.ProductoModel;

import java.util.Map;

public class ReservaModel implements Parcelable {
    private String reservaId;
    private String estado;
    private String fecha;
    private Map<String, ProductoModel> productos;
    private int subtotal;
    private int total;
    private String usuarioId;
    private String qr;

    // Constructor
    public ReservaModel(String reservaId, String estado, String fecha,
                        Map<String, ProductoModel> productos, int subtotal,
                        int total, String usuarioId, String qr) {
        this.reservaId = reservaId;
        this.estado = estado;
        this.fecha = fecha;
        this.productos = productos;
        this.subtotal = subtotal;
        this.total = total;
        this.usuarioId = usuarioId;
        this.qr = qr;
    }

    // Getters y Setters

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

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
    protected ReservaModel(Parcel in) {
        reservaId = in.readString();
        estado = in.readString();
        fecha = in.readString();
        // Leer el Map de productos
        productos = in.readHashMap(ProductoModel.class.getClassLoader());
        subtotal = in.readInt();
        total = in.readInt();
        usuarioId = in.readString();
        qr = in.readString();
    }

    public static final Creator<ReservaModel> CREATOR = new Creator<ReservaModel>() {
        @Override
        public ReservaModel createFromParcel(Parcel in) {
            return new ReservaModel(in);
        }

        @Override
        public ReservaModel[] newArray(int size) {
            return new ReservaModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reservaId);
        dest.writeString(estado);
        dest.writeString(fecha);
        // Escribir el Map de productos
        dest.writeMap(productos);
        dest.writeInt(subtotal);
        dest.writeInt(total);
        dest.writeString(usuarioId);
        dest.writeString(qr);
    }
}
