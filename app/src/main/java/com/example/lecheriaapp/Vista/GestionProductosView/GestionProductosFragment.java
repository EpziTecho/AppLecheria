package com.example.lecheriaapp.Vista.GestionProductosView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresenterGestionProductos;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class GestionProductosFragment extends Fragment implements View.OnClickListener {

    private Button mBtnAgregarProducto;
    private PresenterGestionProductos presenterGestionProductos;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private androidx.appcompat.widget.SearchView searchView;

    public GestionProductosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestionproductos, container, false);
        presenterGestionProductos = new PresenterGestionProductos(getActivity(), mDatabase, mAuth);

        mBtnAgregarProducto = view.findViewById(R.id.add_button);
        mBtnAgregarProducto.setOnClickListener(this);

        searchView = (androidx.appcompat.widget.SearchView) view.findViewById(R.id.buscar_prod);
        recyclerView = view.findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        presenterGestionProductos.cargarRecyclerViewGestion(recyclerView);
        presenterGestionProductos.setupSearchView(searchView, recyclerView);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AgregarProductoFragment()).commit();
                break;
        }
    }
}
