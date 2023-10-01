package com.example.lecheriaapp.Vista.ProductoView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.R;

public class DetallesProductoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        // Obtener los argumentos pasados al fragmento
        Bundle args = getArguments();
        if (args != null) {
            // Obtener los datos del producto seleccionado
            String nombre = args.getString("nombre");
            String precio = args.getString("precio");
            String estado = args.getString("estado");
            String ingredientes = args.getString("ingredientes");
            String calorias = args.getString("caloria");
            String disponibilidad = args.getString("disponibilidad");
            String categoria = args.getString("categoria");

            // Obtener la URL de la imagen del producto
            String imageUrl = args.getString("imageUrl");

            // Asignar los datos a las vistas
            nombreTextView.setText(nombre);
            precioTextView.setText("S/. " + precio);
            estadoTextView.setText(estado);
            ingredientesTextView.setText(ingredientes);
            caloriasTextView.setText(calorias+" Kcal");
            disponibilidadTextView.setText(disponibilidad);
            categoriaTextView.setText(categoria);

            // Cargar la imagen utilizando Glide
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(imagenImageView);
        }

        return rootView;
    }
}
