package com.example.lecheriaapp.Vista.HomeView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.ProductosHomePresenter.ProductosHomePresenter;
import com.example.lecheriaapp.Adaptadores.ProductosAdapter;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ProductosAdapter adapter;
    private ArrayList<ProductoModel> productoModelList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProductosHomePresenter productosHomePresenter;

    private Spinner spinnerSedes;
    private Spinner rightSpinner;
    private Spinner spinnerCategorias;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewHome);
        // Obtener la referencia a los Spinners del layout
        spinnerSedes = view.findViewById(R.id.leftSpinner);
        rightSpinner = view.findViewById(R.id.rightSpinner);
        spinnerCategorias = view.findViewById(R.id.rightSpinner);
        // Crear un ArrayAdapter para cada Spinner con los datos deseados
        ArrayAdapter<CharSequence> leftAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.left_spinner_items, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> rightAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.right_spinner_items, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> categoriasAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.right_spinner_items, android.R.layout.simple_spinner_item);
        // Especificar el diseño del menú desplegable de los Spinners
        leftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Establecer los ArrayAdapter como adaptador de cada Spinner
        spinnerSedes.setAdapter(leftAdapter);
        rightSpinner.setAdapter(rightAdapter);
        spinnerCategorias.setAdapter(categoriasAdapter);
        productosHomePresenter = new ProductosHomePresenter(getActivity(), mAuth, mDatabase);

        // Agregar listener al spinner de Sedes
        spinnerSedes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sede = parent.getItemAtPosition(position).toString();
                if (sede.equals("Todas las sedes")) {
                    initRecyclerTodos();
                } else if (sede.equals("Centro")) {
                    initRecyclerCentro();
                } else if (sede.equals("SMP")) {
                    initRecyclerSMP();
                } else if (sede.equals("Callao")) {
                    initRecyclerCallao();
                } else if (sede.equals("Ate")) {
                    initRecyclerAte();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó ningún item
            }
        });

        // Agregar listener al spinner de Categorías
        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoria = parent.getItemAtPosition(position).toString();
                mostrarProductosPorCategoria(categoria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó ninguna categoría
            }
        });

        obtenerRolUsuario();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerTodos();
    }

    private void initRecyclerTodos() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí
        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);
        productosHomePresenter.cargarRecyclerView(recyclerView);
    }

    private void initRecyclerCentro() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);

        productosHomePresenter.mostrarProductosAdminCentro(recyclerView);
    }

    private void initRecyclerSMP() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);

        productosHomePresenter.mostrarProductosAdminSMP(recyclerView);
    }

    private void initRecyclerCallao() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);

        productosHomePresenter.mostrarProductosAdminCallao(recyclerView);
    }

    private void initRecyclerAte() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);

        productosHomePresenter.mostrarProductosAdminAte(recyclerView);
    }

    private void mostrarProductosPorCategoria(String categoria) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);

        // Obtener las selecciones de los Spinners
        String sede = spinnerSedes.getSelectedItem().toString();
        String categoriaSeleccionada = spinnerCategorias.getSelectedItem().toString();

        if (sede.equals("Todas las sedes") && categoriaSeleccionada.equals("Todos los productos")) {
            // Mostrar todos los productos de todas las sedes
            initRecyclerTodos();
        } else {
            // Filtrar productos por sede y categoría
            if (sede.equals("Todas las sedes")) {
                // Filtrar por categoría en todas las sedes
                productosHomePresenter.mostrarProductosPorCategoriaEnTodasLasSedes(categoria, recyclerView);
            } else if (categoriaSeleccionada.equals("Todos los productos")) {
                // Mostrar todos los productos de la sede seleccionada
                if (sede.equals("Centro")) {
                    initRecyclerCentro();
                } else if (sede.equals("SMP")) {
                    initRecyclerSMP();
                } else if (sede.equals("Callao")) {
                    initRecyclerCallao();
                } else if (sede.equals("Ate")) {
                    initRecyclerAte();
                }
            } else {
                // Filtrar por categoría en la sede seleccionada
                if (sede.equals("Centro")) {
                    productosHomePresenter.mostrarProductosPorCategoriaCentro(categoria, recyclerView);
                } else if (sede.equals("SMP")) {
                    productosHomePresenter.mostrarProductosPorCategoriaSMP(categoria, recyclerView);
                } else if (sede.equals("Callao")) {
                    productosHomePresenter.mostrarProductosPorCategoriaCallao(categoria, recyclerView);
                } else if (sede.equals("Ate")) {
                    productosHomePresenter.mostrarProductosPorCategoriaAte(categoria, recyclerView);
                }
            }
        }
    }
    private void obtenerRolUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String rol = dataSnapshot.child("rol").getValue(String.class);
                        configurarSpinnerSegunRol(rol);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Maneja el error al obtener los datos del usuario
                }
            };

            usersRef.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private void configurarSpinnerSegunRol(String rol) {
        if (rol.equals("AdminCallao")) {
            spinnerSedes.setSelection(4);  // Poner en "Callao"
            spinnerSedes.setEnabled(false); // Bloquear el spinner
        } else if (rol.equals("AdminCentro")) {
            spinnerSedes.setSelection(1);  // Poner en "Centro"
            spinnerSedes.setEnabled(false); // Bloquear el spinner
        } else if (rol.equals("AdminSMP")) {
            spinnerSedes.setSelection(2);  // Poner en "SMP"
            spinnerSedes.setEnabled(false); // Bloquear el spinner
        } else if (rol.equals("AdminAte")) {
            spinnerSedes.setSelection(3);  // Poner en "Ate"
            spinnerSedes.setEnabled(false); // Bloquear el spinner
        }
    }

    @Override
    public void onClick(View view) {

    }
}
