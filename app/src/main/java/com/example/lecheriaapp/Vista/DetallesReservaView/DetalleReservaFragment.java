package com.example.lecheriaapp.Vista.DetallesReservaView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Adaptadores.ProductosAdapter; // Asegúrate de tener el adaptador correcto
import com.example.lecheriaapp.Modelo.ProductoModel; // Asegúrate de tener el modelo correcto
import com.example.lecheriaapp.Modelo.ReservaModel;
import com.example.lecheriaapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DetalleReservaFragment extends Fragment {

    private static final String ARG_RESERVA = "RESERVA";

    private ReservaModel reserva;

    public DetalleReservaFragment() {
        // Required empty public constructor
    }

    public static DetalleReservaFragment newInstance(ReservaModel reserva) {
        DetalleReservaFragment fragment = new DetalleReservaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RESERVA, reserva);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reserva = getArguments().getParcelable(ARG_RESERVA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_reserva, container, false);

        // Mostrar los detalles de la reserva en las vistas
        TextView textViewFecha = view.findViewById(R.id.textViewFecha);
        TextView textViewEstado = view.findViewById(R.id.textViewEstado);
        TextView textViewTotal = view.findViewById(R.id.textViewTotal);
        TextView textViewUsuarioId = view.findViewById(R.id.textViewUsuarioId);
        //TextView textViewQR = view.findViewById(R.id.textViewQR);
        ImageView imageViewQR = view.findViewById(R.id.imageViewQR);
        textViewFecha.setText(reserva.getFecha());
        textViewEstado.setText(reserva.getEstado());
        textViewTotal.setText(String.valueOf(reserva.getTotal()));
        textViewUsuarioId.setText("Usuario ID: " + String.valueOf(reserva.getUsuarioId()));
        //textViewQR.setText("QR: " + reserva.getQr());

        Glide.with(this)
                .load(reserva.getQr())
                .into(imageViewQR );

        // Configurar RecyclerView para mostrar la lista de productos
        RecyclerView recyclerViewProductos = view.findViewById(R.id.recyclerViewProductos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewProductos.setLayoutManager(layoutManager);

        // Obtener la lista de productos de la reserva
        Map<String, ProductoModel> productosMap = reserva.getProductos();

// Convertir el mapa a una lista de productos
        List<ProductoModel> productos = new ArrayList<>(productosMap.values());

        // Crear y configurar el adaptador
        ProductosAdapter productosAdapter = new ProductosAdapter(requireContext(), productos);
        recyclerViewProductos.setAdapter(productosAdapter);

        return view;
    }
}
