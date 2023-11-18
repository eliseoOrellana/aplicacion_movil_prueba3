package com.example.evaluacion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateDeleteActivity extends AppCompatActivity {

    private IncidenceModel incidenceModel;
    Spinner laboratoriesList;
    private EditText etRut, etIncidencias, etNombre;
    private String date, time;
    private Button btnUpdate, btnDelete, btnBack;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        Intent intent = getIntent();
        incidenceModel = (IncidenceModel) intent.getSerializableExtra("incidence");

        databaseHelper = new DatabaseHelper(this);

        etRut = findViewById(R.id.etRut);
        laboratoriesList = findViewById(R.id.laboratory);
        etIncidencias = findViewById(R.id.etIncidencias);
        etNombre = findViewById(R.id.etNombre);
        btnDelete = findViewById(R.id.btndelete);
        btnUpdate = findViewById(R.id.btnupdate);
        btnBack = findViewById(R.id.btnback);

        etRut.setText(incidenceModel.getRut());
        etIncidencias.setText(incidenceModel.getIncidenceBody());
        etNombre.setText(incidenceModel.getName());

        String laboratoryFromDatabase = incidenceModel.getLaboratory();
        // Crea un ArrayAdapter para el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.laboratoriesEntries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Establece el adaptador en el Spinner
        laboratoriesList.setAdapter(adapter);
        // Busca la posición de la cadena en el Spinner
        int position = -1;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(laboratoryFromDatabase)) {
                position = i;
                break;
            }
        }
        laboratoriesList.setSelection(position);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validarCampos()) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    date = dateFormat.format(calendar.getTime());
                    time = timeFormat.format(calendar.getTime());
                    String updateLaboratory = laboratoriesList.getSelectedItem().toString();

                    databaseHelper.updateIncidence(incidenceModel.getId(), updateLaboratory, etRut.getText().toString(), etNombre.getText().toString(), etIncidencias.getText().toString(), date, time);
                    Toast.makeText(UpdateDeleteActivity.this, "Incidencia actualizada", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(UpdateDeleteActivity.this, GetAllIncidenceActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteIncidence(incidenceModel.getId());

                Toast.makeText(UpdateDeleteActivity.this, "Incidencia eliminada", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(UpdateDeleteActivity.this, GetAllIncidenceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(UpdateDeleteActivity.this, GetAllIncidenceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private boolean validarCampos() {
        String rutText = etRut.getText().toString().trim();
        String nameText = etNombre.getText().toString().trim();
        String incidenceText = etIncidencias.getText().toString().trim();
        String selectedLaboratory = laboratoriesList.getSelectedItem().toString();

        if (rutText.isEmpty()) {
            etRut.setError("Ingrese un RUT ");
            return false;
        }
        if (!validarRutChileno(rutText)) {
            etRut.setError("Ingrese un RUT válido");
            return false;
        }

        if (nameText.isEmpty()) {
            etNombre.setError("El campo de nombre está vacío");
            return false;
        }

        if (containsNumbers(nameText)) {
            etNombre.setError("El campo de nombre no debe contener números");
            return false;
        }

        if (selectedLaboratory.isEmpty()) {
            // Muestra un mensaje de error para seleccionar un laboratorio
            TextView textViewMensaje = findViewById(R.id.textViewMensaje);
            textViewMensaje.setText("Seleccione un laboratorio.");
            return false;
        }

        if (incidenceText.isEmpty()) {
            etIncidencias.setError("Ingrese los detalles del incidente");
            return false;
        }

        return true;
    }

    // Función para validar el Rut chileno
    private boolean validarRutChileno(String rut) {
        // Implementa tu lógica de validación del Rut aquí
        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

   // Función para verificar si una cadena contiene números
    private boolean containsNumbers(String s) {
        return s.matches(".*\\d.*");
    }
}