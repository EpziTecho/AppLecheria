package com.example.lecheriaapp.Presentador.FavoritosPresenter;

import com.example.lecheriaapp.Modelo.ProductoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritosUsuarioPresenter {
    private DatabaseReference mDatabase;
    private FavoritosUsuarioListener listener;

    public FavoritosUsuarioPresenter(FavoritosUsuarioListener listener) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.listener = listener;
    }

    public void obtenerFavoritosUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String clienteId = user.getUid();
            DatabaseReference usuarioRef = mDatabase.child("Usuarios").child(clienteId);
            DatabaseReference favoritosRef = usuarioRef.child("Favoritos");

            favoritosRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<ProductoModel> favoritosList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductoModel producto = snapshot.getValue(ProductoModel.class);
                        favoritosList.add(producto);
                    }
                    listener.onFavoritosObtenidos(favoritosList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle database error
                    listener.onFavoritosError(databaseError.getMessage());
                }
            });
        }
    }

    public interface FavoritosUsuarioListener {
        void onFavoritosObtenidos(List<ProductoModel> favoritosList);
        void onFavoritosError(String mensajeError);
    }
}
