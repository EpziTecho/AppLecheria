package com.example.lecheriaapp.Presentador.ReservasProductosUsuarioPresenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.google.firebase.database.ValueEventListener;
import com.example.lecheriaapp.Modelo.ReservaModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservarProductosUsuario {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String TAG = "ReservarProductosUsuario";

    public ReservarProductosUsuario() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void calcularSubtotalYTotal(String reservaId) {
        DatabaseReference productosRef = mDatabase.child("reservas").child(reservaId).child("productos");

        productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double subtotal = 0.0;
                for (DataSnapshot productoSnapshot : dataSnapshot.getChildren()) {
                    ProductoModel producto = productoSnapshot.getValue(ProductoModel.class);
                    if (producto != null) {
                        double precio = Double.parseDouble(producto.getPrecio());
                        subtotal += precio;
                    }
                }
                double total = subtotal;

                DatabaseReference reservaRef = mDatabase.child("reservas").child(reservaId);
                reservaRef.child("estado").setValue("RESERVA TEMPORAL");
                reservaRef.child("subtotal").setValue(subtotal);
                reservaRef.child("total").setValue(total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al calcular subtotal y total: " + error.getMessage());
            }
        });
    }

    public void reservarProducto(ProductoModel producto) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            mDatabase.child("reservas").orderByChild("usuarioId").equalTo(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String reservaId = dataSnapshot.getChildren().iterator().next().getKey();
                                DatabaseReference productosRef = mDatabase.child("reservas").child(reservaId).child("productos");

                                productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        long cantidadProductos = dataSnapshot.getChildrenCount();
                                        String productoId = "producto" + String.format("%03d", cantidadProductos);

                                        DatabaseReference productoRef = productosRef.child(productoId);
                                        productoRef.setValue(producto);

                                        // Actualizar la fecha y hora
                                        actualizarFechaYHoraReserva(reservaId);

                                        calcularSubtotalYTotal(reservaId);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e(TAG, "Error al reservar producto: " + error.getMessage());
                                    }
                                });
                            } else {
                                DatabaseReference reservaRef = mDatabase.child("reservas").push();
                                reservaRef.child("usuarioId").setValue(userId);
                                reservaRef.child("estado").setValue("RESERVA TEMPORAL");
                                DatabaseReference productosRef = reservaRef.child("productos");
                                String productoId = "producto00";
                                DatabaseReference productoRef = productosRef.child(productoId);
                                productoRef.setValue(producto);

                                // Actualizar la fecha y hora
                                actualizarFechaYHoraReserva(reservaRef.getKey());

                                calcularSubtotalYTotal(reservaRef.getKey());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Error al buscar reserva: " + error.getMessage());
                        }
                    });
        }
    }

    private void actualizarFechaYHoraReserva(String reservaId) {
        // Obtener la fecha y hora actuales del sistema
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fechaHora = sdf.format(new Date());

        DatabaseReference reservaRef = mDatabase.child("reservas").child(reservaId);
        reservaRef.child("fecha").setValue(fechaHora);
    }
}
