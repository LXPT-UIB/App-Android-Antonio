package com.amari.mesalistapruebas


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amari.mesalistapruebas.R
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReservarScreen() {

    // Colores
    val cafe = Color(0xFF795548)
    val fondo = Color(0xFFF5F0EA)
    val blanco = Color.White

    // Variables de selección
    var mostrarCalendario by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf<Long?>(null) }
    var personas by remember { mutableIntStateOf(2) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val calendario = rememberDatePickerState()

    val fechaTexto = fechaSeleccionada?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } ?: "Seleccionar día"

    val horas = listOf(
        "12:00", "12:30", "13:00", "13:30",
        "14:00", "14:30", "15:00", "15:30",
        "19:00", "19:30", "20:00", "20:30",
        "21:00", "21:30", "22:00", "22:30"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Imagen de fondo
        Image(
            painter = painterResource(R.drawable.fondo_marron),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 25.dp,
                    bottom = 90.dp
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. SELECCIÓN DE FECHA

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = blanco
                ),
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
                        onClick = {
                            mostrarCalendario = true
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = fondo,
                            contentColor = cafe
                        )
                    ) {
                        Text(
                            text = fechaTexto,
                            fontSize = 17.sp
                        )
                    }
                }
            }

            // 2. SELECCIÓN DE PERSONAS

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = blanco
                ),
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

                        // Botón menos

                        FilledTonalButton(
                            onClick = {
                                if (personas > 1) personas--
                            },
                            enabled = personas > 1,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = fondo,
                                contentColor = cafe
                            )
                        ) {
                            Text(
                                text = "−",
                                fontSize = 25.sp
                            )
                        }

                        // Número de personas

                        Text(
                            text = personas.toString(),
                            modifier = Modifier.padding(horizontal = 28.dp),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = cafe
                        )

                        // Botón más

                        FilledTonalButton(
                            onClick = {
                                personas++
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = fondo,
                                contentColor = cafe
                            )
                        ) {
                            Text(
                                text = "+",
                                fontSize = 25.sp
                            )
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
                colors = CardDefaults.cardColors(
                    containerColor = blanco
                ),
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

                    // Zona desplazable de horarios

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            horas.forEach { hora ->

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
            val onConfirmarReservaClick = null
            FilledTonalButton(
                onClick = { onConfirmarReservaClick },
                modifier = Modifier
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
            ) {
                Text(
                    text = "Confirmar Reserva",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037)
                )
            }

        }
    }
    // VENTANA DEL CALENDARIO

    if (mostrarCalendario) {

        DatePickerDialog(
            onDismissRequest = {
                mostrarCalendario = false
            },
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
                TextButton(
                    onClick = {
                        mostrarCalendario = false
                    }
                ) {
                    Text("Cancelar", color = cafe)
                }
            }
        ) {
            DatePicker(
                state = calendario
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservarNavegacion(navController: NavController) {
    ReservarScreen()
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ReservarScreenPreview() {
    _root_ide_package_.com.amari.mesalistapruebas.ui.theme.MesaListaPruebasTheme {
        ReservarScreen()
    }
}
