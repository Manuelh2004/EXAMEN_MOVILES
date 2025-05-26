package com.example.evaluacion_t2.ui.Reporte;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.evaluacion_t2.R;
import com.example.evaluacion_t2.databinding.FragmentReporteBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class ReporteFragment extends Fragment {


    private String servidor = "http://10.0.2.2/paciente/";
    //private Double IMC, alt_paciente, pes_paciente;
    private BarChart grafic;
    private FragmentReporteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReporteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        grafic = (BarChart) root.findViewById(R.id.barChart);

        obtenerDatosIMC();

        return root;
    }

    private void obtenerDatosIMC() {
        String url = servidor + "mostrar_paciente.php";

        RequestParams params = new RequestParams();

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();
                Toast.makeText(getActivity(), "Puedes Realizar dezplazamiento a la izquierda" , Toast.LENGTH_LONG).show();

                try {
                    // Parsear el JSON recibido
                    Random random = new Random();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        double alt_paciente = jsonObject.getDouble("alt_paciente");
                        double pes_paciente = jsonObject.getDouble("pes_paciente");
                        String nombre = jsonObject.getString("nom_paciente");

                        double IMC = pes_paciente / (alt_paciente * alt_paciente);
                        entries.add(new BarEntry(i, (float) IMC));
                        labels.add(nombre);

                        int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                        colors.add(color);
                    }


                    BarDataSet dataSet = new BarDataSet(entries, "IMC por Paciente");
                    dataSet.setColors(colors);
                    dataSet.setValueTextSize(14f);
                    dataSet.setValueTextColor(Color.BLACK);

                    BarData data = new BarData(dataSet);
                    data.setBarWidth(0.9f);

                    grafic.setData(data);
                    grafic.setFitBars(true);
                    grafic.getDescription().setEnabled(false);
                    grafic.setVisibleXRangeMaximum(3); // Muestra solo 5 barras visibles a la vez
                    grafic.moveViewToX(0);

                    XAxis xAxis = grafic.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(14f); // Tamaño de texto para los labels del eje X

                    grafic.getAxisLeft().setAxisMinimum(0f);
                    grafic.getAxisLeft().setTextSize(14f); // Tamaño del eje Y (opcional)
                    grafic.getAxisRight().setEnabled(false);

                    Legend legend = grafic.getLegend();
                    grafic.setExtraBottomOffset(10f);
                    legend.setTextSize(16f); // Tamaño del texto de la leyenda

                    grafic.animateY(1000);
                    grafic.invalidate();


                    Log.d("JSON_RESPONSE", response);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error al parsear el JSON", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}