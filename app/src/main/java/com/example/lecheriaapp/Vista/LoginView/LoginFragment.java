package com.example.lecheriaapp.Vista.LoginView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.lecheriaapp.Presentador.LoginPresenter.PresentadorLogin;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.RegistroView.RegistroFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText correoEditText, passwordEditText;
    private PresentadorLogin presentadorLogin;

    public LoginFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Obtener una instancia de FirebaseAuth y DatabaseReference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // Inicializar el presentador para manejar las operaciones de autenticación
        presentadorLogin = new PresentadorLogin(getActivity(), mAuth, mDatabase);

        // Obtener las referencias a los elementos de la vista y configurar sus eventos de clic
        correoEditText = view.findViewById(R.id.username_input);
        passwordEditText = view.findViewById(R.id.password_input);
        Button mBtnLogin = view.findViewById(R.id.login_button);
        mBtnLogin.setOnClickListener(this);
        Button mBtnRegistro = view.findViewById(R.id.registro_button);
        mBtnRegistro.setOnClickListener(this);

        // Devolver la vista inflada
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                // Si se hace clic en el botón de login, obtener los valores de correo y contraseña
                String email = correoEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                // Mostrar un mensaje de error para que el usuario ingrese los datos requeridos
                Toast.makeText(getActivity(), "Por favor, ingresa el correo electrónico y la contraseña.", Toast.LENGTH_SHORT).show();
                } else {
                // Llamar al método signInUser del presentador para iniciar sesión con las credenciales proporcionadas
                presentadorLogin.signInUser(email, password);
                }
                break;
            case R.id.registro_button:
                // Si se hace clic en el botón de registro, reemplazar el fragmento actual por el fragmento de registro
                // Esta DEPRECATED, busco otra forma de hacerlo
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegistroFragment()).commit();
                break;
        }
    }
}

