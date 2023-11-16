package com.example.lecheriaapp.Presentador.GestionProductosPresenter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lecheriaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PresentadorAgregarProductos {
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    public PresentadorAgregarProductos(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    private void cargaProductoFirebase(Map<String, Object> producto) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Primero obtenemos el número del último producto creado en el nodo "productos"
        mDatabase.child("productos").child("ultimoProducto").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    int ultimoProductoProductos = 0;
                    if (snapshot.exists()) {
                        ultimoProductoProductos = snapshot.getValue(Integer.class);
                    }

                    // Actualizamos la referencia del producto en la base de datos de Firebase con los valores del nuevo producto
                    String productoIdProductos = getFormattedNumber(ultimoProductoProductos + 1);
                    DatabaseReference productoRefProductos = mDatabase.child("productos").child("producto" + productoIdProductos);
                    productoRefProductos.setValue(producto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Si se completa la actualización del producto en "productos", muestra un mensaje de confirmación
                                Toast.makeText(mContext, "Producto agregado en productos", Toast.LENGTH_SHORT).show();
                            } else {
                                // Si ocurre un error en la actualización del producto en "productos", muestra un mensaje de error
                                Toast.makeText(mContext, "Error al agregar producto en productos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Actualizamos el número del último producto creado en el nodo "productos"
                    mDatabase.child("productos").child("ultimoProducto").setValue(ultimoProductoProductos + 1);

                    // Verificamos si el estado del producto es "En promoción"
                    String estado = (String) producto.get("estado");
                    if (estado != null && estado.equals("En promocion")) {
                        // Creamos un nodo de promociones y agregamos el producto allí para el usuario
                        DatabaseReference promocionesRefProductos = mDatabase.child("productos").child("promociones");
                        promocionesRefProductos.child("producto" + productoIdProductos).setValue(producto);
                    }
                } else {
                    Toast.makeText(mContext, "Error al obtener el último producto en productos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Obtenemos el número del último producto creado en el nodo del usuario
        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("ultimoProducto").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    int ultimoProductoUsuario = 0;
                    if (snapshot.exists()) {
                        ultimoProductoUsuario = snapshot.getValue(Integer.class);
                    }

                    // Actualizamos la referencia del producto en la base de datos de Firebase con los valores del nuevo producto
                    String productoIdUsuario = getFormattedNumber(ultimoProductoUsuario + 1);
                    DatabaseReference productoRefUser = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("productos").child("producto" + productoIdUsuario);
                    productoRefUser.setValue(producto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Si se completa la actualización del producto en el nodo del usuario, muestra un mensaje de confirmación
                                Toast.makeText(mContext, "Producto agregado para el usuario", Toast.LENGTH_SHORT).show();
                            } else {
                                // Si ocurre un error en la actualización del producto en el nodo del usuario, muestra un mensaje de error
                                Toast.makeText(mContext, "Error al agregar producto para el usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Actualizamos el número del último producto creado para el usuario
                    mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("ultimoProducto").setValue(ultimoProductoUsuario + 1);

                    // Verificamos si el estado del producto es "En promoción"
                    String estado = (String) producto.get("estado");
                    if (estado != null && estado.equals("En promocion")) {
                        // Creamos un nodo de promociones y agregamos el producto allí para el usuario
                        DatabaseReference promocionesRefUser = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("promociones");
                        promocionesRefUser.child("producto" + productoIdUsuario).setValue(producto);
                    }
                } else {
                    Toast.makeText(mContext, "Error al obtener el último producto para el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void agregarProducto(String estado, String nombre, String caloria, String precio,
                                String disponibilidad, String categoria, String ingredientes, String imageUrl, String qrUrl, String cantidad) {
        // Creamos un mapa de valores para el nuevo producto
        Map<String, Object> producto = new HashMap<>();
        producto.put("estado", estado);
        producto.put("nombre", nombre);
        producto.put("caloria", caloria);
        producto.put("precio", precio);
        producto.put("disponibilidad", disponibilidad);
        producto.put("categoria", categoria);
        producto.put("ingredientes", ingredientes);
        producto.put("imageUrl", imageUrl);
        producto.put("codigoQR", qrUrl);
        producto.put("cantidad", cantidad);

        // Agregamos el producto a la base de datos de Firebase
        cargaProductoFirebase(producto);
    }

    private String getFormattedNumber(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(number);
    }
}
