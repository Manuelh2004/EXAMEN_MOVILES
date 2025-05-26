package com.example.evaluacion_t2.ui.Registro;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.evaluacion_t2.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RegistroFragment extends Fragment implements View.OnClickListener {

    private ListView lista;
    private Button btnRegistrar;

    final String servidor = "http://10.0.2.2/paciente/";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registro, container, false);
        lista = rootView.findViewById(R.id.LstPacientes);

        btnRegistrar = (Button) rootView.findViewById(R.id.btnCrear);
        btnRegistrar.setOnClickListener(this);

        MostrarDatos();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == btnRegistrar){
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_registro_to_nav_agregar_paciente);
        }
    }

    public class ContactAdapter extends BaseAdapter {
        private Context context;
        private List<Paciente> pacientetList;

        public ContactAdapter(Context context, List<Paciente> contactList) {
            this.context = context;
            this.pacientetList = contactList;
        }

        @Override
        public int getCount() {
            return pacientetList.size();
        }

        @Override
        public Object getItem(int position) {
            return pacientetList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.paciente_item, null);
            }

            TextView id = convertView.findViewById(R.id.tvId);
            TextView nombres = convertView.findViewById(R.id.tvNombres);
            TextView edad = convertView.findViewById(R.id.tvEdad);
            TextView imc = convertView.findViewById(R.id.tvIMC);
            TextView clasificacion = convertView.findViewById(R.id.tvClasificacion);

            Paciente paciente = pacientetList.get(position);

            float IMC = paciente.peso / (paciente.altura * paciente.altura);

            String clasificacionIMC;
            if (IMC < 18.5f) {
                clasificacionIMC = "Bajo peso";
            } else if (IMC < 25.0f) {
                clasificacionIMC = "Peso normal";
            } else if (IMC < 30.0f) {
                clasificacionIMC = "Sobrepeso";
            } else {
                clasificacionIMC = "Obesidad";
            }

            id.setText(paciente.id);
            nombres.setText("Nombres y Apellidos: " + paciente.nombre + " " + paciente.apaterno + " " + paciente.amaterno);
            edad.setText("Edad: " + paciente.edad);
            imc.setText("IMC: " + IMC);
            clasificacion.setText("Clasificación IMC: " + clasificacionIMC);

            return convertView;
        }
    }

    private void MostrarDatos() {
        String url = servidor + "mostrar_paciente.php";
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<Paciente> pacientes = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id_paciente = jsonObject.getString("id_paciente");
                        String nom_paciente = jsonObject.getString("nom_paciente");
                        String ap_paciente = jsonObject.getString("ap_paciente");
                        String am_paciente = jsonObject.getString("am_paciente");
                        String ed_paciente = jsonObject.getString("ed_paciente");
                        String sex_paciente = jsonObject.getString("sex_paciente");
                        Float alt_paciente = Float.parseFloat(jsonObject.getString("alt_paciente"));
                        Float pes_paciente = Float.parseFloat(jsonObject.getString("pes_paciente"));
                        String inf_paciente = jsonObject.getString("inf_paciente");

                        Paciente paciente = new Paciente(
                                id_paciente,
                                nom_paciente,
                                ap_paciente,
                                am_paciente,
                                ed_paciente,
                                sex_paciente,
                                pes_paciente,
                                alt_paciente,
                                inf_paciente
                        );

                        pacientes.add(paciente);
                    }

                    ContactAdapter adapter = new ContactAdapter(getActivity(), pacientes);
                    lista.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error al parsear el JSON", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String errorMessage = (responseBody != null) ? new String(responseBody) : error.getMessage();
                Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
