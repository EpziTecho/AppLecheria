package com.example.lecheriaapp.Vista.ProductoView;

import android.os.Bundle;
import android.util.Log;
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
import com.example.lecheriaapp.Modelo.ProductoModel;

public class DetallesProductoFragment extends Fragment {
    private ReservarProductosUsuario reservarProductosUsuario;
    private ProductoModel producto; // Almacena el producto como un objeto
    private static final String TAG = "ReservarProductosUsuario"; // Etiqueta para las impresiones de log

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalles_producto, container, false);

        // Obtener referencias a las vistas del diseño
        TextView nombreTextView = rootView.findViewById(R.id.producto_nombre);
        TextView caloriasTextView = rootView.findViewById(R.id.producto_calorias);
        TextView precioTextView = rootView.findViewById(R.id.producto_precio);
        TextView estadoTextView = rootView.findViewById(R.id.producto_estado);
        TextView disponibilidadTextView = rootView.findViewById(R.id.producto_disponibilidad);
        TextView ingredientesTextView = rootView.findViewById(R.id.producto_ingredientes);
        ImageView imagenImageView = rootView.findViewById(R.id.producto_imagen);
        TextView categoriaTextView = rootView.findViewById(R.id.producto_categoria);
        Button botonFavorito = rootView.findViewById(R.id.btn_favorito);

        // Obtener los argumentos pasados al fragmento
        Bundle args = getArguments();
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


            // Crea un objeto ProductoModel con los datos del producto ordenados alfabéticamente y de forma descendente

            producto= new ProductoModel(getArguments().getString("nombre"),getArguments().getString("caloria"),getArguments().getString("precio"),getArguments().getString("estado"),getArguments().getString("disponibilidad"),getArguments().getString("ingredientes"),getArguments().getString("imageUrl"),getArguments().getString("categoria"),getArguments().getString("codigoQR"));



            Log.d(TAG, "Producto recibido: " + producto.getCodigoQR());
            Log.d(TAG, "Reservando producto"+ producto.getNombre());
            Log.d(TAG, "Reservando producto"+ producto.getPrecio());

            // Asigna los datos a las vistas
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
        reservarProductosUsuario = new ReservarProductosUsuario();

        // Configurar el clic en el botón "Reservar Producto"
        Button ReservasButton = rootView.findViewById(R.id.btn_reservas);
        ReservasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama a la función para reservar el producto con el objeto ProductoModel

                reservarProductosUsuario.reservarProducto(producto);
            }
        });

        return rootView;
    }

}
