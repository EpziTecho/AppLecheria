package com.example.lecheriaapp.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder> {

    private Context context;
    private List<ProductoModel> listaProductos;

    public ProductosAdapter(Context context, List<ProductoModel> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    // Método que crea una nueva vista de item_producto al ser requerida por el RecyclerView
    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla la vista del layout item_producto.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductosViewHolder(view);
    }

    // Método que actualiza el contenido de una vista de item_producto al ser requerido por el RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ProductosViewHolder holder, int position) {
        // Obtiene los datos del producto en la posición indicada
        ProductoModel producto = listaProductos.get(position);

        // Actualiza los elementos de la vista con los datos del producto
        holder.nombre.setText(producto.getNombre());
        holder.descripcion.setText(producto.getCaloria());
        holder.precio.setText(producto.getPrecio());
        // Carga la imagen del producto utilizando Glide, una librería para la carga de imágenes
        Glide.with(context).load(producto.getImageUrl()).into(holder.imagen);
    }

    // Método que indica la cantidad de elementos que hay en la lista de productos
    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Clase que representa una vista de item_producto
    public static class ProductosViewHolder extends RecyclerView.ViewHolder {

        public ImageView imagen;
        public TextView nombre;
        public TextView descripcion;
        public TextView precio;
        public TextView disponibilidad;

        public ProductosViewHolder(@NonNull View itemView) {
            super(itemView);

            // Obtiene los elementos de la vista y los asigna a las variables
            imagen = itemView.findViewById(R.id.producto_imagen);
            nombre = itemView.findViewById(R.id.producto_nombre);
            descripcion = itemView.findViewById(R.id.producto_descripcion);
            precio = itemView.findViewById(R.id.producto_precio);
            //disponibilidad = itemView.findViewById(R.id.producto_disponibilidad);
        }
    }
}
