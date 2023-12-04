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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import java.util.Locale;


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

                // Formato de fecha actual en tus datos
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot productosSnapshot = reservaSnapshot.child("productos");
                    Map<String, ProductoModel> productosMap = new HashMap<>();

                    for (DataSnapshot productoSnapshot : productosSnapshot.getChildren()) {
                        ProductoModel producto = productoSnapshot.getValue(ProductoModel.class);
                        productosMap.put(productoSnapshot.getKey(), producto);
                    }

                    try {
                        // Convertir la fecha a objeto Date
                        Date fechaDate = dateFormat.parse(reservaSnapshot.child("fecha").getValue(String.class));

                        ReservaModel reserva = new ReservaModel(
                                reservaSnapshot.getKey(),
                                reservaSnapshot.child("estado").getValue(String.class),
                                dateFormat.format(fechaDate), // Guardar la fecha formateada si es necesario
                                productosMap,
                                reservaSnapshot.child("subtotal").getValue(Integer.class),
                                reservaSnapshot.child("total").getValue(Integer.class),
                                reservaSnapshot.child("usuarioId").getValue(String.class),
                                reservaSnapshot.child("qr").getValue(String.class)
                        );

                        reservas.add(reserva);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // Ordenar la lista de reservas por fecha
                Collections.sort(reservas, new Comparator<ReservaModel>() {
                    @Override
                    public int compare(ReservaModel r1, ReservaModel r2) {
                        try {
                            Date date1 = dateFormat.parse(r1.getFecha());
                            Date date2 = dateFormat.parse(r2.getFecha());
                            return date2.compareTo(date1); // Ordenar de forma descendente
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                view.mostrarReservas(reservas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
