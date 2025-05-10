package com.example.myapplicationcompose

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplicationcompose.ui.theme.MyApplicationComposeTheme

class MainActivity2 : ComponentActivity() {

    var firstKeyName: String? = null
    var secondKeyName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Recibir los parámetros enviados desde el MainActivity (login)
            val myIntent = intent
            firstKeyName = myIntent.getStringExtra("firstKeyName")
            secondKeyName = myIntent.getStringExtra("secondKeyName")
            // Se concatena la información para mostrarla en interfaz
            val nameComplete: String = "$firstKeyName $secondKeyName"
            Toast.makeText(this, nameComplete, Toast.LENGTH_LONG).show()
            MyApplicationComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    CalculatorScreen(name = nameComplete, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(name: String, modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    // Variables de estado para los números de entrada y el resultado
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()

            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título y usuario recibido
        Text(text = "Calculadora", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Usuario: $name", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Fila con los dos campos de entrada (#1 y #2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = num1,
                onValueChange = { num1 = it },
                label = { Text("Insertar") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = num2,
                onValueChange = { num2 = it },
                label = { Text("Insertar") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Primera fila de botones: SUMAR y RESTAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    val n1 = num1.toFloatOrNull()
                    val n2 = num2.toFloatOrNull()
                    if (n1 != null && n2 != null) {
                        result = (n1 + n2).toString()
                    } else {
                        Toast.makeText(contexto, "Ingrese números válidos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("SUMAR")
            }
            Button(
                onClick = {
                    val n1 = num1.toFloatOrNull()
                    val n2 = num2.toFloatOrNull()
                    if (n1 != null && n2 != null) {
                        result = (n1 - n2).toString()
                    } else {
                        Toast.makeText(contexto, "Ingrese números válidos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("RESTAR")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Segunda fila de botones: MULTIPLICAR y DIVIDIR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    val n1 = num1.toFloatOrNull()
                    val n2 = num2.toFloatOrNull()
                    if (n1 != null && n2 != null) {
                        result = (n1 * n2).toString()
                    } else {
                        Toast.makeText(contexto, "Ingrese números válidos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("MULTIPLICAR")
            }
            Button(
                onClick = {
                    val n1 = num1.toDoubleOrNull()
                    val n2 = num2.toDoubleOrNull()
                    if (n1 != null && n2 != null) {
                        if (n2 == 0.0) {
                            Toast.makeText(contexto, "No se puede dividir entre cero", Toast.LENGTH_SHORT).show()
                        } else {
                            result = (n1 / n2).toString()
                        }
                    } else {
                        Toast.makeText(contexto, "Ingrese números válidos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("DIVIDIR")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para mostrar el resultado
        Text(text = "Resultado", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = result,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón para salir de la actividad
        Button(
            onClick = { activity?.finish() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salir")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    MyApplicationComposeTheme {
        CalculatorScreen(name = "mi_usuario")
    }
}