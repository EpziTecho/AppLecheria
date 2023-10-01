package com.example.lecheriaapp.Vista.PromocionesView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.R;

import java.util.Objects;


public class QrFragment extends Fragment {

    private TextView nombreProductoRow;
    private TextView precioProductoRow;
    private ImageView QrProductoRow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr, container, false);
         nombreProductoRow = view.findViewById(R.id.nombreProductoRow);
         precioProductoRow = view.findViewById(R.id.precioProductoRow);
         QrProductoRow = view.findViewById(R.id.qrProductoRow);


         Bundle args = getArguments();
            if (args != null) {
                // Obtener los datos del producto seleccionado
                String nombre = args.getString("nombre");
                String precio = args.getString("precio");
                String codigoQR = args.getString("codigoQR");
                nombreProductoRow.setText(nombre);
                precioProductoRow.setText(precio);
                Glide.with(requireContext())
                        .load(codigoQR)
                        .into(QrProductoRow);

            }


        return view;
    }

}