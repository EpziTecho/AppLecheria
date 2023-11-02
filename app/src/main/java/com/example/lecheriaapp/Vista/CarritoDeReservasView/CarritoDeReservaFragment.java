package com.example.lecheriaapp.Vista.CarritoDeReservasView;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private TextView textViewFechaHoraReserva;
    private TextView textViewNombreUsuario;
    private TextView textViewSubtotal;
    private TextView textViewTotal;

    private TextView textViewIdReserva;
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
        // Obtener los TextView para mostrar la información
        textViewIdReserva = view.findViewById(R.id.textViewReservaID);
        textViewFechaHoraReserva = view.findViewById(R.id.textViewFechaHoraReserva);
        textViewNombreUsuario = view.findViewById(R.id.textViewNombreUsuario);
        textViewSubtotal = view.findViewById(R.id.textViewSubtotal);
        textViewTotal = view.findViewById(R.id.textViewTotal);


        // Obtener el nombre del usuario, la fecha, el subtotal y el total (de donde corresponda)
        String nombreUsuario = obtenerNombreUsuario();
        String fechaHoraReserva = obtenerFechaReservaTemporal();
        double subtotal = obtenerSubtotalYTotal();
        String idReserva = obtenerIdReservaTemporal();
        //double total = obtenerTotal();

        // Asignar los valores a los TextView
        textViewIdReserva.setText("ID de la Reserva: " + idReserva);
        textViewFechaHoraReserva.setText("Fecha y Hora de la Reserva: " + fechaHoraReserva);
        textViewNombreUsuario.setText("Nombre de usuario: " + nombreUsuario);
        textViewSubtotal.setText("Subtotal: " + subtotal);
        //textViewTotal.setText("Total: " + total);

        // Llama al método para obtener los productos de la reserva temporal
        obtenerProductosEnReservaTemporal();

        return view;
    }
    // Métodos para obtener la información

    private String obtenerIdReservaTemporal(){
        CarritoReservaUsuarioPresenter presenter = new CarritoReservaUsuarioPresenter();
        presenter.obtenerIdReservaTemporal(new CarritoReservaUsuarioPresenter.OnIdReservaTemporalObtenidoListener() {

            @Override
            public void onIdReservaTemporalObtenido(String idReservaTemporal) {
                // Asigna el valor del id de la reserva al TextView correspondiente
                textViewIdReserva.setText("ID de la Reserva: " + idReservaTemporal);
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
            }
        });
        return null;
    }
    private String obtenerNombreUsuario() {
        CarritoReservaUsuarioPresenter presenter = new CarritoReservaUsuarioPresenter();
        presenter.obtenerNombreUsuario(new CarritoReservaUsuarioPresenter.OnNombreUsuarioObtenidoListener() {
            @Override
            public void onNombreUsuarioObtenido(String nombreUsuario) {
                // Asigna el valor del nombre de usuario al TextView correspondiente
                textViewNombreUsuario.setText("Nombre de usuario: " + nombreUsuario);
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
            }
        });
        return null;
    }
    private String obtenerFechaReservaTemporal() {
        CarritoReservaUsuarioPresenter presenter = new CarritoReservaUsuarioPresenter();
        presenter.obtenerFechaReservaTemporal(new CarritoReservaUsuarioPresenter.OnFechaReservaObtenidoListener() {
            @Override
            public void onFechaReservaObtenido(String fechaReserva) {
                // Asigna el valor de la fecha de reserva al TextView correspondiente
                textViewFechaHoraReserva.setText("Fecha y Hora de la Reserva: " + fechaReserva);
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario, por ejemplo, mostrar un mensaje de error en caso de que no se encuentre una reserva temporal
                textViewFechaHoraReserva.setText("Fecha y Hora de la Reserva: No encontrada");
            }
        });
        return null;
    }


    private double obtenerSubtotalYTotal() {
        CarritoReservaUsuarioPresenter presenter = new CarritoReservaUsuarioPresenter();
        presenter.obtenerSubtotalYTotal(new CarritoReservaUsuarioPresenter.OnSubtotalYTotalObtenidosListener() {
            @Override
            public void onSubtotalYTotalObtenidos(double subtotal, double total) {
                // Asigna los valores de subtotal y total a los TextView correspondientes
                textViewSubtotal.setText("Subtotal: " + subtotal);
                textViewTotal.setText("Total: " + total);
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
            }
        });
        return 0;
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