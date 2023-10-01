package com.example.lecheriaapp.Vista.RegistroView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lecheriaapp.Presentador.RegistroPresenter.PresentadorRegistro;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroFragment extends Fragment implements View.OnClickListener {

    // Declaración de variables
    private EditText mEtxtNombre, mEtxtUsuario, mEtxtCorreo, mEtxtPassword, mEtxtConfirmarPassword;
    private PresentadorRegistro presentadorRegistro;
    private ImageView mImagenRegistro;
    private static final int GALLERY_REQUEST_CODE = 123;
    private Uri imageUri;

    public RegistroFragment() {

    }

    // Método que se ejecuta cuando se crea la vista del fragmento
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        // Obtener instancias de Firebase Auth y Database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // Crear instancia del presentador del registro
        presentadorRegistro = new PresentadorRegistro(getActivity(), mAuth, mDatabase);

        // Obtener referencias de los elementos del layout
        mEtxtNombre = view.findViewById(R.id.editTextNombre);
        mEtxtUsuario = view.findViewById(R.id.editTextUsername);
        mEtxtCorreo = view.findViewById(R.id.editTextEmail);
        mEtxtPassword = view.findViewById(R.id.editTextPassword);
        mEtxtConfirmarPassword = view.findViewById(R.id.editTextConfirmarPassword);
        Button mBtnRegistro = view.findViewById(R.id.buttonRegistro);
        mBtnRegistro.setOnClickListener(this);
        Button mBtnSeleccionarImagen = view.findViewById(R.id.selectImageButtonRegistro);
        mBtnSeleccionarImagen.setOnClickListener(this);
        mImagenRegistro = view.findViewById(R.id.selectedImageView);
        mImagenRegistro.setOnClickListener(this);

        // Devolver vista
        return view;
    }

    // Método que se ejecuta cuando se hace clic en un botón
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegistro:
                // Obtener valores de los campos de texto
                String nombre = mEtxtNombre.getText().toString().trim();
                String usuario = mEtxtUsuario.getText().toString().trim();
                String email = mEtxtCorreo.getText().toString().trim();
                String password = mEtxtPassword.getText().toString().trim();
                String confirmarPassword = mEtxtConfirmarPassword.getText().toString().trim();

                if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(usuario) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmarPassword)) {
                    // Mostrar un mensaje de error para que el usuario complete todos los campos requeridos
                    Toast.makeText(getActivity(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmarPassword)) {
                    // Mostrar un mensaje de error si las contraseñas no coinciden
                    Toast.makeText(getActivity(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                } else {
                    // Verificar si se seleccionó una imagen
                    if (imageUri == null) {
                        // Mostrar un mensaje de error si no se seleccionó una imagen
                        Toast.makeText(getActivity(), "Por favor, selecciona una imagen.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Enviar información al presentador para crear un nuevo usuario con la imagen seleccionada
                        presentadorRegistro.singUpUser(email, password, nombre, usuario, imageUri);
                    }
                }
                break;
            case R.id.selectImageButtonRegistro:
                // Abrir la galería de imágenes para seleccionar una imagen
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
                break;
        }
    }


    // Método que se ejecuta cuando se obtiene el resultado de la actividad de la galería
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            // Obtener la URI de la imagen seleccionada
            imageUri = data.getData();
            // Mostrar la imagen seleccionada en el ImageView
            mImagenRegistro.setImageURI(imageUri);
        }
    }
}
