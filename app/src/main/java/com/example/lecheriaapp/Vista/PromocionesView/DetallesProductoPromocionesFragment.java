package com.example.lecheriaapp.Vista.PromocionesView;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Adaptadores.RecyclerProductoAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.PromocionesPresenter.ProductosPromocionesPresenter;
import com.example.lecheriaapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DetallesProductoPromocionesFragment extends Fragment {
    private TextView nombreProductoRow;
    private TextView caloriasProductoRow;
    private TextView precioProductoRow;
    private TextView disponibilidadProductoRow;
    private TextView estadoProductoRow;
    private TextView descripcionProductoRow,QproductoRow;
    private Button btnFavorito;
    private Button btnQuieroPromo;
    private ShapeableImageView imagenProductoRow;
    private RecyclerView recyclerView2;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<ProductoModel> productoModelList;
    private RecyclerProductoAdapter adapter;
    private String codigoQR;
    public DetallesProductoPromocionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalles_producto_promociones, container, false);
        recyclerView2 = view.findViewById(R.id.recycler_productos_relacionados);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ProductosPromocionesPresenter presenter = new ProductosPromocionesPresenter(getContext(), mDatabase, mAuth);
        presenter.cargarRecyclerProductosRelacionados(recyclerView2);//
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí
        adapter = new RecyclerProductoAdapter(getContext(), R.layout.recycler_horizontal_productos_relacionados, productoModelList);
        recyclerView2.setAdapter(adapter);
        // Initialize views
        nombreProductoRow = view.findViewById(R.id.nombreProductoRow);
        precioProductoRow = view.findViewById(R.id.precioProductoRow);
        descripcionProductoRow = view.findViewById(R.id.descripcionProductoRow);
        //QproductoRow = view.findViewById(R.id.QrProductoRow);
        imagenProductoRow = view.findViewById(R.id.imagenProductoRow);
        btnQuieroPromo = view.findViewById(R.id.btn_quiero_promo);

        // Obtener los argumentos pasados al fragmento
        Bundle args = getArguments();
        if (args != null) {
            // Obtener los datos del producto seleccionado
            String nombre = args.getString("nombre");
            String precio = args.getString("precio");
            String descripcion = args.getString("ingredientes");
            // Obtener la URL de la imagen del producto
            String imageUrl = args.getString("imageUrl");
            codigoQR = args.getString("codigoQR");
            // Asignar los datos a las vistas
            nombreProductoRow.setText(nombre);
            precioProductoRow.setText("S/. " + precio);
            descripcionProductoRow.setText(descripcion);
            //QproductoRow.setText(codigoQR);
            // Cargar la imagen utilizando Glide
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(imagenProductoRow);
        }

        // Configurar el click listener del botón "Quiero Promo"
        btnQuieroPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar al fragmento QrFragment
                //enviar datos a qr
                Bundle args = new Bundle();
                args.putString("nombre", nombreProductoRow.getText().toString());
                args.putString("precio", precioProductoRow.getText().toString());
                //enviar el url de codigoQr
                args.putString("codigoQR", codigoQR);
                QrFragment qrFragment = new QrFragment();
                qrFragment.setArguments(args);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, qrFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        return view;
    }
}
