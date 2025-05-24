package com.example.adivinarnumero_laboratorio3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // Declaración de vistas
    private lateinit var tvTitle: TextView
    private lateinit var etGuess: EditText
    private lateinit var btnGuess: Button
    private lateinit var tvAttempts: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var btnReset: Button

    // Variables para la lógica del juego
    private var targetNumber: Int = 0
    private var attemptCount: Int = 0
    private val maxAttempts = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de las vistas
        tvTitle = findViewById(R.id.tvTitle)
        etGuess = findViewById(R.id.etGuess)
        btnGuess = findViewById(R.id.btnGuess)
        tvAttempts = findViewById(R.id.tvAttempts)
        tvFeedback = findViewById(R.id.tvFeedback)
        btnReset = findViewById(R.id.btnReset)

        // Inicia una nueva partida
        startNewGame()

        btnGuess.setOnClickListener {
            val guessInput = etGuess.text.toString()
            if (guessInput.isEmpty()) {
                Toast.makeText(this, "Ingresa un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val guess = guessInput.toIntOrNull()
            if (guess == null) {
                Toast.makeText(this, "Por favor, ingresa un valor numérico válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            attemptCount++
            when {
                guess == targetNumber -> {
                    tvFeedback.text = "¡Correcto! Has adivinado el número."
                    endGame()
                }
                attemptCount >= maxAttempts -> {
                    tvFeedback.text = "Has perdido. El número era $targetNumber."
                    endGame()
                }
                guess < targetNumber -> {
                    tvFeedback.text = "El número es mayor."
                    tvAttempts.text = "Intentos: $attemptCount / $maxAttempts"
                }
                guess > targetNumber -> {
                    tvFeedback.text = "El número es menor."
                    tvAttempts.text = "Intentos: $attemptCount / $maxAttempts"
                }
            }
            etGuess.text.clear()
        }

        btnReset.setOnClickListener {
            startNewGame()
        }
    }

    // Reinicia el juego
    private fun startNewGame() {
        targetNumber = Random.nextInt(0, 101)
        attemptCount = 0
        tvAttempts.text = "Intentos: $attemptCount / $maxAttempts"
        tvFeedback.text = ""
        etGuess.text.clear()
        etGuess.isEnabled = true
        btnGuess.isEnabled = true
        btnReset.visibility = View.GONE
    }

    // Llama cuando el juego termina (se acierta o se agotan los intentos)
    private fun endGame() {
        etGuess.isEnabled = false
        btnGuess.isEnabled = false
        tvAttempts.text = "Intentos: $attemptCount / $maxAttempts"
        btnReset.visibility = View.VISIBLE
    }
}