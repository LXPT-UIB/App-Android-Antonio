package com.amari.mesalistapruebas.pantallas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amari.mesalistapruebas.R
import com.amari.mesalistapruebas.data.remote.RetrofitClient
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReservarScreen(
    onContinuarClick: (fechaIso: String, hora: String, personas: Int) -> Unit = { _, _, _ -> }
) {
    val cafe = Color(0xFF795548)
    val fondo = Color(0xFFF5F0EA)
    val blanco = Color.White

    var mostrarCalendario by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf<Long?>(null) }
    var personas by remember { mutableIntStateOf(2) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    // Estados de red para disponibilidad (RF-06)
    var horasDisponibles by remember { mutableStateOf<List<String>>(emptyList()) }
    var cargandoHoras by remember { mutableStateOf(false) }
    var errorHoras by remember { mutableStateOf<String?>(null) }

    val hoyUtcMillis = remember {
        java.time.LocalDate.now()
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }

    val calendario = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= hoyUtcMillis
            }
        }
    )

    // Fecha visual para el usuario (dd/MM/yyyy)
    val fechaTexto = fechaSeleccionada?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } ?: "Seleccionar día"

    // Fecha ISO para la API (YYYY-MM-DD)
    val fechaIso = fechaSeleccionada?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    // Consultar horas libres automáticamente al cambiar fecha o comensales
    LaunchedEffect(fechaIso, personas) {
        if (fechaIso != null) {
            cargandoHoras = true
            errorHoras = null
            horaSeleccionada = null
            try {
                val respuesta = RetrofitClient.api.consultarDisponibilidad(fechaIso, personas)
                if (respuesta.isSuccessful) {
                    horasDisponibles = respuesta.body()?.times ?: emptyList()
                } else {
                    horasDisponibles = emptyList()
                    errorHoras = "No se pudo consultar disponibilidad (${respuesta.code()})"
                }
            } catch (e: Exception) {
                horasDisponibles = emptyList()
                errorHoras = "Error de conexión"
            } finally {
                cargandoHoras = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.fondo_marron),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. SELECCIÓN DE FECHA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = blanco),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Qué día?",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = cafe
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    FilledTonalButton(
                        onClick = { mostrarCalendario = true },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = fondo,
                            contentColor = cafe
                        )
                    ) {
                        Text(text = fechaTexto, fontSize = 17.sp)
                    }
                }
            }

            // 2. SELECCIÓN DE PERSONAS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = blanco),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Cuántos sois?",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = cafe
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledTonalButton(
                            onClick = { if (personas > 1) personas-- },
                            enabled = personas > 1,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = fondo,
                                contentColor = cafe
                            )
                        ) {
                            Text(text = "−", fontSize = 25.sp)
                        }

                        Text(
                            text = personas.toString(),
                            modifier = Modifier.padding(horizontal = 28.dp),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = cafe
                        )

                        FilledTonalButton(
                            onClick = { personas++ },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = fondo,
                                contentColor = cafe
                            )
                        ) {
                            Text(text = "+", fontSize = 25.sp)
                        }
                    }
                }
            }

            // 3. SELECCIÓN DE HORAS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = blanco),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Horas libres",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = cafe
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            fechaIso == null -> {
                                Text("Selecciona primero una fecha", color = Color.Gray)
                            }
                            cargandoHoras -> {
                                CircularProgressIndicator(color = cafe)
                            }
                            errorHoras != null -> {
                                Text(errorHoras!!, color = Color(0xFFAD4545))
                            }
                            horasDisponibles.isEmpty() -> {
                                Text("No hay horas disponibles ese día", color = Color.Gray)
                            }
                            else -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        horasDisponibles.forEach { hora ->
                                            val seleccionada = horaSeleccionada == hora
                                            Surface(
                                                modifier = Modifier.clickable {
                                                    horaSeleccionada = hora
                                                },
                                                shape = RoundedCornerShape(50),
                                                color = if (seleccionada) cafe else fondo
                                            ) {
                                                Text(
                                                    text = hora,
                                                    modifier = Modifier.padding(
                                                        horizontal = 18.dp,
                                                        vertical = 11.dp
                                                    ),
                                                    color = if (seleccionada) blanco else cafe,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Botón para pasar a la segunda pantalla (Opciones de menú + Nota)
            FilledTonalButton(
                onClick = {
                    if (fechaIso != null && horaSeleccionada != null) {
                        onContinuarClick(fechaIso, horaSeleccionada!!, personas)
                    }
                },
                enabled = fechaIso != null && horaSeleccionada != null,
                modifier = Modifier.height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF5D4037)
                )
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        fechaSeleccionada = calendario.selectedDateMillis
                        mostrarCalendario = false
                    }
                ) {
                    Text("Aceptar", color = cafe)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) {
                    Text("Cancelar", color = cafe)
                }
            }
        ) {
            DatePicker(state = calendario)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservarNavegacion(navController: NavController) {
    ReservarScreen(
        onContinuarClick = { fechaIso, hora, personas ->
            navController.navigate("detalles_reserva/$fechaIso/$hora/$personas")
        }
    )
}