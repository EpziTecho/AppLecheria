package com.example.lecheriaapp.Presentador.GestionReservasPresenter;

import com.example.lecheriaapp.Modelo.ReservaModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReservaModel reserva = snapshot.getValue(ReservaModel.class);
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
