package com.example.lecheriaapp.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresentadorEditarProductos;
import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresenterGestionProductos;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.EditarProductosFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RecyclerProductoGestionAdapter extends RecyclerView.Adapter<RecyclerProductoGestionAdapter.ProductoViewGestionHolder> {
    private Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private int layoutResource;
    private ArrayList<ProductoModel> arrayListProductos;
    private PresenterGestionProductos presenterGestionProductos;
    private PresentadorEditarProductos presentadorEditarProductos;
    private static ArrayList<ProductoModel> filteredList;

    public RecyclerProductoGestionAdapter(Context mContext, int layoutResource, ArrayList<ProductoModel> arrayListProductos) {
        this.mContext = mContext;
        this.layoutResource = layoutResource;
        this.arrayListProductos = arrayListProductos;
        this.filteredList = new ArrayList<>(arrayListProductos);
        presenterGestionProductos = new PresenterGestionProductos(mContext, mDatabase, mAuth);
        presentadorEditarProductos = new PresentadorEditarProductos(mContext);
    }

    @NonNull
    @Override
    public ProductoViewGestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);
        return new ProductoViewGestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewGestionHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductoModel productoModel = filteredList.get(position);
        holder.mNombreProducto.setText(productoModel.getNombre());
        holder.mPrecioProducto.setText(productoModel.getPrecio());
        holder.mEstadoProducto.setText(productoModel.getEstado());
        holder.mCaloria.setText(productoModel.getCaloria() + " Kcal");
        Glide.with(mContext)
                .load(productoModel.getImageUrl())
                .placeholder(R.drawable.baseline_table_restaurant_24)
                .error(R.drawable.baseline_book_24)
                .into(holder.mImagenProducto);

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = productoModel.getNombre();
                String calorias = productoModel.getCaloria();
                String precio = productoModel.getPrecio();
                String disponibilidad = productoModel.getDisponibilidad();
                String categoria = productoModel.getCategoria();
                String ingredientes = productoModel.getIngredientes();
                String estado = productoModel.getEstado();
                String imagen = productoModel.getImageUrl();
                String codigoQR = productoModel.getCodigoQR();
                int position = getRealPosition(holder.getAdapterPosition());

                Fragment editarFragment = EditarProductosFragment.newInstance(nombre, calorias, precio, disponibilidad, categoria, ingredientes, estado, imagen,codigoQR, position);
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, editarFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getRealPosition(holder.getAdapterPosition());
                presenterGestionProductos.eliminarProductoFirebase(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String searchText) {
        filteredList.clear();
        if (searchText.trim().isEmpty()) {
            filteredList.addAll(arrayListProductos);
        } else {
            String query = searchText.toLowerCase().trim();
            for (ProductoModel producto : arrayListProductos) {
                if (producto.getNombre().toLowerCase().contains(query)) {
                    filteredList.add(producto);
                }
            }
        }
        notifyDataSetChanged();
    }

    private int getRealPosition(int filteredPosition) {
        ProductoModel filteredItem = filteredList.get(filteredPosition);
        return arrayListProductos.indexOf(filteredItem);
    }

    public static class ProductoViewGestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNombreProducto, mPrecioProducto, mEstadoProducto, mCaloria;
        ImageView mImagenProducto;
        Button btnEditar, btnEliminar;

        public ProductoViewGestionHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNombreProducto = itemView.findViewById(R.id.nombreProductoRow);
            mPrecioProducto = itemView.findViewById(R.id.precioProductoRow);
            mEstadoProducto = itemView.findViewById(R.id.estadoProductoRow);
            mImagenProducto = itemView.findViewById(R.id.imagenProductoRow);
            mCaloria = itemView.findViewById(R.id.caloriaProductoRow);
            btnEditar = itemView.findViewById(R.id.btnEditarProducto);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProducto);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ProductoModel productoModel = filteredList.get(position);
                // Puedes realizar alguna acción al hacer clic en el elemento de la lista
                // por ejemplo, mostrar los detalles del producto
                // Toast.makeText(view.getContext(), productoModel.getNombre(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
