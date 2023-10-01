package com.example.lecheriaapp.Vista.PromocionesView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Adaptadores.RecyclerProductoAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.PromocionesPresenter.ProductosPromocionesPresenter;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PromocionesFragment extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerProductoAdapter adapter;
    private RecyclerView recyclerView;

    private ArrayList<ProductoModel> productoModelList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_promociones, container, false);

        // Obtener referencia al RecyclerView en el layout
        recyclerView = root.findViewById(R.id.recycler_productospromocion);

        // Inicializar el Presenter
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ProductosPromocionesPresenter presenter = new ProductosPromocionesPresenter(getContext(), mDatabase, mAuth);

        // Cargar los productos de promociones en el RecyclerView
        presenter.cargarRecyclerProductosPromociones(recyclerView);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerPromociones();
    }

    private void initRecyclerPromociones() {
        //linearlayout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí
        adapter = new RecyclerProductoAdapter(getContext(), R.layout.item_producto_promocion, productoModelList);
        recyclerView.setAdapter(adapter);
    }
}
