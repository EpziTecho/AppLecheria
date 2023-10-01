package com.example.lecheriaapp.Vista.FavoritosUsuarioView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Adaptadores.RecyclerProductoAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoritosUsuarioFragment extends Fragment implements RecyclerProductoAdapter.FavoritosUpdateListener {

    private RecyclerView mRecyclerView;
    private RecyclerProductoAdapter mAdapter;
    private DatabaseReference mDatabase;

    public FavoritosUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritosusuario, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_favoritos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarFavoritos();

        return view;
    }

    private void cargarFavoritos() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String clienteId = user.getUid();
            DatabaseReference usuarioRef = mDatabase.child("Usuarios").child(clienteId);
            DatabaseReference favoritosRef = usuarioRef.child("Favoritos");

            favoritosRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<ProductoModel> favoritosList = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        ProductoModel producto = dataSnapshot.getValue(ProductoModel.class);
                        favoritosList.add(producto);
                    }

                    mAdapter = new RecyclerProductoAdapter(getContext(), R.layout.item_favorito, new ArrayList<>(favoritosList));
                    mAdapter.setFavoritosUpdateListener(this); // Establecer el listener
                    mRecyclerView.setAdapter(mAdapter);
                }
            });
        }
    }

    @Override
    public void onFavoritosUpdated() {
        cargarFavoritos(); // Actualizar la lista de favoritos cuando se produzca un cambio
    }
}
