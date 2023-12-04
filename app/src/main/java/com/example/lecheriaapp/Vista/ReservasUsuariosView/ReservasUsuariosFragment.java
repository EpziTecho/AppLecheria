package com.example.lecheriaapp.Vista.ReservasUsuariosView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Adaptadores.ReservasAdapter;
import com.example.lecheriaapp.Modelo.ReservaModel;
import com.example.lecheriaapp.Presentador.GestionReservasPresenter.PresentadorGestionReservas;
import com.example.lecheriaapp.Presentador.GestionReservasPresenter.PresentadorGestionReservas.GestionReservasView;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.DetallesReservaView.DetalleReservaFragment;

import java.util.List;

public class ReservasUsuariosFragment extends Fragment implements GestionReservasView, ReservasAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ReservasAdapter reservasAdapter;
    private PresentadorGestionReservas presentadorGestionReservas;
    private String keyUsuario;
    private String rolUsuario;

    public ReservasUsuariosFragment() {
        // Required empty public constructor
    }

    public static ReservasUsuariosFragment newInstance() {
        return new ReservasUsuariosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservas_usuarios_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewReservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservasAdapter = new ReservasAdapter();
        recyclerView.setAdapter(reservasAdapter);



        // Configurar el manejador de clics en el adaptador
        reservasAdapter.setOnItemClickListener(this);

        presentadorGestionReservas = new PresentadorGestionReservas(this);
        presentadorGestionReservas.obtenerReservasDesdeFirebase(); // Call to get reservations
        presentadorGestionReservas.obtenerKeyUsuario();
        presentadorGestionReservas.obtenerRolUsuario();

        return view;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        // Implement how you want to display the message in your fragment
    }

    @Override
    public void mostrarReservas(List<ReservaModel> reservas) {
        reservasAdapter.actualizarReservas(reservas);
    }

    @Override
    public void mostrarKeyUsuario(String keyUsuario) {
        this.keyUsuario = keyUsuario;
        Log.d("ReservasUsuariosFragment", "Valor de keyUsuario: " + keyUsuario);
    }

    @Override
    public void mostrarRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
        Log.d("ReservasUsuariosFragment", "Valor de rolUsuario: " + rolUsuario);
    }

    // Método implementado desde OnItemClickListener
    @Override
    public void onItemClick(ReservaModel reserva) {
        // Manejar el clic en la reserva
        // Aquí es donde deberías navegar al fragmento de detalles
        Fragment detalleReservaFragment = DetalleReservaFragment.newInstance(reserva);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detalleReservaFragment)
                .addToBackStack(null)
                .commit();
    }
}
