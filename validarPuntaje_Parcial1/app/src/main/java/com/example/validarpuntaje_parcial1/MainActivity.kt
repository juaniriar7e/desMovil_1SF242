package com.example.validarpuntaje_parcial1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Actividad principal
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Se carga el layout definido en activity_main.xml
        setContentView(R.layout.activity_main)

        // Se obtienen las referencias a los elementos de la UI
        val editTextGrade = findViewById<EditText>(R.id.editTextGrade)
        val buttonValidate = findViewById<Button>(R.id.buttonValidate)
        val textFeedback = findViewById<TextView>(R.id.textFeedback)

        // Se define la acción al pulsar el botón
        buttonValidate.setOnClickListener {
            val gradeStr = editTextGrade.text.toString()
            val gradeValue = gradeStr.toDoubleOrNull()
            if (gradeValue == null || gradeValue < 0 || gradeValue > 100) {
                // Si la entrada no es válida, se muestra un mensaje de error y se limpia el feedback
                textFeedback.text = ""
                Toast.makeText(
                    this,
                    "Por favor, ingrese un número válido entre 0 y 100.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Se instancia la clase Grade y se obtiene el mensaje de feedback
                val grade = Grade(gradeValue)
                val feedback = grade.getFeedback()
                // Se actualiza el TextView y se muestra un Toast con la información
                textFeedback.text = "Resultado: $feedback"
                Toast.makeText(
                    this,
                    "Calificación: $gradeValue - $feedback",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

// Clase orientada a objetos que encapsula la calificación y su lógica de retroalimentación
class Grade(private val score: Double) {
    fun getFeedback(): String {
        return when {
            score >= 91 -> "Excelente"
            score >= 81 -> "Bueno"
            score >= 71 -> "Regular"
            score >= 61 -> "Más o menos regular"
            else -> "Reprobado"
        }
    }
}