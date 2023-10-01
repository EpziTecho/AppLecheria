package com.example.lecheriaapp.Presentador.LoginPresenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lecheriaapp.Vista.PrincipalView.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PresentadorLogin {

    private Context mcontext; // Contexto de la actividad o aplicación que utiliza esta clase
    private FirebaseAuth mAuth; // Objeto de FirebaseAuth para manejar la autenticación de usuarios en Firebase
    private DatabaseReference mDatabase; // Objeto de DatabaseReference para acceder y modificar datos en la base de datos en tiempo real de Firebase
    private String TAG= "Presentador Login"; // Etiqueta utilizada para identificar los mensajes de registro o "Log" en el registro de depuración

    public PresentadorLogin(Context context, FirebaseAuth mAuth, DatabaseReference mDatabase) {
        this.mcontext = context; // Asignar el contexto de la actividad o aplicación a la variable de clase correspondiente
        this.mAuth = mAuth; // Asignar el objeto de FirebaseAuth a la variable de clase correspondiente
        this.mDatabase = mDatabase; // Asignar el objeto de DatabaseReference a la variable de clase correspondiente
    }

    public void signInUser(String email, String password) {

        // Crear un cuadro de diálogo de progreso mientras se realiza la tarea
        final ProgressDialog dialog = new ProgressDialog(mcontext);
        dialog.setMessage("Iniciando sesión..."); // Establecer el mensaje que se mostrará en el cuadro de diálogo
        dialog.setCancelable(false); // Establecer si el usuario puede cancelar el cuadro de diálogo o no
        dialog.show(); // Mostrar el cuadro de diálogo en la pantalla

        // Intentar iniciar sesión con el email y la contraseña proporcionados
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()) { // Si el usuario se loguea correctamente
                            //redireccionar a la vista principal
                            Log.d(TAG, "signInWithEmail:success"); // Registrar un mensaje de éxito en el registro de depuración
                            mDatabase.child("Usuarios").child(task.getResult().getUser().getUid()).child("estado").setValue("online"); // Establecer el estado del usuario en línea en la base de datos en tiempo real de Firebase
                            Intent intent= new Intent(mcontext, MainActivity.class); // Crear un intent para abrir la actividad principal
                            mcontext.startActivity(intent); // Iniciar la actividad principal
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException()); // Registrar un mensaje de fallo en el registro de depuración
                            Toast.makeText(mcontext, "Authentication failed.", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de error al usuario
                            dialog.dismiss(); // Descartar el cuadro de diálogo de progreso
                        }
                    }
                });
    }
}