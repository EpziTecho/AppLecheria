package com.example.lecheriaapp.Presentador.GestionProductosPresenter;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.GestionProductosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

        // Primero obtenemos el número del último producto creado
        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("ultimoProducto").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    int ultimoProducto = 0;
                    if (snapshot.exists()) {
                        ultimoProducto = snapshot.getValue(Integer.class);
                    }

                    // Actualizamos la referencia del producto en la base de datos de Firebase con los valores del nuevo producto
                    String productoId = getFormattedNumber(ultimoProducto + 1);
                    DatabaseReference productoRef = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("productos").child("producto" + productoId);
                    productoRef.setValue(producto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Si se completa la actualización del producto, se muestra un mensaje de confirmación
                                Toast.makeText(mContext, "Producto agregado", Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container, new GestionProductosFragment()).commit();
                            } else {
                                // Si ocurre un error en la actualización del producto, se muestra un mensaje de error
                                Toast.makeText(mContext, "Error al agregar producto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Actualizamos el número del último producto creado
                    mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("ultimoProducto").setValue(ultimoProducto + 1);

                    // Verificar si el estado del producto es "En promoción"
                    String estado = (String) producto.get("estado");
                    if (estado != null && estado.equals("En promocion")) {
                        // Crear un nodo de promociones y agregar el producto allí
                        DatabaseReference promocionesRef = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("promociones");
                        promocionesRef.child("producto" + productoId).setValue(producto);
                    }
                } else {
                    Toast.makeText(mContext, "Error al obtener el último producto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void agregarProducto(String estado, String nombre, String caloria, String precio,
                                String disponibilidad, String categoria, String ingredientes, String imageUrl,String qrUrl) {
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
        producto.put("codigoQR",qrUrl);

        // Agregar el producto a la base de datos de Firebase
        cargaProductoFirebase(producto);
    }

    private String getFormattedNumber(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(number);
    }
}
