package com.example.lecheriaapp.Vista.ReservasUsuariosView;

import android.os.Bundle;
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

import java.util.List;

public class ReservasUsuariosFragment extends Fragment implements GestionReservasView {
    private RecyclerView recyclerView;
    private ReservasAdapter reservasAdapter;
    private PresentadorGestionReservas presentadorGestionReservas;

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
        reservasAdapter = new ReservasAdapter(); // Pasar una lista vacía al constructor
        recyclerView.setAdapter(reservasAdapter);

        presentadorGestionReservas = new PresentadorGestionReservas(this);
        presentadorGestionReservas.obtenerReservasDesdeFirebase(); // Llamada para obtener las reservas

        return view;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        // Implementa cómo deseas mostrar el mensaje en tu fragmento
    }

    @Override
    public void mostrarReservas(List<ReservaModel> reservas) {
        reservasAdapter.actualizarReservas(reservas);
    }
}
