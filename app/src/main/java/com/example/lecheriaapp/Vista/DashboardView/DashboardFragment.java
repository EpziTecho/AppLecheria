package com.example.lecheriaapp.Vista.DashboardView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lecheriaapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {

    BarChart barChart;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        barChart = view.findViewById(R.id.barchart);
        pieChart = view.findViewById(R.id.piechart);

        // Supongamos que reservations es un mapa de reservas obtenido de Firebase
        Map<String, Map<String, Object>> reservations = obtenerReservas();

        // Contador de reservas por mes
        Map<String, Integer> reservationsPorMes = new HashMap<>();

        // Formato de fecha para parsear las fechas de las reservas
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        for (Map.Entry<String, Map<String, Object>> entry : reservations.entrySet()) {
            String fechaString = (String) entry.getValue().get("fecha");

            try {
                Date fecha = sdf.parse(fechaString);
                String mes = new SimpleDateFormat("MMMM", Locale.getDefault()).format(fecha);

                // Incrementar el contador para el mes correspondiente
                int count = reservationsPorMes.getOrDefault(mes, 0);
                reservationsPorMes.put(mes, count + 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Crear las entradas para el gráfico de barras y de pie
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        int i = 0;
        for (Map.Entry<String, Integer> entry : reservationsPorMes.entrySet()) {
            float value = entry.getValue().floatValue();
            barEntries.add(new BarEntry(i, value));
            pieEntries.add(new PieEntry(value, entry.getKey()));
            i++;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Ventas y Reservas por mes");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(14f); // Tamaño del texto de los valores en las barras
        barDataSet.setValueFormatter(new CustomBarValueFormatter()); // Formateador para mostrar ambas etiquetas

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.animateY(5000);
        // barChart.getDescription().setText("Reservas por mes");
        //barChart.getDescription().setTextColor(getResources().getColor(R.color.black));

        // Configurar el gráfico de pastel
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(new PieData(pieDataSet));
        pieChart.animateXY(5000, 5000);
        pieChart.getDescription().setEnabled(false);

        return view;
    }

    private Map<String, Map<String, Object>> obtenerReservas() {
        DatabaseReference reservasRef = FirebaseDatabase.getInstance().getReference().child("reservas");
        Map<String, Map<String, Object>> reservations = new HashMap<>();

        reservasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                    // Obtener datos de reserva como un mapa
                    Map<String, Object> reservaData = (Map<String, Object>) reservaSnapshot.getValue();

                    // Verificar el estado de la reserva
                    String estadoReserva = (String) reservaData.get("estado");
                    if (estadoReserva != null && estadoReserva.equals("FINALIZADO")) {
                        // La reserva está FINALIZADA, agregarla al mapa principal
                        String reservaId = reservaSnapshot.getKey();
                        reservations.put(reservaId, reservaData);
                    }
                }

                // Después de obtener los datos, puedes actualizar tus gráficos aquí
                actualizarGraficos(reservations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores, si es necesario
            }
        });

        // Devolver un mapa vacío (se actualizará en onDataChange)
        return reservations;
    }
    private void actualizarGraficos(Map<String, Map<String, Object>> reservations) {
        // Actualizar el PieChart
        actualizarPieChart(reservations);

        // Actualizar el BarChart
        actualizarBarChart(reservations);
    }

    private void actualizarPieChart(Map<String, Map<String, Object>> reservations) {
        if (pieChart != null) {
            // Limpiar datos antiguos
            pieChart.clear();

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            Map<String, Integer> productosFrecuentes = new HashMap<>();

            // Iterar sobre las reservas y contar productos por nombre
            for (Map<String, Object> reservaData : reservations.values()) {
                // Obtener el nodo "productos" de la reserva
                Map<String, Map<String, Object>> productos = (Map<String, Map<String, Object>>) reservaData.get("productos");

                // Verificar si hay productos en la reserva
                if (productos != null) {
                    // Iterar sobre los productos y contar por nombre y cantidad
                    for (Map.Entry<String, Map<String, Object>> productoEntry : productos.entrySet()) {
                        String nombreProducto = (String) productoEntry.getValue().get("nombre");
                        int cantidad = Integer.parseInt(productoEntry.getValue().get("cantidad").toString());

                        // Incrementar el contador para el producto correspondiente
                        int count = productosFrecuentes.getOrDefault(nombreProducto, 0);
                        productosFrecuentes.put(nombreProducto, count + cantidad);
                    }
                }
            }

            // Encontrar el producto más frecuente
            String productoMasFrecuente = encontrarProductoMasFrecuente(productosFrecuentes);

            // Crear entradas para el PieChart
            for (Map.Entry<String, Integer> entry : productosFrecuentes.entrySet()) {
                String producto = entry.getKey();
                int frecuencia = entry.getValue();

                // Solo agregar la entrada si la frecuencia es mayor que cero
                if (frecuencia > 0) {
                    pieEntries.add(new PieEntry(frecuencia));  // Etiqueta vacía
                }
            }

            // Configurar el gráfico de pastel
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

            pieDataSet.setValueTextSize(12f); // Establecer el tamaño del texto dentro de las entradas
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            // Configurar el formateador para mostrar valores enteros
            pieDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value); // Convertir el valor a entero y devolver como cadena
                }
            });

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.animateXY(5000, 5000);
            pieChart.getDescription().setEnabled(false);

            // Configurar la leyenda
            Legend legend = pieChart.getLegend();
            legend.setTextSize(14f);
            legend.setFormSize(14f);
            legend.setWordWrapEnabled(true);
            legend.setForm(Legend.LegendForm.CIRCLE);

            // Configurar la leyenda encima del PieChart
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setXEntrySpace(7f);

            // Configurar la posición del PieChart en relación con la leyenda
            pieChart.setExtraOffsets(5f, 5f, 5f, 20f);

            // Configurar la leyenda personalizada
            ArrayList<LegendEntry> legendEntries = new ArrayList<>();
            for (String producto : productosFrecuentes.keySet()) {
                String label = producto;
                LegendEntry entry = new LegendEntry();
                entry.label = label;
                entry.formColor = ColorTemplate.COLORFUL_COLORS[legendEntries.size() % ColorTemplate.COLORFUL_COLORS.length];
                legendEntries.add(entry);
            }
            legend.setCustom(legendEntries);

            // Notificar al PieChart que los datos han cambiado
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();
        }
    }

    private String encontrarProductoMasFrecuente(Map<String, Integer> productosFrecuentes) {
        int maxFrecuencia = 0;
        String productoMasFrecuente = null;

        for (Map.Entry<String, Integer> entry : productosFrecuentes.entrySet()) {
            String producto = entry.getKey();
            int frecuencia = entry.getValue();

            if (frecuencia > maxFrecuencia) {
                maxFrecuencia = frecuencia;
                productoMasFrecuente = producto;
            }
        }

        return productoMasFrecuente;
    }
    private void actualizarBarChart(Map<String, Map<String, Object>> reservations) {
        if (barChart != null) {
            // Limpiar datos antiguos
            barChart.clear();

            ArrayList<BarEntry> barEntries = new ArrayList<>();
            Map<String, BarDataItem> dataPorMes = new HashMap<>();

            // Iterar sobre las reservas y calcular ventas y cantidad de reservas por mes
            for (Map<String, Object> reservaData : reservations.values()) {
                String fechaString = (String) reservaData.get("fecha");
                float totalReserva = Float.parseFloat(reservaData.get("total").toString());

                try {
                    Date fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).parse(fechaString);
                    String mes = new SimpleDateFormat("MMMM", Locale.getDefault()).format(fecha);

                    // Obtener o crear el objeto BarDataItem para el mes
                    BarDataItem barDataItem = dataPorMes.get(mes);
                    if (barDataItem == null) {
                        barDataItem = new BarDataItem();
                        dataPorMes.put(mes, barDataItem);
                    }

                    // Incrementar la cantidad de reservas y sumar el total de la reserva
                    barDataItem.cantidadReservas++;
                    barDataItem.totalVentas += totalReserva;

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Crear entradas para el BarChart
            int i = 0;
            for (Map.Entry<String, BarDataItem> entry : dataPorMes.entrySet()) {
                BarDataItem barDataItem = entry.getValue();
                barEntries.add(new BarEntry(i, barDataItem.totalVentas, barDataItem)); // Usamos el objeto como etiqueta
                i++;
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Ventas y Reservas por mes");
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            barDataSet.setValueTextSize(12f); // Tamaño del texto de los valores en las barras
            barDataSet.setValueFormatter(new CustomBarValueFormatter()); // Formateador para mostrar ambas etiquetas

            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.animateY(5000);
            //barChart.getDescription().setText("Ventas y Reservas por mes");
            //barChart.getDescription().setTextColor(getResources().getColor(R.color.black));

            //ocultar descripcion
            barChart.getDescription().setEnabled(false);
            // Configurar la leyenda
            Legend legend = barChart.getLegend();
            legend.setTextSize(14f); // Tamaño del texto de la leyenda
            legend.setFormSize(14f); // Tamaño del texto en la leyenda

            // Configurar el eje X (parte inferior del gráfico)
            XAxis xAxis = barChart.getXAxis();
            xAxis.setTextSize(14f); // Tamaño del texto en el eje X

            // Configurar el eje Y (izquierda del gráfico)
            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setTextSize(14f); // Tamaño del texto en el eje Y izquierdo

            // Configurar el eje Y (derecha del gráfico)
            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setTextSize(14f); // Tamaño del texto en el eje Y derecho

            // Notificar al BarChart que los datos han cambiado
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
    }

    // Clase auxiliar para almacenar datos de cada mes
    private static class BarDataItem {
        int cantidadReservas = 0;
        float totalVentas = 0;
    }

    // Formateador personalizado para mostrar ambas etiquetas en las barras
    private static class CustomBarValueFormatter extends ValueFormatter {
        @Override
        public String getBarLabel(BarEntry barEntry) {
            // Obtener el objeto BarDataItem almacenado como etiqueta en la barra
            BarDataItem barDataItem = (BarDataItem) barEntry.getData();

            // Formatear la etiqueta con la cantidad de reservas y el total de ventas
            return "Reservas: " + barDataItem.cantidadReservas + ", Ventas: $" + barDataItem.totalVentas;
        }
    }

}