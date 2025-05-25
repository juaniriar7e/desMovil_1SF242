package com.example.whatsapp_lab4

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    private val viewModel: LocationViewModel by viewModels()
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms -> viewModel.handlePermissions(perms) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsAppLab4Theme {
                LocationTrackerUI(viewModel = viewModel) {
                    permissionLauncher.launch(viewModel.requiredPermissions)
                }
            }
        }
    }
}

@Composable
fun WhatsAppLab4Theme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationTrackerUI(
    viewModel: LocationViewModel,
    onRequestPermissions: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (!viewModel.hasPermissions(context)) {
            onRequestPermissions()
        }
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Botones de Control
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.startTracking(context) },
                enabled = !state.isTracking
            ) { Text("Iniciar") }

            Button(
                onClick = { viewModel.stopTracking() },
                colors = ButtonDefaults.buttonColors(Color.Red),
                enabled = state.isTracking
            ) { Text("Detener") }
        }

        // Ubicación Actual
        state.currentLocation?.let { location ->
            Card(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("📍 Ubicación:", style = MaterialTheme.typography.titleMedium)
                    Text("Lat: ${"%.6f".format(location.latitude)}")
                    Text("Lon: ${"%.6f".format(location.longitude)}")
                }
            }
        }

        // Formulario de Envío
        var phone by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }

        OutlinedTextField(
            value = phone,
            onValueChange = { newText ->
                if (newText.matches(Regex("^\\+?\\d*$"))) {
                    phone = newText
                }
            },
            label = { Text("Número de WhatsApp") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Button(
            onClick = { viewModel.sendToWhatsApp(context, message, phone) },
            enabled = state.currentLocation != null && phone.isNotBlank(),
            colors = ButtonDefaults.buttonColors(Color(0xFF25D366)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar por WhatsApp")
        }
    }
}

class LocationViewModel : ViewModel() {
    val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val _uiState = MutableStateFlow(LocationState())
    val uiState = _uiState.asStateFlow()

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    fun hasPermissions(context: Context): Boolean = requiredPermissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun handlePermissions(perms: Map<String, Boolean>) {
        if (perms.all { it.value }) {
            _uiState.value = _uiState.value.copy(isTracking = true)
        }
    }

    fun startTracking(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                _uiState.value = _uiState.value.copy(currentLocation = result.lastLocation)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient?.requestLocationUpdates(
                LocationRequest.Builder(10000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build(),
                locationCallback!!,
                null
            )
        }
    }

    fun stopTracking() {
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
        _uiState.value = LocationState()
    }

    fun sendToWhatsApp(context: Context, message: String, phone: String) {
        _uiState.value.currentLocation?.let { location ->
            val url = "https://wa.me/${phone.filter { it.isDigit() }}?text=${
                Uri.encode(
                    "$message\n" +
                            "📍 Lat: ${location.latitude}\n" +
                            "📍 Lon: ${location.longitude}\n" +
                            "https://maps.google.com/?q=${location.latitude},${location.longitude}"
                )
            }"
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}

data class LocationState(
    val isTracking: Boolean = false,
    val currentLocation: Location? = null
)