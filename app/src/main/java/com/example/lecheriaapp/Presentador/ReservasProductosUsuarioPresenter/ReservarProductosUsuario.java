package com.example.lecheriaapp.Presentador.ReservasProductosUsuarioPresenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.HomeView.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservarProductosUsuario {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String TAG = "ReservarProductosUsuario";

    public ReservarProductosUsuario(Context context) {
        this.context = context;
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
                        int cantidad = producto.getCantidad();
                        subtotal += precio * cantidad;
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

    public void reservarProducto(ProductoModel producto, int cantidad, String local) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference reservasRef = mDatabase.child("reservas");

            // Primero, verifica si el usuario ya tiene una reserva temporal
            reservasRef.orderByChild("usuarioId").equalTo(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean reservaTemporalEncontrada = false;
                            for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                                String estado = reservaSnapshot.child("estado").getValue(String.class);
                                if ("RESERVA TEMPORAL".equals(estado)) {
                                    reservaTemporalEncontrada = true;
                                    agregarProductoAReserva(reservaSnapshot, producto, cantidad, local);
                                    break;  // Salir del bucle si se encuentra una reserva temporal
                                }
                            }

                            if (!reservaTemporalEncontrada) {
                                // Si no se encuentra una reserva temporal, crear una nueva
                                crearNuevaReserva(userId, producto, cantidad, local);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Error al buscar reserva: " + error.getMessage());
                        }
                    });
        }
    }

    private void agregarProductoAReserva(DataSnapshot reservaSnapshot, ProductoModel producto, int cantidad, String local) {
        DatabaseReference productosRef = reservaSnapshot.child("productos").getRef();
        long cantidadProductos = reservaSnapshot.child("productos").getChildrenCount();
        String productoId = "producto" + String.format("%03d", cantidadProductos);

        DatabaseReference productoRef = productosRef.child(productoId);
        // Modificar la cantidad y local del producto antes de agregarlo a la reserva
        producto.setCantidad(cantidad);
        producto.setLocal(local);
        productoRef.setValue(producto);

        // Actualizar la fecha y hora
        actualizarFechaYHoraReserva(reservaSnapshot.getKey());

        calcularSubtotalYTotal(reservaSnapshot.getKey());
        Toast.makeText(context, "Producto reservado correctamente", Toast.LENGTH_SHORT).show();
        regresarAlHome();
    }

    private void crearNuevaReserva(String userId, ProductoModel producto, int cantidad, String local) {
        DatabaseReference reservaRef = mDatabase.child("reservas").push();
        reservaRef.child("usuarioId").setValue(userId);
        reservaRef.child("estado").setValue("RESERVA TEMPORAL");
        reservaRef.child("qr").setValue("null");
        DatabaseReference productosRef = reservaRef.child("productos");
        String productoId = "producto00";
        DatabaseReference productoRef = productosRef.child(productoId);
        // Modificar la cantidad y local del producto antes de agregarlo a la reserva
        producto.setCantidad(cantidad);
        producto.setLocal(local);
        productoRef.setValue(producto);

        // Actualizar la fecha y hora
        actualizarFechaYHoraReserva(reservaRef.getKey());

        calcularSubtotalYTotal(reservaRef.getKey());
        Toast.makeText(context, "Producto reservado correctamente", Toast.LENGTH_SHORT).show();
        regresarAlHome();
    }


    private void regresarAlHome() {
        // Reemplaza el fragmento actual con el HomeFragment
        if (context != null) {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .addToBackStack(null)
                    .commit();
        } else {
            Log.e(TAG, "Contexto es nulo al intentar regresar al Home");
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
