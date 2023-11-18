package com.example.evaluacion1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.Toast;

public class SecondScreenActivity extends AppCompatActivity {
    TextView dateTextView, timeTextView;
    Button btn_Confirmar, btn_Salir;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean incidenciaEnviada = false; // Variable para rastrear si la incidencia se ha enviado
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);

        // Inicializar las vistas
        dateTextView = findViewById(R.id.date);
        timeTextView = findViewById(R.id.time);
        btn_Confirmar = findViewById(R.id.btnConfirmar);
        btn_Salir = findViewById(R.id.btnVolver);

        databaseHelper = new DatabaseHelper(this); // faltaba inicializar variable

        // Inicializar el sensor de acelerómetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btn_Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Obtiene los datos del intent
                String rut = getIntent().getStringExtra("RUT");
                String name = getIntent().getStringExtra("NAME");
                String incidence = getIntent().getStringExtra("INCIDENCE");
                String laboratory = getIntent().getStringExtra("LABORATORY");
                String fechaActual = obtenerFechaActual();
                String horaActual = obtenerHoraActual();

                // Agregar la incidencia a la base de datos
                databaseHelper.addIncidenceDetail(laboratory, rut, name, incidence, fechaActual, horaActual);
                mostrarDialogoConfirmacion();
            }
        });

        // Obtener la fecha y hora actual
        String fechaActual = obtenerFechaActual();
        String horaActual = obtenerHoraActual();

        // Configurar los valores en las vistas
        dateTextView.setText(fechaActual);
        timeTextView.setText(horaActual);


        // Botón para volver la aplicación
        btn_Salir.setOnClickListener(View -> finish());
    }

    // Función para obtener la fecha actual en el formato "dd/MM/yyyy"
    private String obtenerFechaActual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    // Función para obtener la hora actual en el formato "HH:mm:ss"
    private String obtenerHoraActual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    // Funciónes asociadas al acelerometro

    protected void onResume() {
        super.onResume();

        // Registrar el listener del sensor de acelerómetro
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();

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
                if (orientation >=85 && (orientation <= 95)&& incidenciaEnviada!=true){
                    // Obtiene los datos del intent
                    String rut = getIntent().getStringExtra("RUT");
                    String name = getIntent().getStringExtra("NAME");
                    String incidence = getIntent().getStringExtra("INCIDENCE");
                    String laboratory = getIntent().getStringExtra("LABORATORY");
                    String fechaActual = obtenerFechaActual();
                    String horaActual = obtenerHoraActual();

                    // Agregar la incidencia a la base de datos
                    databaseHelper.addIncidenceDetail(laboratory, rut, name, incidence, fechaActual, horaActual);
                    mostrarDialogoConfirmacion();
                    mostrarDialogoConfirmacion();
                    incidenciaEnviada = true; // Marcar la incidencia como enviada
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // No es necesario implementar esta parte en este ejemplo
        }
    };
    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Envío realizado con éxito");
        builder.setMessage("La incidencia ha sido enviada con éxito. El plazo de respuesta es de 3 días.");
        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(SecondScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
    }
}