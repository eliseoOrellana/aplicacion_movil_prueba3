package com.example.evaluacion1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    Spinner laboratoriesList;
    EditText rut, name, incidenceBody;
    Button btn_enviar, btn_salir,btnListarIncidencias;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean incidenciaEnviada = false; // Variable para rastrear si la incidencia se ha enviado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mostrarDialogoConfirmacion();

        laboratoriesList = findViewById(R.id.laboratory);
        rut = findViewById(R.id.rut);
        name = findViewById(R.id.name);
        incidenceBody = findViewById(R.id.incidenceBody);
        btn_enviar = findViewById(R.id.btn_enviar);
        btn_salir = findViewById(R.id.btnExit);
        btnListarIncidencias = findViewById(R.id.btnListarIncidencias);



        // Inicializar el sensor de acelerómetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()) {
                    enviarIncidencia();
                    finish();
                }
            }
        });
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        btnListarIncidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // Inicia la actividad para ver todas las incidencias
                Intent intent;
                intent = new Intent(MainActivity.this, GetAllIncidenceActivity.class);
                startActivity(intent);
            }
        });



    }

    private boolean validarCampos() {
        String rutText = rut.getText().toString().trim();
        String nameText = name.getText().toString().trim();
        String incidenceText = incidenceBody.getText().toString().trim();
        String selectedLaboratory = laboratoriesList.getSelectedItem().toString();

        if (rutText.isEmpty()) {
            rut.setError("Ingrese un RUT ");
            return false;
        }
        if (!validarRutChileno(rutText)){
            rut.setError("Ingrese un RUT válido");
            return false;
        }

        if (nameText.isEmpty()) {
            name.setError("El campo de nombre está vacío");
            return false;
        }

        if (containsNumbers(nameText)) {
            name.setError("El campo de nombre no debe contener números");
            return false;
        }

        if (selectedLaboratory.isEmpty()) {
            // Muestra un mensaje de error para seleccionar un laboratorio
            TextView textViewMensaje = findViewById(R.id.textViewMensaje);
            textViewMensaje.setText("Seleccione un laboratorio.");
            return false;
        }

        if (incidenceText.isEmpty()) {
            incidenceBody.setError("Ingrese los detalles del incidente");
            return false;
        }

        return true;
    }

    // Función para validar el Rut chileno
    private boolean validarRutChileno(String rut) {
        // Implementa tu lógica de validación del Rut aquí
        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
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

    // Funciónes asociadas al acelerometro

    protected void onResume() {
        super.onResume();

        // Registrar el listener del sensor de acelerómetro
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    protected void onStop() {
        super.onStop();

        // Detener la escucha del sensor cuando la aplicación está en pausa
        //sensorManager.unregisterListener(sensorEventListener);
    }

    // Listener del sensor de acelerómetro
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor == accelerometerSensor) {
                // Obtener los valores del acelerómetro
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // Calcular la orientación en base a los valores del acelerómetro
                double orientation = Math.toDegrees(Math.acos(z / Math.sqrt(x * x + y * y + z * z)));

                // Mostrar un mensaje cuando el dispositivo esté en posición vertical
                if (orientation >=80 && (orientation <= 95)&& incidenciaEnviada!=true ){
                if(validarCampos()){
                enviarIncidencia();
                incidenciaEnviada = true; // Marcar la incidencia como enviada
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // No es necesario implementar esta parte en este ejemplo
        }
    };

    // Método para mostrar el cuadro de diálogo de confirmación

    // Método para enviar la incidencia
    private void enviarIncidencia() {
        // Iniciar la actividad "second_screen"
      //  Intent intent = new Intent(MainActivity.this, SecondScreenActivity.class);
     //   startActivity(intent);

        // Obtén los datos de los campos
        String rutText = rut.getText().toString();
        String nameText = name.getText().toString();
        String incidenceText = incidenceBody.getText().toString();
        String selectedLaboratory = laboratoriesList.getSelectedItem().toString();

        // Crea un intent para pasar los datos a SecondScreenActivity
        Intent intent = new Intent(MainActivity.this, SecondScreenActivity.class);

        // Pasa los datos como extras en el intent
        intent.putExtra("RUT", rutText);
        intent.putExtra("NAME", nameText);
        intent.putExtra("INCIDENCE", incidenceText);
        intent.putExtra("LABORATORY", selectedLaboratory);

        // Inicia SecondScreenActivity
        startActivity(intent);


    }
    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bienvenido(a)");
        builder.setMessage("Para una mejor experiencia de uso, puedes confirmar el envío de tus respuestas colocando tu celular en posición vertical o presionando el botón de envío.");
        builder.setNegativeButton("Siguiente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }

        });
        builder.show();
    }
    }




