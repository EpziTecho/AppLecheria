package com.example.lecheriaapp.Presentador.RegistroPresenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lecheriaapp.Vista.PrincipalView.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class PresentadorRegistro {
    private Context mcontext; // Contexto de la actividad o aplicación que utiliza esta clase
    private FirebaseAuth mAuth; // Objeto de FirebaseAuth para manejar la autenticación de usuarios en Firebase
    private DatabaseReference mDatabase; // Objeto de DatabaseReference para acceder y modificar datos en la base de datos en tiempo real de Firebase
    private StorageReference mStorage; // Objeto de StorageReference para almacenar la imagen en Firebase Storage
    private String TAG= "Presentador Registro"; // Etiqueta utilizada para identificar los mensajes de registro o "Log" en el registro de depuración

    public PresentadorRegistro(Context mcontext, FirebaseAuth mAuth, DatabaseReference mDatabase) {
        this.mcontext = mcontext;
        this.mAuth = mAuth;
        this.mDatabase = mDatabase;
        this.mStorage = FirebaseStorage.getInstance().getReference();
    }

    public void singUpUser(final String email, final String password, final String nombreCompleto, final String username, Uri imageUri) {
        // Crear un diálogo de progreso para mostrar mientras se realiza el registro del usuario
        final ProgressDialog dialog = new ProgressDialog(mcontext);
        dialog.setMessage("Registrando usuario...");
        dialog.setCancelable(false);
        dialog.show();

        // Subir la imagen seleccionada a Firebase Storage
        StorageReference imageRef = mStorage.child("usuarios").child(email + "_profile");
        imageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Obtener la URL de la imagen subida
                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final Uri downloadUri = task.getResult();

                                // Llamar al método de Firebase que registra al usuario con su correo electrónico y contraseña
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Si el registro es exitoso, se actualiza la información del usuario en la base de datos
                                                    Log.d(TAG, "createUserWithEmail:success");
                                                    dialog.dismiss();

                                                    // Crear un Map con la información del usuario a almacenar en la base de datos
                                                    Map<String, Object> crearUsuario = new HashMap<>();
                                                    crearUsuario.put("nombre", nombreCompleto);
                                                    crearUsuario.put("username", username);
                                                    crearUsuario.put("email", email);
                                                    crearUsuario.put("password", password);
                                                    crearUsuario.put("rol", "cliente");
                                                    crearUsuario.put("imagen", downloadUri.toString());

                                                    // Almacenar la información del usuario en la base de datos bajo el UID del usuario registrado
                                                    mDatabase.child("Usuarios").child(task.getResult().getUser().getUid()).updateChildren(crearUsuario);

                                                    // Crear un intent para abrir la actividad principal y luego iniciarla
                                                    Intent intent = new Intent(mcontext, MainActivity.class);
                                                    mcontext.startActivity(intent);
                                                } else {
                                                    // Si el registro falla, se muestra un mensaje de error al usuario
                                                    dialog.dismiss();
                                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                    Toast.makeText(mcontext, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Si no se puede obtener la URL de la imagen subida, se muestra un mensaje de error
                                dialog.dismiss();
                                Log.w(TAG, "getDownloadUrl:failure", task.getException());
                                Toast.makeText(mcontext, "Failed to get image URL.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Si no se puede subir la imagen, se muestra un mensaje de error
                    dialog.dismiss();
                    Log.w(TAG, "putFile:failure", task.getException());
                    Toast.makeText(mcontext, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
