package com.example.lecheriaapp.Vista.ProductoView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Presentador.ReservasProductosUsuarioPresenter.ReservarProductosUsuario;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class DetallesProductoFragment extends Fragment {
    private ReservarProductosUsuario reservarProductosUsuario;
    private ProductoModel producto;
    private static final String TAG = "ReservarProductosUsuario";

    private List<String> localesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalles_producto, container, false);

        TextView nombreTextView = rootView.findViewById(R.id.producto_nombre);
        TextView caloriasTextView = rootView.findViewById(R.id.producto_calorias);
        TextView precioTextView = rootView.findViewById(R.id.producto_precio);
        TextView estadoTextView = rootView.findViewById(R.id.producto_estado);
        TextView disponibilidadTextView = rootView.findViewById(R.id.producto_disponibilidad);
        TextView ingredientesTextView = rootView.findViewById(R.id.producto_ingredientes);
        ImageView imagenImageView = rootView.findViewById(R.id.producto_imagen);
        TextView categoriaTextView = rootView.findViewById(R.id.producto_categoria);
        Button botonFavorito = rootView.findViewById(R.id.btn_favorito);

        Spinner localSpinner = rootView.findViewById(R.id.spinner_local);
        EditText cantidadEditText = rootView.findViewById(R.id.editText_cantidad);
        Button ReservasButton = rootView.findViewById(R.id.btn_reservas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        Bundle args = requireArguments();
        if (args != null) {
            String nombre = args.getString("nombre");
            String calorias = args.getString("caloria");
            String precio = args.getString("precio");
            String estado = args.getString("estado");
            String disponibilidad = args.getString("disponibilidad");
            String ingredientes = args.getString("ingredientes");
            String imageUrl = args.getString("imageUrl");
            String categoria = args.getString("categoria");
            String codigoQR = args.getString("codigoQR");

            producto = new ProductoModel(
                    getArguments().getString("nombre"),
                    getArguments().getString("caloria"),
                    getArguments().getString("precio"),
                    getArguments().getString("estado"),
                    getArguments().getString("disponibilidad"),
                    getArguments().getString("ingredientes"),
                    getArguments().getString("imageUrl"),
                    getArguments().getString("categoria"),
                    getArguments().getString("codigoQR")
            );

            nombreTextView.setText(nombre);
            precioTextView.setText("S/. " + precio);
            estadoTextView.setText(estado);
            ingredientesTextView.setText(ingredientes);
            caloriasTextView.setText(calorias + " Kcal");
            disponibilidadTextView.setText(disponibilidad);
            categoriaTextView.setText(categoria);

            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(imagenImageView);
        }
        reservarProductosUsuario = new ReservarProductosUsuario(requireContext());

        // Verificar si hay una sesión iniciada
        if (user != null) {
        // El usuario ha iniciado sesión, mostrar los componentes
            localSpinner.setVisibility(View.VISIBLE);
            cantidadEditText.setVisibility(View.VISIBLE);
            ReservasButton.setVisibility(View.VISIBLE);
        // Obtener la disponibilidad del argumento
        String disponibilidad = args.getString("disponibilidad");

// Crear una nueva lista y agregar la disponibilidad solo si no está en la lista
        List<String> localesList = new ArrayList<>();
        if (!localesList.contains(disponibilidad)) {
            localesList.add(disponibilidad);
        }

// Agregar los locales solo si no están en la lista
        if (!localesList.contains("Centro")) {
            localesList.add("Centro");
        }
        if (!localesList.contains("Ate")) {
            localesList.add("Ate");
        }
        if (!localesList.contains("SMP")) {
            localesList.add("SMP");
        }
        if (!localesList.contains("Callao")) {
            localesList.add("Callao");
        }

// Crear el adaptador y configurar el Spinner
        ArrayAdapter<String> localAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, localesList);
        localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localSpinner.setAdapter(localAdapter);

        } else {
            // No hay sesión iniciada, ocultar los componentes
            localSpinner.setVisibility(View.GONE);
            cantidadEditText.setVisibility(View.GONE);
            ReservasButton.setVisibility(View.GONE);
        }
        ReservasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantidadEditText != null) {
                    String cantidadText = cantidadEditText.getText().toString();
                    if (!cantidadText.isEmpty()) {
                        int cantidad = Integer.parseInt(cantidadText);
                        String local = localSpinner.getSelectedItem().toString();

                        reservarProductosUsuario.reservarProducto(producto, cantidad, local);

                        // Cerrar el teclado virtual
                        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                    } else {
                        // Mostrar un mensaje si el EditText está vacío
                        mostrarMensaje("Ingrese una cantidad");
                    }
                } else {
                    // Manejar el caso en que cantidadEditText es nulo
                    mostrarMensaje("Error al obtener el EditText de cantidad");
                }
            }
        });

        return rootView;
    }

    // Método para mostrar mensajes
    private void mostrarMensaje(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Contexto es nulo al mostrar mensaje");
        }
    }
}
