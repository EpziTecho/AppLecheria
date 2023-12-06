package com.example.lecheriaapp.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.List;

// ProductosAdapter.java
public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> {

    private Context context;
    private List<ProductoModel> productos;

    public ProductosAdapter(Context context, List<ProductoModel> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_productos_detalle_reserva, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoModel producto = productos.get(position);

        // Cargar la imagen con Glide
        Glide.with(context)
                .load(producto.getImageUrl())  // Asegúrate de tener un método getUrlImagen() en tu modelo
                .into(holder.imageViewProducto);

        // Resto del código para establecer otros datos en el ViewHolder
        holder.textViewNombreProducto.setText(producto.getNombre());
        holder.textViewCantidad.setText("" + producto.getCantidad());
        holder.textViewPrecio.setText("S/ " + producto.getPrecio());
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    // ViewHolder para representar cada item del RecyclerView
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreProducto;
        ImageView imageViewProducto;
        TextView textViewCantidad;
        TextView textViewPrecio;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreProducto = itemView.findViewById(R.id.textViewNombreProducto);
            imageViewProducto = itemView.findViewById(R.id.imageViewProducto);
            textViewCantidad = itemView.findViewById(R.id.textViewCantidad);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecio);
            ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                    .toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED, 60)
                    .build();
            MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
            ViewCompat.setBackground(textViewCantidad, shapeDrawable);
        }
    }
}
