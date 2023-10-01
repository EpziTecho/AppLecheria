package com.example.lecheriaapp.Vista.GestionProductosView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresentadorAgregarProductos;
import com.example.lecheriaapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AgregarProductoFragment extends Fragment implements View.OnClickListener {

    private EditText mTextNombreProducto, mTextCalorias, mTextPrecio, mTextIngredientes;
    private Button mBtnAgregar, mBtnCancelar, mBtnSeleccionarImagen;
    private ImageView mSelectedImageView;
    private Spinner mSpinnerEstado, mSpinnerDisponibilidad, mSpinnerCategoria;
    private ArrayAdapter<String> mEstadoAdapter, mDisponibilidadAdapter, mCategoriaAdapter;
    private static final int GALLERY_REQUEST_CODE = 123;
    private Uri imageUri;
    private PresentadorAgregarProductos mPresentador;
    private StorageReference mStorageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agregarproducto, container, false);

        mTextNombreProducto = view.findViewById(R.id.textNombreProducto);
        mTextCalorias = view.findViewById(R.id.textCalorias);
        mTextPrecio = view.findViewById(R.id.textPrecio);
        mTextIngredientes = view.findViewById(R.id.textIngredientes);
        mBtnAgregar = view.findViewById(R.id.btnAgregar);
        mBtnCancelar = view.findViewById(R.id.btnCancelar);
        mBtnSeleccionarImagen = view.findViewById(R.id.selectImageButton);
        mSelectedImageView = view.findViewById(R.id.selectedImageView);
        mSpinnerEstado = view.findViewById(R.id.spinnerEstado);
        mSpinnerDisponibilidad = view.findViewById(R.id.spinnerDisponibilidad);
        mSpinnerCategoria = view.findViewById(R.id.spinnerCategoria);

        // Configurar adaptador para el spinner de estado
        Resources res = getResources();
        String[] estados = res.getStringArray(R.array.left_spinner_estado);
        mEstadoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, estados);
        mEstadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerEstado.setAdapter(mEstadoAdapter);

        // Configurar adaptador para el spinner de disponibilidad
        String[] disponibilidad = res.getStringArray(R.array.left_spinner_sedes);
        mDisponibilidadAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, disponibilidad);
        mDisponibilidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDisponibilidad.setAdapter(mDisponibilidadAdapter);

        // Configurar adaptador para el spinner de categoría
        String[] categorias = res.getStringArray(R.array.right_spinner_items);
        mCategoriaAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categorias);
        mCategoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategoria.setAdapter(mCategoriaAdapter);

        mBtnAgregar.setOnClickListener(this);
        mBtnSeleccionarImagen.setOnClickListener(this);
        mBtnCancelar.setOnClickListener(this);
        mPresentador = new PresentadorAgregarProductos(requireContext());
        mStorageRef = FirebaseStorage.getInstance().getReference(); // Obtener referencia a Firebase Storage

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAgregar) {
            String nombreProducto = mTextNombreProducto.getText().toString().trim();
            String calorias = mTextCalorias.getText().toString().trim();
            String precio = mTextPrecio.getText().toString().trim();
            String ingredientes = mTextIngredientes.getText().toString().trim();
            String estado = mSpinnerEstado.getSelectedItem().toString();
            String disponibilidad = mSpinnerDisponibilidad.getSelectedItem().toString();
            String categoria = mSpinnerCategoria.getSelectedItem().toString();

            if (!nombreProducto.isEmpty() && !calorias.isEmpty() && !ingredientes.isEmpty() && !estado.isEmpty() && !disponibilidad.isEmpty() && !categoria.isEmpty() && imageUri != null) {
                // Generar el código QR
                Bitmap qrBitmap = generateQRCode(nombreProducto);

                // Subir el código QR a Firebase Storage
                final ProgressDialog dialog = new ProgressDialog(requireContext());
                dialog.setMessage("Agregando producto...");
                dialog.setCancelable(false);
                dialog.show();
                StorageReference qrRef = mStorageRef.child("QrProductos").child(nombreProducto +"-QR"+".png");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] qrData = baos.toByteArray();
                UploadTask uploadTask = qrRef.putBytes(qrData);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Obtener la URL de descarga del código QR subido
                    qrRef.getDownloadUrl().addOnSuccessListener(qrUri -> {
                        String qrUrl = qrUri.toString();

                        // Subir la imagen del producto a Firebase Storage
                        StorageReference imageRef = mStorageRef.child("productos").child(imageUri.getLastPathSegment());
                        UploadTask imageUploadTask = imageRef.putFile(imageUri);
                        imageUploadTask.addOnSuccessListener(imageTaskSnapshot -> {
                            // Obtener la URL de descarga de la imagen del producto subida
                            imageRef.getDownloadUrl().addOnSuccessListener(imageUri -> {
                                String imageUrl = imageUri.toString();

                                // Crear el mapa de datos para el producto
                                Map<String, Object> producto = new HashMap<>();
                                producto.put("caloria", calorias);
                                producto.put("categoria", imageUrl);
                                producto.put("disponibilidad", disponibilidad);
                                producto.put("estado", estado);
                                producto.put("ingredientes", ingredientes);
                                producto.put("nombre", nombreProducto);
                                producto.put("precio", precio);
                                producto.put("codigoQR", qrUrl);

                                // Llamar al presentador para agregar el producto
                                mPresentador.agregarProducto(
                                        estado,
                                        nombreProducto,
                                        calorias,
                                        precio,
                                        disponibilidad,
                                        categoria,
                                        ingredientes,
                                        imageUrl,
                                        qrUrl
                                );

                                // Mostrar mensaje de éxito
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Producto agregado", Toast.LENGTH_SHORT).show();

                                // Ir al fragmento de gestión de productos
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container, new GestionProductosFragment()).commit();
                            });
                        }).addOnFailureListener(e -> {
                            // Mostrar mensaje de error en caso de falla en la carga de la imagen del producto
                            Toast.makeText(getActivity(), "Error al subir la imagen del producto", Toast.LENGTH_SHORT).show();
                        });
                    });
                }).addOnFailureListener(e -> {
                    // Mostrar mensaje de error en caso de falla en la carga del código QR
                    Toast.makeText(getActivity(), "Error al subir el código QR", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.selectImageButton) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } else if (view.getId() == R.id.btnCancelar) {
           //Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new GestionProductosFragment()).commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                mSelectedImageView.setImageBitmap(originalBitmap);
                // Obtener el nombre de la imagen seleccionada
                String imageName = getFileName(imageUri);

                // Guardar la imagen con el nombre seleccionado
                saveImage(originalBitmap, imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void saveImage(Bitmap bitmap, String imageName) {
        ContextWrapper contextWrapper = new ContextWrapper(requireContext());
        File directory = contextWrapper.getDir("images", Context.MODE_PRIVATE);
        File imagePath = new File(directory, imageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            imageUri = Uri.fromFile(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap generateQRCode(String data) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                }
            }
            return qrBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
