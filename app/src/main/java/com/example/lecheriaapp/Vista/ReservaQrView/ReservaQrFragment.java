package com.example.lecheriaapp.Vista.ReservaQrView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.R;

public class ReservaQrFragment extends Fragment {

    private static final String ARG_QR_CODE_URL = "qr_code_url";
    private static final String ARG_RESERVA_CODIGO = "reserva_codigo";
    private static final String ARG_NOMBRE_USUARIO = "nombre_usuario";

    private String qrCodeUrl;
    private String reservaCodigo;
    private String nombreUsuario;

    public ReservaQrFragment() {
        // Constructor vacío requerido por Fragment
    }

    public static ReservaQrFragment newInstance(String qrCodeUrl, String reservaCodigo, String nombreUsuario) {
        ReservaQrFragment fragment = new ReservaQrFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QR_CODE_URL, qrCodeUrl);
        args.putString(ARG_RESERVA_CODIGO, reservaCodigo);
        args.putString(ARG_NOMBRE_USUARIO, nombreUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qrCodeUrl = getArguments().getString(ARG_QR_CODE_URL);
            reservaCodigo = getArguments().getString(ARG_RESERVA_CODIGO);
            nombreUsuario = getArguments().getString(ARG_NOMBRE_USUARIO);
            Log.d("QR", "URL del código QR: " + qrCodeUrl);
            Log.d("QR", "Código de Reserva: " + reservaCodigo);
            Log.d("QR", "Nombre de Usuario: " + nombreUsuario);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva_qr, container, false);
        ImageView qrCodeImageView = view.findViewById(R.id.qrCodeImageView);
        TextView codigoReservaTextView = view.findViewById(R.id.codigoReservaTextView);
        TextView nombreUsuarioTextView = view.findViewById(R.id.nombreUsuarioTextView);

        // Verificar si la URL es válida antes de cargarla
        if (!TextUtils.isEmpty(qrCodeUrl)) {
            // Cargar el código QR utilizando Glide (puedes usar Picasso también)
            Glide.with(this)
                    .load(qrCodeUrl)
                    .into(qrCodeImageView);

            // Mostrar el código de reserva y el nombre del usuario
            codigoReservaTextView.setText("Código de Reserva: " + reservaCodigo);
            nombreUsuarioTextView.setText("Nombre de Usuario: " + nombreUsuario);
        } else {
            // Manejar el caso de URL nula o vacía
            Toast.makeText(requireContext(), "URL del código QR no válida", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
