package com.example.lecheriaapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lecheriaapp.Modelo.ReservaModel;
import com.example.lecheriaapp.R;
import java.util.ArrayList;
import java.util.List;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder> {
    private List<ReservaModel> reservasList;

    public ReservasAdapter() {
        this.reservasList = new ArrayList<>();
    }

    public ReservasAdapter(List<ReservaModel> reservasList) {
        this.reservasList = reservasList;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        ReservaModel reserva = reservasList.get(position);
        holder.bind(reserva);
    }

    @Override
    public int getItemCount() {
        return reservasList.size();
    }

    public void actualizarReservas(List<ReservaModel> nuevasReservas) {
        reservasList.clear();
        reservasList.addAll(nuevasReservas);
        notifyDataSetChanged();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombreUsuario;
        private TextView textViewFecha;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreUsuario = itemView.findViewById(R.id.textViewNombreUsuario);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
        }

        public void bind(ReservaModel reserva) {
            textViewNombreUsuario.setText(reserva.getUserId());
            textViewFecha.setText(reserva.getFecha());
        }
    }
}
