package com.example.lecheriaapp.Presentador.ReservasProductosUsuarioPresenter;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.lecheriaapp.Modelo.ProductoModel;

public class ReservarProductosUsuario {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String TAG = "ReservarProductosUsuario"; // Etiqueta para las impresiones de log

    public ReservarProductosUsuario() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void reservarProducto(ProductoModel producto) {
        Log.d(TAG, "Producto recibido: " + producto.getCodigoQR());
        FirebaseUser currentUser =   mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Crear una referencia al nodo "reservastemporal" y generar una clave única para la reserva
            DatabaseReference reservaRef = mDatabase.child("reservastemporal").push();

            // Almacenar todo el objeto ProductoModel en el nodo de la reserva


            // Almacenar los datos del producto con sus etiquetas
            reservaRef.child("nombre").setValue(producto.getNombre());
            reservaRef.child("caloria").setValue(producto.getCaloria());
            reservaRef.child("precio").setValue(producto.getPrecio());
            reservaRef.child("estado").setValue(producto.getEstado());
            reservaRef.child("ingredientes").setValue(producto.getIngredientes());
            reservaRef.child("disponibilidad").setValue(producto.getDisponibilidad());
            reservaRef.child("imageUrl").setValue(producto.getImageUrl());
            reservaRef.child("categoria").setValue(producto.getCategoria());
            reservaRef.child("codigoQR").setValue(producto.getCodigoQR());


            // También puedes almacenar el ID del usuario que hizo la reserva si es necesario
           // reservaRef.child("usuarioId").setValue(userId);
        }
    }
}
