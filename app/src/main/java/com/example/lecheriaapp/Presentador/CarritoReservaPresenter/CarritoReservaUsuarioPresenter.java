package com.example.lecheriaapp.Presentador.CarritoReservaPresenter;

import android.util.Log;

import androidx.annotation.NonNull;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Modelo.ReservaModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarritoReservaUsuarioPresenter {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public CarritoReservaUsuarioPresenter() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void obtenerProductosEnReservaTemporal(final OnProductosObtenidosListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Realizar una consulta para obtener las reservas del usuario actual
            Query reservaQuery = mDatabase.child("reservas")
                    .orderByChild("usuarioId")
                    .equalTo(userId);

            reservaQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<ProductoModel> productos = new ArrayList<>();
                    for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                        String estado = reservaSnapshot.child("estado").getValue(String.class);

                        if ("RESERVA TEMPORAL".equals(estado)) {
                            // Solo obtén los productos de la reserva temporal
                            DataSnapshot productosSnapshot = reservaSnapshot.child("productos");
                            for (DataSnapshot productoSnapshot : productosSnapshot.getChildren()) {
                                ProductoModel producto = productoSnapshot.getValue(ProductoModel.class);
                                productos.add(producto);
                            }
                        }
                    }

                    listener.onProductosObtenidos(productos);

                    Log.d("CarritoReservaPresenter", "Productos obtenidos: " + productos.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onError(error.getMessage());
                }
            });
        }
    }

    public void obtenerNombreUsuario(final OnNombreUsuarioObtenidoListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Realizar una consulta para obtener el nombre del usuario
            Query usuarioQuery = mDatabase.child("Usuarios")
                    .child(userId)
                    .child("nombre");

            usuarioQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nombreUsuario = dataSnapshot.getValue(String.class);
                    listener.onNombreUsuarioObtenido(nombreUsuario);

                    Log.d("CarritoReservaPresenter", "Nombre de usuario obtenido: " + nombreUsuario);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onError(error.getMessage());
                }
            });
        }
    }

    public void obtenerFechaReservaTemporal(final OnFechaReservaObtenidoListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Realizar una consulta para obtener las reservas del usuario actual
            Query reservaQuery = mDatabase.child("reservas")
                    .orderByChild("usuarioId")
                    .equalTo(userId);

            reservaQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                        String estado = reservaSnapshot.child("estado").getValue(String.class);
                        if ("RESERVA TEMPORAL".equals(estado)) {
                            String fechaReserva = reservaSnapshot.child("fecha").getValue(String.class);
                            listener.onFechaReservaObtenido(fechaReserva);

                            Log.d("CarritoReservaPresenter", "Fecha de reserva obtenida: " + fechaReserva);
                            return; // Termina el bucle después de obtener la fecha de la reserva temporal
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


    public void obtenerSubtotalYTotal(final OnSubtotalYTotalObtenidosListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Realizar una consulta para obtener la reserva temporal actual del usuario
            Query reservaQuery = mDatabase.child("reservas")
                    .orderByChild("usuarioId")
                    .equalTo(userId);

            reservaQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Busca la reserva temporal actual del usuario
                    for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                        String estado = reservaSnapshot.child("estado").getValue(String.class);
                        if ("RESERVA TEMPORAL".equals(estado)) {
                            double subtotal = reservaSnapshot.child("subtotal").getValue(Double.class);
                            double total = reservaSnapshot.child("total").getValue(Double.class);
                            listener.onSubtotalYTotalObtenidos(subtotal, total);
                            return; // Sal del bucle después de obtener los datos de la reserva temporal
                        }
                    }

                    listener.onError("No se encontró una reserva temporal");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onError(error.getMessage());
                }
            });
        }
    }

    public void obtenerIdReservaTemporal(final OnIdReservaTemporalObtenidoListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Realizar una consulta para obtener el subtotal y el total de la reserva actual del usuario
            Query reservaQuery = mDatabase.child("reservas")
                    .orderByChild("usuarioId")
                    .equalTo(userId);

            reservaQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Obtén el subtotal y total de la primera reserva (asumiendo que solo hay una)
                    for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                        String estado = reservaSnapshot.child("estado").getValue(String.class);
                        if ("RESERVA TEMPORAL".equals(estado)) {
                            String idReservaTemporal = reservaSnapshot.getKey();
                            listener.onIdReservaTemporalObtenido(idReservaTemporal);
                            return; // Sal del bucle ya que solo necesitas los datos de la primera reserva
                        }
                    }

                    listener.onError("No se encontraron reservas");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onError(error.getMessage());
                }
            });
        }
    }
    public void finalizarReservaTemporal(final OnReservaFinalizadaListener listener) {
        obtenerIdReservaTemporal(new OnIdReservaTemporalObtenidoListener() {
            @Override
            public void onIdReservaTemporalObtenido(String idReservaTemporal) {
                if (idReservaTemporal != null) {
                    // Actualiza el estado de la reserva temporal a "FINALIZADO"
                    DatabaseReference reservaRef = mDatabase.child("reservas").child(idReservaTemporal);
                    reservaRef.child("estado").setValue("FINALIZADO", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                listener.onReservaFinalizada();
                                Log.d("CarritoReservaPresenter", "Reserva finalizada con éxito");
                            } else {
                                listener.onError(error.getMessage());
                            }
                        }
                    });
                } else {
                    listener.onError("No se encontró una reserva temporal");
                }
            }

            @Override
            public void onError(String mensajeError) {
                listener.onError(mensajeError);
            }
        });
    }


    public void obtenerEstadoReservaTemporal(final OnEstadoReservaObtenidoListener listener) {
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

    public interface OnProductosObtenidosListener {
        void onProductosObtenidos(List<ProductoModel> productos);
        void onError(String mensajeError);
    }

    public interface OnReservaInformacionObtenidaListener {
        void onReservaInformacionObtenida(List<ReservaModel> reservas);
        void onError(String mensajeError);
    }

    public interface OnNombreUsuarioObtenidoListener {
        void onNombreUsuarioObtenido(String nombreUsuario);
        void onError(String mensajeError);
    }
    public interface OnFechaReservaObtenidoListener {
        void onFechaReservaObtenido(String fechaReserva);
        void onError(String mensajeError);
    }
    public interface OnSubtotalYTotalObtenidosListener {
        void onSubtotalYTotalObtenidos(double subtotal, double total);
        void onError(String mensajeError);
    }

    public interface OnIdReservaTemporalObtenidoListener {
        void onIdReservaTemporalObtenido(String idReservaTemporal);
        void onError(String mensajeError);
    }
    public interface OnReservaFinalizadaListener {
        void onReservaFinalizada();
        void onError(String mensajeError);
    }
    public interface OnEstadoReservaObtenidoListener {
        void onEstadoReservaObtenido(String estadoReserva);
        void onError(String mensajeError);
    }

}
