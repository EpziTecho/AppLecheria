package com.example.lecheriaapp.Vista.CarritoDeReservasView;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lecheriaapp.Adaptadores.ProductosReservadosAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.CarritoReservaPresenter.CarritoReservaUsuarioPresenter;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.HomeView.HomeFragment;
import com.example.lecheriaapp.Vista.ReservaQrView.ReservaQrFragment;

import java.util.ArrayList;
import java.util.List;

public class CarritoDeReservaFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewFechaHoraReserva;
    private TextView textViewNombreUsuario;
    private TextView textViewSubtotal;
    private TextView textViewTotal;
    private Button btnFinalizarReserva;

    private TextView textViewIdReserva;
    private ProductosReservadosAdapter productosAdapter;
    private CarritoReservaUsuarioPresenter presenter;

    public CarritoDeReservaFragment() {
        // Constructor vacío
    }

    public static CarritoDeReservaFragment newInstance() {
        return new CarritoDeReservaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito_de_reserva, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProductosReservados);

        // Configura el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Inicializa el adaptador
        productosAdapter = new ProductosReservadosAdapter(getContext(), new ArrayList<ProductoModel>());
        recyclerView.setAdapter(productosAdapter);

        // Inicializa el presentador
        presenter = new CarritoReservaUsuarioPresenter();

        // Obtener los TextView para mostrar la información
        textViewIdReserva = view.findViewById(R.id.textViewReservaID);
        textViewFechaHoraReserva = view.findViewById(R.id.textViewFechaHoraReserva);
        textViewNombreUsuario = view.findViewById(R.id.textViewNombreUsuario);
        textViewSubtotal = view.findViewById(R.id.textViewSubtotal);
        textViewTotal = view.findViewById(R.id.textViewTotal);
        btnFinalizarReserva = view.findViewById(R.id.btnFinalizarReserva);

        // Establece un OnClickListener para el botón "Finalizar Reserva"
        btnFinalizarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarReservaTemporal();
            }
        });

        obtenerEstadoReservaTemporal();

        return view;
    }

    private void finalizarReservaTemporal() {
        presenter.finalizarReservaTemporal(new CarritoReservaUsuarioPresenter.OnReservaFinalizadaListener() {
            @Override
            public void onReservaFinalizada() {
                // La reserva se ha finalizado correctamente, puedes realizar acciones adicionales si es necesario
                Toast.makeText(getContext(), "Reserva finalizada correctamente", Toast.LENGTH_SHORT).show();
                // Redirige a HomeFragment
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReservaQrFragment()).commit();
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario, por ejemplo, mostrar un mensaje de error
            }
        });
    }

    private void obtenerEstadoReservaTemporal() {
        presenter.obtenerEstadoReservaTemporal(new CarritoReservaUsuarioPresenter.OnEstadoReservaObtenidoListener() {
            @Override
            public void onEstadoReservaObtenido(String estado) {
                if ("RESERVA TEMPORAL".equals(estado)) {
                    // El estado es RESERVA TEMPORAL, continúa con el flujo actual
                    obtenerIdReservaTemporal();
                    obtenerNombreUsuario();
                    obtenerFechaReservaTemporal();
                    obtenerSubtotalYTotal();
                    obtenerProductosEnReservaTemporal();
                } else {
                    // El estado no es RESERVA TEMPORAL, redirige a CarritoDeReservarFragment
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarritoDeReservaVacioFragment()).commit();
                }
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
            }
        });
    }

    private void obtenerIdReservaTemporal() {
        presenter.obtenerIdReservaTemporal(new CarritoReservaUsuarioPresenter.OnIdReservaTemporalObtenidoListener() {
            @Override
            public void onIdReservaTemporalObtenido(String idReservaTemporal) {
                if (idReservaTemporal != null) {
                    textViewIdReserva.setText("ID de la Reserva: " + idReservaTemporal);
                }
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
            }
        });
    }

    private void obtenerNombreUsuario() {
        presenter.obtenerNombreUsuario(new CarritoReservaUsuarioPresenter.OnNombreUsuarioObtenidoListener() {
            @Override
            public void onNombreUsuarioObtenido(String nombreUsuario) {
                textViewNombreUsuario.setText("Nombre de usuario: " + nombreUsuario);
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
            }
        });
    }

    private void obtenerFechaReservaTemporal() {
        presenter.obtenerFechaReservaTemporal(new CarritoReservaUsuarioPresenter.OnFechaReservaObtenidoListener() {
            @Override
            public void onFechaReservaObtenido(String fechaReserva) {
                textViewFechaHoraReserva.setText("Fecha y Hora de la Reserva: " + fechaReserva);
            }

            @Override
            public void onError(String mensajeError) {
                textViewFechaHoraReserva.setText("Fecha y Hora de la Reserva: No encontrada");
            }
        });
    }

    private void obtenerSubtotalYTotal() {
        presenter.obtenerSubtotalYTotal(new CarritoReservaUsuarioPresenter.OnSubtotalYTotalObtenidosListener() {
            @Override
            public void onSubtotalYTotalObtenidos(double subtotal, double total) {
                textViewSubtotal.setText("Subtotal: " + subtotal);
                textViewTotal.setText("Total: " + total);
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
            }
        });
    }

    private void obtenerProductosEnReservaTemporal() {
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
        productosAdapter.listaProductos = productos;
        productosAdapter.notifyDataSetChanged();
    }
}