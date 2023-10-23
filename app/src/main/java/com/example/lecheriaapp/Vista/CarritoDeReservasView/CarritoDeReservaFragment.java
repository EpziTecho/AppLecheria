package com.example.lecheriaapp.Vista.CarritoDeReservasView;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lecheriaapp.Adaptadores.ProductosReservadosAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.CarritoReservaPresenter.CarritoReservaUsuarioPresenter;
import com.example.lecheriaapp.R;

import java.util.ArrayList;
import java.util.List;

public class CarritoDeReservaFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductosReservadosAdapter productosAdapter;

    public CarritoDeReservaFragment() {
        // Constructor vacío
    }

    public static CarritoDeReservaFragment newInstance() {
        return new CarritoDeReservaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito_de_reserva, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProductosReservados); // Asigna el RecyclerView desde tu diseño

        // Configura el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Inicializa el adaptador
        productosAdapter = new ProductosReservadosAdapter(getContext(), new ArrayList<ProductoModel>());
        recyclerView.setAdapter(productosAdapter);

        // Llama al método para obtener los productos de la reserva temporal
        obtenerProductosEnReservaTemporal();

        return view;
    }

    private void obtenerProductosEnReservaTemporal() {
        CarritoReservaUsuarioPresenter presenter = new CarritoReservaUsuarioPresenter();
        presenter.obtenerProductosEnReservaTemporal(new CarritoReservaUsuarioPresenter.OnProductosObtenidosListener() {
            @Override
            public void onProductosObtenidos(List<ProductoModel> productos) {
                actualizarListaDeProductos(productos);
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
            }
        });
    }


    private void actualizarListaDeProductos(List<ProductoModel> productos) {
        productosAdapter.listaProductos = productos; // Actualiza la lista de productos
        productosAdapter.notifyDataSetChanged(); // Notifica los cambios al adaptador
    }
}