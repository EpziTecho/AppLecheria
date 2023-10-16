package com.example.lecheriaapp.Presentador.ReservasProductosUsuarioPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class ReservarProductosUsuario {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

public ReservarProductosUsuario() {
    mAuth = FirebaseAuth.getInstance();
    mDatabase = FirebaseDatabase.getInstance().getReference();

}

    public void reservarProducto(String productoId, String nombre, String precio, String estado, String ingredientes, String calorias, String disponibilidad, String categoria) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference reservaRef = mDatabase.child("reservas_temporal").child(userId).push();
            reservaRef.child("productoId").setValue(productoId);
            reservaRef.child("nombre").setValue(nombre);
            reservaRef.child("precio").setValue(precio);
            reservaRef.child("estado").setValue(estado);
            reservaRef.child("ingredientes").setValue(ingredientes);
            reservaRef.child("calorias").setValue(calorias);
            reservaRef.child("disponibilidad").setValue(disponibilidad);
            reservaRef.child("categoria").setValue(categoria);

        }
    }
}



