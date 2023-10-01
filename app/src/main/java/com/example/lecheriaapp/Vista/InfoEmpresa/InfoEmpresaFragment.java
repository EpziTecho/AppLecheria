package com.example.lecheriaapp.Vista.InfoEmpresa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.lecheriaapp.R;

public class InfoEmpresaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_empresa, container, false);
        ImageView whatsappImageView = view.findViewById(R.id.logowhatsapp);
        ImageView facebookImageView = view.findViewById(R.id.logofacebook);
        ImageView instagramImageView = view.findViewById(R.id.logoinstagram);
        // Configurar OnClickListener para WhatsApp
        whatsappImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });
        // Configurar OnClickListener para Facebook
        facebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebook();
            }
        });
        // Configurar OnClickListener para Instagram
        instagramImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagram();
            }
        });
        return view;
    }
    private void openWhatsApp() {
        String phoneNumber = "+51902216601"; //
        String message = "Hola, estoy interesado en tus productos."; //
        try {
            // Formar la URL de WhatsApp con el número de teléfono y el mensaje
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);

            // Abrir la URL en un navegador externo
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFacebook() {
        String facebookUrl = "https://www.facebook.com/lalecheria.ite/?locale=es_LA";
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openInstagram() {
        String instagramUrl = "https://www.instagram.com/_lalecheria/";
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
