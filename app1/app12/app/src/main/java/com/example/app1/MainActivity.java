package com.example.app1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btnImprimir;
    EditText txtMensaje;
    CheckBox check;
    CalendarView miCalendario;
    String miFecha = ""; // se actualizará con la fecha seleccionada
    ImageButton imgButton;

    @SuppressLint({"MissingInflatedId", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Aplicar Edge-to-Edge a la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enlazar componentes de la interfaz
        btnImprimir = findViewById(R.id.btnHola);
        txtMensaje = findViewById(R.id.textView);
        check = findViewById(R.id.checkBox);
        imgButton = findViewById(R.id.imageButton);
        miCalendario = findViewById(R.id.calendarView);

        // Listener del CalendarView para capturar la fecha seleccionada
        miCalendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                miFecha = dayOfMonth + "/" + (month + 1) + "/" + year;
            }
        });

        // Al pulsar el botón "Imprimir" se obtiene el contenido del EditText,
        // el estado del CheckBox, la hora actual y la fecha seleccionada, para mostrarse en un Toast.
        btnImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Se recalcula la hora actual al hacer clic
                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
                String hora = formatoHora.format(new Date());

                String message = txtMensaje.getText().toString() +
                        ", " + (check.isChecked() ? "Marcado" : "No Marcado") +
                        ", " + hora +
                        ", " + miFecha;

                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                // Mensaje opcional mediante Snackbar
                Snackbar.make(v, "Hola Mundo, estoy en clase de Android", Snackbar.LENGTH_LONG).show();
            }
        });

        // Al pulsar el ImageButton se inicia el segundo Activity (main2)
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiamos getApplicationContext() por MainActivity.this
                Intent puente = new Intent(MainActivity.this, main2.class);
                startActivity(puente);
            }
        });
    }
}