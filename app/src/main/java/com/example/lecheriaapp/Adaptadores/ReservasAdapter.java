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
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ReservaModel reserva);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ReservasAdapter() {
        this.reservasList = new ArrayList<>();
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

        // Manejar clics en el elemento
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(reserva);
                }
            }
        });
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
        private TextView textViewIdReserva;
        private TextView textViewFecha;
        private TextView textViewTotal;
        private TextView textViewEstado;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIdReserva = itemView.findViewById(R.id.textViewIdReserva);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
        }

        public void bind(ReservaModel reserva) {
            textViewIdReserva.setText(String.valueOf(reserva.getReservaId()));
            textViewFecha.setText(reserva.getFecha());
            textViewEstado.setText(reserva.getEstado());
            textViewTotal.setText(String.valueOf(reserva.getTotal()));
        }
    }
}
