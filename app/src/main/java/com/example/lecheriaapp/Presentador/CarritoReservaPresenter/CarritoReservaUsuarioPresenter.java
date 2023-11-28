package com.example.lecheriaapp.Presentador.CarritoReservaPresenter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Modelo.ReservaModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    double subtotal = 0.0;
                    double total = 0.0;

                    // Busca la reserva temporal actual del usuario
                    for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                        String estado = reservaSnapshot.child("estado").getValue(String.class);
                        if ("RESERVA TEMPORAL".equals(estado)) {
                            DataSnapshot productosSnapshot = reservaSnapshot.child("productos");
                            for (DataSnapshot productoSnapshot : productosSnapshot.getChildren()) {
                                ProductoModel producto = productoSnapshot.getValue(ProductoModel.class);
                                if (producto != null) {
                                    // Obtener la cantidad y el precio como strings desde el producto
                                    String cantidadStr = producto.getCantidad();
                                    String precioStr = producto.getPrecio();

                                    // Convertir las cadenas a valores numéricos
                                    int cantidad = Integer.parseInt(cantidadStr);
                                    double precio = Double.parseDouble(precioStr);

                                    // Realizar la operación y acumular en subtotal
                                    subtotal += cantidad * precio;
                                }
                            }
                            total = subtotal;  // En este ejemplo, total es igual al subtotal
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
                    // Actualiza el estado de la reserva temporal a "FINALIZADO" en Firebase
                    DatabaseReference reservaRef = mDatabase.child("reservas").child(idReservaTemporal);
                    Map<String, Object> estadoMap = new HashMap<>();
                    estadoMap.put("estado", "FINALIZADO");

                    reservaRef.updateChildren(estadoMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                // Genera el código QR en un hilo separado
                                new GenerateQRCodeTask(idReservaTemporal, listener).execute();
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


    private class GenerateQRCodeTask extends AsyncTask<Void, Void, String> {
        private String idReservaTemporal;
        private OnReservaFinalizadaListener listener;

        public GenerateQRCodeTask(String idReservaTemporal, OnReservaFinalizadaListener listener) {
            this.idReservaTemporal = idReservaTemporal;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            // Generar el código QR y obtener su URL
            return generarYSubirCodigoQR(idReservaTemporal);
        }

        @Override
        protected void onPostExecute(String qrCodeUrl) {
            // Actualizar el atributo qr de la reserva con la URL del código QR
            actualizarQREnReserva(idReservaTemporal, qrCodeUrl, listener);
        }
    }
    private String generarYSubirCodigoQR(String idReservaTemporal) {
        // Generar el código QR
        String qrCodeContent = "Contenido del código QR";  // Puedes personalizar esto según tus necesidades
        Bitmap qrCodeBitmap = generarCodigoQR(qrCodeContent);

        // Subir el código QR al almacenamiento de Firebase
        String qrCodeFileName = "qr_code_" + idReservaTemporal + ".png";
        String qrCodeUrl = subirCodigoQRAlStorage(qrCodeBitmap, qrCodeFileName);

        return qrCodeUrl;
    }
    private String subirCodigoQRAlStorage(Bitmap qrCodeBitmap, String fileName) {
        // Obtener la referencia al Storage de Firebase
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("qrcodes").child(fileName);

        // Convertir el Bitmap a bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        // Subir el código QR al Storage
        UploadTask uploadTask = storageRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continuar con la tarea para obtener la URL de descarga
            return storageRef.getDownloadUrl();
        });

        try {
            // Obtener la URL de descarga del código QR
            return Tasks.await(urlTask).toString();
        } catch (Exception e) {
            Log.e("CarritoReservaPresenter", "Error al subir el código QR al Storage: " + e.getMessage());
            return null;
        }
    }

    private void actualizarQREnReserva(String idReservaTemporal, String qrCodeUrl, OnReservaFinalizadaListener listener) {
        // Actualizar el atributo qr de la reserva con la URL del código QR
        DatabaseReference reservaRef = mDatabase.child("reservas").child(idReservaTemporal);
        reservaRef.child("qr").setValue(qrCodeUrl, (error, ref) -> {
            if (error == null) {
                listener.onReservaFinalizada();
                Log.d("CarritoReservaPresenter", "Reserva finalizada con éxito");
            } else {
                listener.onError(error.getMessage());
            }
        });
    }
    private Bitmap generarCodigoQR(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
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
