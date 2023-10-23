package com.example.lecheriaapp.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import java.util.List;

public class ProductosReservadosAdapter extends RecyclerView.Adapter<ProductosReservadosAdapter.ProductoViewHolder> {

    private Context context;
    public List<ProductoModel> listaProductos;

    public ProductosReservadosAdapter(Context context, List<ProductoModel> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemproductoreserva, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoModel producto = listaProductos.get(position);

        holder.nombreTextView.setText("Nombre: " + producto.getNombre());
        holder.precioTextView.setText("Precio: $" + producto.getPrecio());
        //imagen viene con glide
        Glide.with(context)
                .load(producto.getImageUrl())
                .placeholder(R.drawable.ic_home) // Cambia ic_home por el ID de tu imagen de carga
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;
        public ImageView imageView;
        public TextView precioTextView;


        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreTextView = itemView.findViewById(R.id.textViewNombre);
            precioTextView = itemView.findViewById(R.id.textViewPrecio);
            imageView = itemView.findViewById(R.id.imageViewProducto);
        }
    }

}
