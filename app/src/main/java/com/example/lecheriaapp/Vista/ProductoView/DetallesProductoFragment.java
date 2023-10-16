package com.example.lecheriaapp.Vista.ProductoView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Presentador.ReservasProductosUsuarioPresenter.ReservarProductosUsuario;
import com.example.lecheriaapp.R;

public class DetallesProductoFragment extends Fragment {

    private ReservarProductosUsuario reservarProductosUsuario;
    private String productoId; // Agrega una variable para almacenar el ID del producto u otra identificación única del producto

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.fragment_detalles_producto, container, false);

        // Obtener referencias a las vistas del diseño
        TextView nombreTextView = rootView.findViewById(R.id.producto_nombre);
        TextView precioTextView = rootView.findViewById(R.id.producto_precio);
        TextView estadoTextView = rootView.findViewById(R.id.producto_estado);
        TextView ingredientesTextView = rootView.findViewById(R.id.producto_ingredientes);
        TextView caloriasTextView = rootView.findViewById(R.id.producto_calorias);
        TextView disponibilidadTextView = rootView.findViewById(R.id.producto_disponibilidad);
        TextView categoriaTextView = rootView.findViewById(R.id.producto_categoria);
        ImageView imagenImageView = rootView.findViewById(R.id.producto_imagen);
        Button botonFavorito = rootView.findViewById(R.id.btn_favorito);
        Button ReservasButton = rootView.findViewById(R.id.btn_reservas);

        // Obtener los argumentos pasados al fragmento
        Bundle args = getArguments();
        if (args != null) {
            // Obtener los datos del producto seleccionado
            String nombre = args.getString("nombre");
            String precio = args.getString("precio");
            String estado = args.getString("estado");
            String ingredientes = args.getString("ingredientes");
            String calorias = args.getString("calorias");
            String disponibilidad = args.getString("disponibilidad");
            String categoria = args.getString("categoria");

            // Obtener la URL de la imagen del producto
            String imageUrl = args.getString("imageUrl");

            // Almacena el ID del producto
            productoId = args.getString("productoId");

            // Asignar los datos a las vistas
            nombreTextView.setText(nombre);
            precioTextView.setText("S/. " + precio);
            estadoTextView.setText(estado);
            ingredientesTextView.setText(ingredientes);
            caloriasTextView.setText(calorias + " Kcal");
            disponibilidadTextView.setText(disponibilidad);
            categoriaTextView.setText(categoria);

            // Cargar la imagen utilizando Glide
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(imagenImageView);
        }

        // Configurar el clic en el botón "Reservar Producto"
        ReservasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reservarProductosUsuario != null && productoId != null) {
                    // Realizar la reserva
                    reservarProductosUsuario.reservarProducto(productoId, nombreTextView.getText().toString(), precioTextView.getText().toString(), estadoTextView.getText().toString(), ingredientesTextView.getText().toString(), caloriasTextView.getText().toString(), disponibilidadTextView.getText().toString(), categoriaTextView.getText().toString());
                    Toast.makeText(requireContext(), "Producto reservado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    // Mostrar un mensaje de error o manejar la falta de ReservaManager o productoId
                    Toast.makeText(requireContext(), "No se pudo realizar la reserva. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
