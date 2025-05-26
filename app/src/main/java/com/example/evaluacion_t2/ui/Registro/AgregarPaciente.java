package com.example.evaluacion_t2.ui.Registro;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.evaluacion_t2.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AgregarPaciente extends Fragment implements View.OnClickListener{
    private Button btnRegistrar;
    private EditText etNombre, etApellidoPaterno, etApellidoMaterno, etEdad, etAltura, etPeso, etInformacion;
    private Spinner spSexo;
    final String servidor = "http://10.0.2.2/evaluacion_t2/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agregar_paciente, container, false);

        etNombre = (EditText) rootView.findViewById(R.id.etNombres);
        etApellidoPaterno = (EditText) rootView.findViewById(R.id.etApellidoPaterno);
        etApellidoMaterno = (EditText) rootView.findViewById(R.id.etApellidoMaterno);
        etEdad = (EditText) rootView.findViewById(R.id.etEdad);
        etAltura = (EditText) rootView.findViewById(R.id.etNombres);
        etPeso = (EditText) rootView.findViewById(R.id.etPeso);
        etInformacion = (EditText) rootView.findViewById(R.id.etInformacion);

        spSexo = (Spinner) rootView.findViewById(R.id.spSexo);
        String[] sexoItems = new String[] {"Masculino", "Femenino", "Otro"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, sexoItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSexo.setAdapter(adapter);

        btnRegistrar = (Button) rootView.findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this);

        return rootView;
    }

    private void LimpiarCampos()
    {
        etNombre.setText("");
        etApellidoMaterno.setText("");
        etApellidoPaterno.setText("");
        etAltura.setText("");
        etInformacion.setText("");
        etEdad.setText("");
        etPeso.setText("");
        spSexo.setSelection(0);
        etNombre.requestFocus();
    }
    private void RegistrarPaciente() {
        String url = servidor + "registrar_paciente.php";

        // Obtener valores de los campos
        String nombre = etNombre.getText().toString().trim();
        String apellidoPaterno = etApellidoPaterno.getText().toString().trim();
        String apellidoMaterno = etApellidoMaterno.getText().toString().trim();
        String edad = etEdad.getText().toString().trim();
        String alturaStr = etAltura.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();
        String informacion = etInformacion.getText().toString().trim();
        String sexo = spSexo.getSelectedItem().toString();

        // Validación básica de campos vacíos
        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() ||
                edad.isEmpty() || alturaStr.isEmpty() || pesoStr.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que altura y peso sean float válidos
        float altura, peso;
        try {
            altura = Float.parseFloat(alturaStr);
            peso = Float.parseFloat(pesoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Por favor, ingrese valores numéricos válidos para altura y peso", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear parámetros para enviar (se pasan como strings)
        RequestParams params = new RequestParams();
        params.put("nombre", nombre);
        params.put("apellido_paterno", apellidoPaterno);
        params.put("apellido_materno", apellidoMaterno);
        params.put("edad", edad);
        params.put("altura", String.valueOf(altura));
        params.put("peso", String.valueOf(peso));
        params.put("informacion", informacion);
        params.put("sexo", sexo);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                if (response.contains("exito")) {
                    Toast.makeText(getContext(), "Paciente registrado correctamente", Toast.LENGTH_SHORT).show();
                    LimpiarCampos();
                } else {
                    Toast.makeText(getContext(), "Error al registrar paciente: " + response, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error en la conexión al servidor", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegistrar) {
            RegistrarPaciente();
        }
    }
}