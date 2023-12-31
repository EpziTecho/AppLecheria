package com.example.lecheriaapp.Vista.CarritoDeReservasView;

import android.app.ProgressDialog;
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
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private TextView textViewFechaHoraReserva;
    private TextView textViewNombreUsuario;
    private TextView textViewSubtotal;
    private TextView textViewTotal;
    private Button btnFinalizarReserva;
    private Button btnCancelarReserva;

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
        btnCancelarReserva = view.findViewById(R.id.btnCancelarReserva);
        // Establece un OnClickListener para el botón "Finalizar Reserva"
        btnFinalizarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalizarReservaTemporal();
                mostrarDialogoPreparandoReserva();
            }
        });
        // Configura el OnClickListener para el botón "Cancelar Reserva"
        btnCancelarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarReservaTemporal();
            }
        });
        obtenerEstadoReservaTemporal();

        return view;
    }
    private void cancelarReservaTemporal() {
        presenter.cancelarReservaTemporal(new CarritoReservaUsuarioPresenter.OnReservaCanceladaListener() {
            @Override
            public void onReservaCancelada() {
                // La reserva se ha cancelado correctamente
                Toast.makeText(getContext(), "Reserva cancelada correctamente", Toast.LENGTH_SHORT).show();
                // Redirige a HomeFragment u otra ubicación deseada
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario
                Toast.makeText(getContext(), "Error al cancelar la reserva: " + mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarDialogoPreparandoReserva() {
        // Crea un ProgressDialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Preparando reserva...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void ocultarDialogoPreparandoReserva() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void finalizarReservaTemporal() {
        presenter.finalizarReservaTemporal(new CarritoReservaUsuarioPresenter.OnReservaFinalizadaListener() {
            @Override
            public void onReservaFinalizada() {
                // La reserva se ha finalizado correctamente, puedes realizar acciones adicionales si es necesario
                Toast.makeText(getContext(), "Reserva finalizada correctamente", Toast.LENGTH_SHORT).show();
                // Oculta el ProgressDialog
                ocultarDialogoPreparandoReserva();
                // Redirige a ReservaQrFragment

                mostrarReservaQrFragment();
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar errores si es necesario, por ejemplo, mostrar un mensaje de error
                //ocultarDialogoPreparandoReserva();
                ocultarDialogoPreparandoReserva();
            }
        });
    }

    private void mostrarReservaQrFragment() {
        // Obtén el código de reserva y el nombre de usuario de los TextView
        String codigoReserva = textViewIdReserva.getText().toString();
        String nombreUsuario = textViewNombreUsuario.getText().toString();

        // Crea una instancia del fragmento ReservaQrFragment y pasa la URL del código QR,
        // el código de reserva y el nombre de usuario reales
        ReservaQrFragment reservaQrFragment = ReservaQrFragment.newInstance(
                "https://firebasestorage.googleapis.com/v0/b/lalecheria.appspot.com/o/qrcodes%2Fqr_code_-NjP3LSYyRkB5VYd4Do8.png?alt=media&token=be6d7c32-1ab4-42ff-8047-ce7c4059fbe9",
                codigoReserva,
                nombreUsuario
        );

        // Reemplaza el fragmento actual con ReservaQrFragment
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, reservaQrFragment)
                .commit();
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
                    textViewIdReserva.setText("N° de Reserva: " + idReservaTemporal);
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
                textViewNombreUsuario.setText("Cliente: " + nombreUsuario);
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
                textViewFechaHoraReserva.setText("Fecha de Reserva: " + fechaReserva);
            }

            @Override
            public void onError(String mensajeError) {
                textViewFechaHoraReserva.setText("Fecha de Reserva: No encontrada");
            }
        });
    }

    private void obtenerSubtotalYTotal() {
        presenter.obtenerSubtotalYTotal(new CarritoReservaUsuarioPresenter.OnSubtotalYTotalObtenidosListener() {
            @Override
            public void onSubtotalYTotalObtenidos(double subtotal, double total) {
                textViewSubtotal.setText(""+subtotal);
                textViewTotal.setText("" + total);
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