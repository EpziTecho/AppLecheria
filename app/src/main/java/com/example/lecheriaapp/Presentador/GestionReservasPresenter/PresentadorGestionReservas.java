package com.example.lecheriaapp.Presentador.GestionReservasPresenter;

import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Modelo.ReservaModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresentadorGestionReservas {
    private GestionReservasView view;

    public PresentadorGestionReservas(GestionReservasView view) {
        this.view = view;
    }

    public void obtenerReservasDesdeFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reservas");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ReservaModel> reservas = new ArrayList<>();

                for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                    // For each reservation, get the data snapshot
                    DataSnapshot productosSnapshot = reservaSnapshot.child("productos");

                    // Create a Map to hold the products
                    Map<String, ProductoModel> productosMap = new HashMap<>();

                    // Loop through the products snapshot
                    for (DataSnapshot productoSnapshot : productosSnapshot.getChildren()) {
                        // Get each product and add it to the productosMap
                        ProductoModel producto = productoSnapshot.getValue(ProductoModel.class);
                        productosMap.put(productoSnapshot.getKey(), producto);
                    }

                    // Create a ReservaModel object and set its values
                    ReservaModel reserva = new ReservaModel(
                            reservaSnapshot.getKey(),
                            reservaSnapshot.child("estado").getValue(String.class),
                            reservaSnapshot.child("fecha").getValue(String.class),
                            productosMap,
                            reservaSnapshot.child("subtotal").getValue(Integer.class),
                            reservaSnapshot.child("total").getValue(Integer.class),
                            reservaSnapshot.child("usuarioId").getValue(String.class),
                            reservaSnapshot.child("qr").getValue(String.class)
                    );

                    // Add the ReservaModel object to the list
                    reservas.add(reserva);
                }

                // Mostrar las reservas obtenidas en la vista
                view.mostrarReservas(reservas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase, si es necesario
                view.mostrarMensaje("Error al obtener las reservas");
            }
        });
    }

    public interface GestionReservasView {
        void mostrarMensaje(String mensaje);

        void mostrarReservas(List<ReservaModel> reservas);

        // Otros métodos de la interfaz para la interacción con la vista
    }
}