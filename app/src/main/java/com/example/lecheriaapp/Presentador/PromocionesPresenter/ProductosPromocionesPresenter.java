package com.example.lecheriaapp.Presentador.PromocionesPresenter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Adaptadores.RecyclerProductoAdapter;
import com.example.lecheriaapp.Adaptadores.RecyclerProductoGestionAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductosPromocionesPresenter implements View.OnClickListener {
    private Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerProductoAdapter adapter;

    public ProductosPromocionesPresenter(Context mContext, DatabaseReference mDatabase, FirebaseAuth mAuth) {
        this.mContext = mContext;
        this.mDatabase = mDatabase;
        this.mAuth = mAuth;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    public void cargarRecyclerProductosPromociones(RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot promocionesSnapshot = userSnapshot.child("promociones");
                    for (DataSnapshot productoSnapshot : promocionesSnapshot.getChildren()) {
                        ProductoModel productoModel = new ProductoModel();
                        productoModel.setNombre(productoSnapshot.child("nombre").getValue(String.class));
                        productoModel.setEstado(productoSnapshot.child("estado").getValue(String.class));
                        productoModel.setPrecio(String.valueOf(productoSnapshot.child("precio").getValue(String.class)));
                        productoModel.setIngredientes(productoSnapshot.child("ingredientes").getValue(String.class));
                        productoModel.setDisponibilidad(productoSnapshot.child("disponibilidad").getValue(String.class));
                        productoModel.setCaloria(productoSnapshot.child("caloria").getValue(String.class));
                        productoModel.setCategoria(productoSnapshot.child("categoria").getValue(String.class));
                        productoModel.setImageUrl(productoSnapshot.child("imageUrl").getValue(String.class));
                        productoModel.setCodigoQR(productoSnapshot.child("codigoQR").getValue(String.class));
                        arrayListProductos.add(productoModel);
                    }
                }

                adapter = new RecyclerProductoAdapter(mContext, R.layout.item_producto_promocion, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event if needed
            }
        });
    }
    public void cargarRecyclerProductosRelacionados(RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot promocionesSnapshot = userSnapshot.child("promociones");
                    for (DataSnapshot productoSnapshot : promocionesSnapshot.getChildren()) {
                        ProductoModel productoModel = new ProductoModel();
                        productoModel.setNombre(productoSnapshot.child("nombre").getValue(String.class));
                        productoModel.setEstado(productoSnapshot.child("estado").getValue(String.class));
                        productoModel.setPrecio(String.valueOf(productoSnapshot.child("precio").getValue(String.class)));
                        productoModel.setIngredientes(productoSnapshot.child("ingredientes").getValue(String.class));
                        productoModel.setDisponibilidad(productoSnapshot.child("disponibilidad").getValue(String.class));
                        productoModel.setCaloria(productoSnapshot.child("caloria").getValue(String.class));
                        productoModel.setCategoria(productoSnapshot.child("categoria").getValue(String.class));
                        productoModel.setImageUrl(productoSnapshot.child("imageUrl").getValue(String.class));
                        arrayListProductos.add(productoModel);
                    }
                }

                adapter = new RecyclerProductoAdapter(mContext, R.layout.recycler_horizontal_productos_relacionados, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event if needed
            }
        });
    }
}

