package com.example.lecheriaapp.Presentador.GestionProductosPresenter;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.GestionProductosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.Map;

public class PresentadorEditarProductos {
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    public PresentadorEditarProductos(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Inicializar FirebaseStorage
        mStorage = FirebaseStorage.getInstance();
        // Obtener la referencia a la raíz del Storage
        mStorageRef = mStorage.getReference();
    }

    private void actualizarProductoFirebase(Map<String, Object> producto, int position, Fragment fragment) {
        String userId = mAuth.getCurrentUser().getUid();

        // Actualizamos los valores del producto en la base de datos de Firebase
        mDatabase.child("Usuarios").child(userId).child("productos").child("producto" + getFormattedNumber(position)).updateChildren(producto)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Si se completa la actualización del producto, se muestra un mensaje de confirmación
                        //Toast.makeText(mContext, "Producto actualizado", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                        Fragment gestionProductosFragment = new GestionProductosFragment();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, gestionProductosFragment).commit();

                        // Verificar si el estado del producto es "En promocion"
                        String estado = (String) producto.get("estado");
                        if (estado != null && estado.equals("En promocion")) {
                            // Crear un nodo de promociones y agregar el producto allí
                            DatabaseReference promocionesRef = mDatabase.child("Usuarios").child(userId).child("promociones");
                            String productoId = "producto" + getFormattedNumber(position);
                            promocionesRef.child(productoId).setValue(producto);
                        } else {
                            // Eliminar el producto del nodo promociones si existe
                            DatabaseReference promocionesRef = mDatabase.child("Usuarios").child(userId).child("promociones");
                            String productoId = "producto" + getFormattedNumber(position);
                            promocionesRef.child(productoId).removeValue();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si ocurre un error en la actualización del producto, se muestra un mensaje de error
                        Toast.makeText(mContext, "Error al actualizar producto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editarProducto(Map<String, Object> producto, int position, Fragment fragment, Uri nuevaImageUri) {
        if (nuevaImageUri != null) {
            // Si se seleccionó una nueva imagen, subir la imagen a Firebase Storage
            StorageReference imageRef = mStorageRef.child("productos").child(nuevaImageUri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(nuevaImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Obtener la URL de descarga de la imagen subida
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    producto.put("imageUrl", imageUrl); // Agregar la URL de la nueva imagen al mapa de datos del producto

                    // Llamar al método de actualización del producto con la nueva imagen
                    actualizarProductoFirebase(producto, position, fragment);
                });
            }).addOnFailureListener(e -> {
                // Mostrar mensaje de error en caso de falla en la carga de la nueva imagen
                Toast.makeText(mContext, "Error al subir la nueva imagen", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Si no se seleccionó una nueva imagen, llamar al método de actualización del producto sin cambios en la imagen
            actualizarProductoFirebase(producto, position, fragment);
        }
    }


    private String getFormattedNumber(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(number);
    }
}
