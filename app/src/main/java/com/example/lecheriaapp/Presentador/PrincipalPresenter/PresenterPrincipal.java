package com.example.lecheriaapp.Presentador.PrincipalPresenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lecheriaapp.Modelo.UserModel;
import com.example.lecheriaapp.Presentador.CarritoReservaPresenter.CarritoReservaUsuarioPresenter;
import com.example.lecheriaapp.Vista.PrincipalView.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PresenterPrincipal {

    private Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    public PresenterPrincipal(Context mContext, DatabaseReference mDatabase, FirebaseAuth mAuth) {
        this.mContext = mContext;
        this.mDatabase = mDatabase;
        this.mAuth = mAuth;
    }

    public void welcomeMessage() {
        // Obtener una instancia de FirebaseAuth y DatabaseReference para acceder a la información del usuario actual
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseUser usuario = mAuth.getCurrentUser();

        // Si hay un usuario logueado, mostrar un mensaje de bienvenida con su nombre obtenido desde la base de datos
        if (usuario!= null) {
            mDatabase.child("Usuarios").child(usuario.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        Toast.makeText(mContext, "Bienvenido " + userModel.getNombre(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mContext, "UserModel es nulo", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejo de errores
                }
            });
        } else {
            // Si no hay ningún usuario logueado, mostrar un mensaje informativo
            Toast.makeText(mContext, "No olvides iniciar Sesion para disfrutar de todas las funcionalidades", Toast.LENGTH_SHORT).show();
        }
    }
    public void obtenerEstadoReservaTemporal(final CarritoReservaUsuarioPresenter.OnEstadoReservaObtenidoListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Realizar una consulta para obtener el estado de la reserva temporal del usuario
            Query reservaQuery = mDatabase.child("reservas")
                    .orderByChild("usuarioId")
                    .equalTo(userId);

            reservaQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                        String estado = reservaSnapshot.child("estado").getValue(String.class);
                        if ("RESERVA TEMPORAL".equals(estado)) {
                            listener.onEstadoReservaObtenido(estado);

                            Log.d("CarritoReservaPresenter", "Estado de reserva obtenido: " + estado);
                            return; // Termina el bucle después de obtener el estado de la reserva temporal
                        }
                    }
                    // Si no se encontró una reserva temporal, puedes manejarlo aquí
                    listener.onError("No se encontró una reserva temporal");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onError(error.getMessage());
                }
            });
        }
    }
}
