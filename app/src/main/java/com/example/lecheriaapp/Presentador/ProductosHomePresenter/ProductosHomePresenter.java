package com.example.lecheriaapp.Presentador.ProductosHomePresenter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Adaptadores.RecyclerProductoAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductosHomePresenter {
    private Context mContext;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerProductoAdapter adapter;


    public ProductosHomePresenter(Context mContext, FirebaseAuth mAuth, DatabaseReference mDatabase) {
        this.mContext = mContext;
        this.mAuth = mAuth;
        this.mDatabase = mDatabase;

    }

    public void cargarRecyclerView(RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (user != null) {
            mDatabase.child("Usuarios").child(user.getUid()).child("rol").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String rol = dataSnapshot.getValue(String.class);

                    if (rol != null && rol.equals("cliente")) {
                        // Cargar productos de usuarios con rol "admin" y "adminSMP"
                        Query query = mDatabase.child("Usuarios").orderByChild("rol").startAt("AdminAte").endAt("AdminSMP");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    String uid = userSnapshot.getKey();
                                    DatabaseReference productosRef = mDatabase.child("Usuarios").child(uid).child("productos");
                                    productosRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                String estado = snapshot.child("estado").getValue(String.class);
                                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                                    ProductoModel productoModel = new ProductoModel();
                                                    productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                                                    productoModel.setEstado(estado);
                                                    productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(String.class)));
                                                    productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                                                    productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                                                    productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                                                    productoModel.setCategoria(snapshot.child("categoria").getValue(String.class));
                                                    productoModel.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                                                    arrayListProductos.add(productoModel);
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Manejar error de lectura de la base de datos
                                        }
                                    });
                                }
                                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Manejar error de lectura de la base de datos
                            }
                        });
                    } else {
                        // Cargar productos del usuario actual
                        mDatabase.child("Usuarios").child(user.getUid()).child("productos").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String estado = snapshot.child("estado").getValue(String.class);
                                    if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                        ProductoModel productoModel = new ProductoModel();
                                        productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                                        productoModel.setEstado(estado);
                                        productoModel.setPrecio(String.valueOf(Float.parseFloat(snapshot.child("precio").getValue(String.class))));
                                        productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                                        productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                                        productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                                        productoModel.setCategoria(snapshot.child("categoria").getValue(String.class));
                                        productoModel.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                                        arrayListProductos.add(productoModel);
                                    }
                                }
                                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // No se ha iniciado sesión, cargar productos de usuarios con el rol "admin" y "adminSMP"
            Query query = mDatabase.child("Usuarios").orderByChild("rol").startAt("Admin Centro").endAt("AdminSMP");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String uid = userSnapshot.getKey();
                        DatabaseReference productosRef = mDatabase.child("Usuarios").child(uid).child("productos");
                        productosRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String estado = snapshot.child("estado").getValue(String.class);
                                    if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                        ProductoModel productoModel = new ProductoModel();
                                        productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                                        productoModel.setEstado(estado);
                                        productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(String.class)));
                                        productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                                        productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                                        productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                                        productoModel.setCategoria(snapshot.child("categoria").getValue(String.class));
                                        productoModel.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                                        arrayListProductos.add(productoModel);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejar error de lectura de la base de datos
                            }
                        });
                    }
                    adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Manejar error de lectura de la base de datos
                }
            });
        }
    }
    public void mostrarProductosAdminCentro(RecyclerView recyclerView) {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Usuarios").orderByChild("rol").startAt("AdminCentro").endAt("AdminCentro");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child("Usuarios").child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    ProductoModel productoModel = new ProductoModel();
                                    productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                                    productoModel.setEstado(estado);
                                    productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(String.class)));
                                    productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                                    productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                                    productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                                    productoModel.setCategoria(snapshot.child("categoria").getValue(String.class));
                                    productoModel.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                                    arrayListProductos.add(productoModel);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }

    public void mostrarProductosAdminSMP(RecyclerView recyclerView) {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Usuarios").orderByChild("rol").startAt("AdminSMP").endAt("AdminSMP");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child("Usuarios").child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    ProductoModel productoModel = new ProductoModel();
                                    productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                                    productoModel.setEstado(estado);
                                    productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(String.class)));
                                    productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                                    productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                                    productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                                    productoModel.setCategoria(snapshot.child("categoria").getValue(String.class));
                                    productoModel.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                                    arrayListProductos.add(productoModel);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }

    public void mostrarProductosAdminCallao(RecyclerView recyclerView) {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Usuarios").orderByChild("rol").startAt("AdminCallao").endAt("AdminCallao");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child("Usuarios").child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    ProductoModel productoModel = new ProductoModel();
                                    productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                                    productoModel.setEstado(estado);
                                    productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(String.class)));
                                    productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                                    productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                                    productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                                    productoModel.setCategoria(snapshot.child("categoria").getValue(String.class));
                                    productoModel.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                                    arrayListProductos.add(productoModel);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }
    public void mostrarProductosAdminAte(RecyclerView recyclerView) {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Usuarios").orderByChild("rol").startAt("AdminAte").endAt("AdminAte");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child("Usuarios").child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    ProductoModel productoModel = new ProductoModel();
                                    productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                                    productoModel.setEstado(estado);
                                    productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(String.class)));
                                    productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                                    productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                                    productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                                    productoModel.setCategoria(snapshot.child("categoria").getValue(String.class));
                                    productoModel.setImageUrl(snapshot.child("imageUrl").getValue(String.class));
                                    arrayListProductos.add(productoModel);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }
    public void mostrarProductosPorCategoriaAte(String categoria, RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        Query query = mDatabase.orderByChild("rol").startAt("AdminAte").endAt("AdminAte");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    String categoriaProducto = snapshot.child("categoria").getValue(String.class);
                                    if (categoriaProducto != null && categoriaProducto.equalsIgnoreCase(categoria)) {
                                        ProductoModel productoModel = snapshot.getValue(ProductoModel.class);
                                        arrayListProductos.add(productoModel);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }

    public void mostrarProductosPorCategoriaCentro(String categoria, RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        Query query = mDatabase.orderByChild("rol").startAt("AdminCentro").endAt("AdminCentro");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    String categoriaProducto = snapshot.child("categoria").getValue(String.class);
                                    if (categoriaProducto != null && categoriaProducto.equalsIgnoreCase(categoria)) {
                                        ProductoModel productoModel = snapshot.getValue(ProductoModel.class);
                                        arrayListProductos.add(productoModel);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }
    public void mostrarProductosPorCategoriaSMP(String categoria, RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        Query query = mDatabase.orderByChild("rol").startAt("AdminSMP").endAt("AdminSMP");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    String categoriaProducto = snapshot.child("categoria").getValue(String.class);
                                    if (categoriaProducto != null && categoriaProducto.equalsIgnoreCase(categoria)) {
                                        ProductoModel productoModel = snapshot.getValue(ProductoModel.class);
                                        arrayListProductos.add(productoModel);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }
    public void mostrarProductosPorCategoriaCallao(String categoria, RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        Query query = mDatabase.orderByChild("rol").startAt("AdminCallao").endAt("AdminCallao");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    String categoriaProducto = snapshot.child("categoria").getValue(String.class);
                                    if (categoriaProducto != null && categoriaProducto.equalsIgnoreCase(categoria)) {
                                        ProductoModel productoModel = snapshot.getValue(ProductoModel.class);
                                        arrayListProductos.add(productoModel);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }

    public void mostrarProductosPorCategoriaEnTodasLasSedes(String categoria, RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    DatabaseReference productosRef = mDatabase.child(uid).child("productos");
                    productosRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String estado = snapshot.child("estado").getValue(String.class);
                                if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                    String categoriaProducto = snapshot.child("categoria").getValue(String.class);
                                    if (categoriaProducto != null && categoriaProducto.equalsIgnoreCase(categoria)) {
                                        ProductoModel productoModel = snapshot.getValue(ProductoModel.class);
                                        arrayListProductos.add(productoModel);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Manejar error de lectura de la base de datos
                        }
                    });
                }
                adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de lectura de la base de datos
            }
        });
    }


}
