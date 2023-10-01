package com.example.lecheriaapp.Vista.UbicanosView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.lecheriaapp.R;

public class UbicanosFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // Coordenadas de ubicación por defecto
    private double defaultLatitude = -12.0480325;
    private double defaultLongitude = -77.0439976;

    private TextView tituloDireccionTextView;
    private TextView subtituloDireccionTextView;
    private TextView tituloDescripcionTextView;
    private ImageView imagenProductoRow;
    private LatLng destinationLatLng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ubicanos, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        tituloDireccionTextView = view.findViewById(R.id.titulo_direccion);
        subtituloDireccionTextView = view.findViewById(R.id.subtitulo_direccion);
        tituloDescripcionTextView = view.findViewById(R.id.titulo_descripcion);
        imagenProductoRow = view.findViewById(R.id.imagenProductoRow);
        tituloDireccionTextView.setVisibility(View.GONE);
        subtituloDireccionTextView.setVisibility(View.GONE);
        tituloDescripcionTextView.setVisibility(View.GONE);
        imagenProductoRow.setVisibility(View.GONE);

        // Verificar y solicitar los permisos necesarios
        if (checkLocationPermission()) {
            // Los permisos ya están otorgados, puedes realizar las acciones necesarias
            updateMapLocation(defaultLatitude, defaultLongitude);
            setTextViews(1); // Actualizar los TextViews
        } else {
            requestLocationPermission();
        }

        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        Button button3 = view.findViewById(R.id.button3);
        Button button4 = view.findViewById(R.id.button4);
        Button btnEmpezarRuta = view.findViewById(R.id.btnEmpezarRuta);

        // Asignar acciones para los botones
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la ubicación correspondiente al botón 1
                double latitude = -12.064148601879774;
                double longitude = -77.03696647741637;
                updateMapLocation(latitude, longitude);
                setTextViews(1); // Actualizar los TextViews

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la ubicación correspondiente al botón 2
                double latitude = -11.9530354;
                double longitude = -77.0726801;
                updateMapLocation(latitude, longitude);
                setTextViews(2); // Actualizar los TextViews
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la ubicación correspondiente al botón 3
                double latitude = -12.1852211;
                double longitude = -76.9999967;
                updateMapLocation(latitude, longitude);
                setTextViews(3); // Actualizar los TextViews
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la ubicación correspondiente al botón 4
                double latitude = -12.01388;
                double longitude = -76.901659;
                updateMapLocation(latitude, longitude);
                setTextViews(4); // Actualizar los TextViews
            }
        });

        btnEmpezarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (destinationLatLng != null) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationLatLng.latitude + "," + destinationLatLng.longitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(getActivity(), "No se encontró la aplicación Google Maps", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Seleccione una ubicación primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        updateMapLocation(defaultLatitude, defaultLongitude);
    }

    private void updateMapLocation(double latitude, double longitude) {
        if (googleMap != null) {
            LatLng location = new LatLng(latitude, longitude);
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(location).title("Ubicación"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));

            // Actualizar la posición de destino para la ruta
            destinationLatLng = location;
        }
    }

    private boolean checkLocationPermission() {
        int permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes realizar las acciones necesarias
                updateMapLocation(defaultLatitude, defaultLongitude);
                setTextViews(1); // Actualizar los TextViews
            }
        }
    }

    private void setTextViews(int buttonNumber) {
        tituloDireccionTextView.setVisibility(View.VISIBLE);
        subtituloDireccionTextView.setVisibility(View.VISIBLE);
        tituloDescripcionTextView.setVisibility(View.VISIBLE);
        imagenProductoRow.setVisibility(View.VISIBLE);

        switch (buttonNumber) {
            case 1:
                tituloDireccionTextView.setText(getString(R.string.titulo_direccion1));
                subtituloDireccionTextView.setText(getString(R.string.titulo_subdireccion1));
                tituloDescripcionTextView.setText(getString(R.string.titulo_descripcion1));
                imagenProductoRow.setImageResource(R.drawable.sedecentro);

                break;
            case 2:
                tituloDireccionTextView.setText(getString(R.string.titulo_direccion2));
                subtituloDireccionTextView.setText(getString(R.string.titulo_subdireccion2));
                tituloDescripcionTextView.setText(getString(R.string.titulo_descripcion2));
                imagenProductoRow.setImageResource(R.drawable.sedesmp);
                break;
            case 3:
                tituloDireccionTextView.setText(getString(R.string.titulo_direccion3));
                subtituloDireccionTextView.setText(getString(R.string.titulo_subdireccion3));
                tituloDescripcionTextView.setText(getString(R.string.titulo_descripcion3));
                imagenProductoRow.setImageResource(R.drawable.sedecallao);
                break;
            case 4:
                tituloDireccionTextView.setText(getString(R.string.titulo_direccion4));
                subtituloDireccionTextView.setText(getString(R.string.titulo_subdireccion4));
                tituloDescripcionTextView.setText(getString(R.string.titulo_descripcion4));
                imagenProductoRow.setImageResource(R.drawable.sedeate);
                break;
        }
    }
}
