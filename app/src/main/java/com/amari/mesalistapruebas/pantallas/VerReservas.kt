package com.amari.mesalistapruebas.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amari.mesalistapruebas.R
import com.amari.mesalistapruebas.data.model.ReservationsResponse
import com.amari.mesalistapruebas.data.remote.RetrofitClient
import com.amari.mesalistapruebas.data.repository.Client
import com.amari.mesalistapruebas.data.repository.ReservationClient

private val Crema = Color(0xFFF5F0EA)
private val Cafe = Color(0xFF795548)
private val Verde = Color(0xFF4D8060)
private val Gris = Color(0xFF777777)

@Composable
fun VerReservasContenido(
    nombreUsuario: String,
    reservas: ReservationsResponse
) {
    // Estado que guarda la reserva pulsada para abrir su QR
    var reservaSeleccionada by remember { mutableStateOf<ReservationClient?>(null) }

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
                .padding(horizontal = 22.dp)
                .padding(top = 30.dp)
        ) {
            Text(
                text = "Mis reservas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hola, $nombreUsuario.\nPulsa sobre una reserva para ver su código QR.",
                fontSize = 15.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "${reservas.reservations.size} reservas",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (reservas.reservations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Todavía no tienes reservas.",
                        color = Gris,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reservas.reservations) { reserva ->
                        TarjetaReserva(
                            reserva = reserva,
                            onClick = { reservaSeleccionada = reserva }
                        )
                    }
                }
            }
        }
    }

    // Diálogo emergente con el QR de la reserva seleccionada (RF-18 / RP-07)
    reservaSeleccionada?.let { reserva ->
        DialogoQrReserva(
            reserva = reserva,
            onDismiss = { reservaSeleccionada = null }
        )
    }
}

@Composable
fun VerReservasNavegacion(usuario: Client) {

    var reservas by remember {
        mutableStateOf<ReservationsResponse?>(null)
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitClient.api.obtenerReservas()
            if (respuesta.isSuccessful) {
                reservas = respuesta.body()
                    ?: ReservationsResponse(emptyList())
            } else {
                error = "Error al obtener las reservas: ${respuesta.code()}"
            }
        } catch (e: Exception) {
            error = "Error de conexión: ${e.message}"
        } finally {
            cargando = false
        }
    }

    when {
        cargando -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error!!, color = Color.White)
            }
        }
        else -> {
            VerReservasContenido(
                nombreUsuario = usuario.name,
                reservas = reservas ?: ReservationsResponse(emptyList())
            )
        }
    }
}

@Composable
fun DialogoQrReserva(
    reserva: ReservationClient,
    onDismiss: () -> Unit
) {
    val qrBitmap = remember(reserva.qr) {
        reserva.qr?.takeIf { it.isNotBlank() }?.let { generarQrBitmap(it) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Pase de entrada",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Cafe,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Datos mínimos exigidos por RP-07 (fecha, hora y comensales)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Crema,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📅  ${reserva.date}   •   🕒  ${reserva.time}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Cafe
                        )
                        Text(
                            text = "👥  ${reserva.party} personas" +
                                    (reserva.table?.let { "   •   Mesa $it" } ?: ""),
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                // Imagen QR si la reserva está confirmada y tiene payload QR
                if (reserva.status == "confirmed" && qrBitmap != null) {
                    Image(
                        bitmap = qrBitmap,
                        contentDescription = "Código QR de la reserva",
                        modifier = Modifier
                            .size(220.dp)
                            .padding(8.dp)
                    )
                    Text(
                        text = "Código: ${reserva.qr}",
                        fontSize = 12.sp,
                        color = Gris,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Muestra este código al llegar al restaurante.",
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = when (reserva.status) {
                            "pending_payment" -> "El código QR se activará cuando se complete el pago de la señal."
                            "cancelled" -> "Esta reserva está cancelada; su código QR ya no es válido."
                            "completed", "seated" -> "Esta reserva ya ha sido validada en el restaurante."
                            else -> "Esta reserva no dispone de código QR activo."
                        },
                        fontSize = 14.sp,
                        color = Color(0xFFAD4545),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cerrar",
                    color = Cafe,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    )
}

@Composable
fun TarjetaReserva(
    reserva: ReservationClient,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Restaurante",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cafe,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = when (reserva.status) {
                        "confirmed" -> Color(0xFFE3F0E6)
                        "seated" -> Color(0xFFE3E8F5)
                        "completed" -> Color(0xFFE3F0E6)
                        "cancelled" -> Color(0xFFF5DADA)
                        "no_show" -> Color(0xFFF5DADA)
                        "pending_payment" -> Color(0xFFFFF0D9)
                        else -> Color.LightGray
                    }
                ) {
                    Text(
                        text = when (reserva.status) {
                            "confirmed" -> "Confirmada"
                            "seated" -> "Sentado"
                            "completed" -> "Completada"
                            "cancelled" -> "Cancelada"
                            "no_show" -> "No presentado"
                            "pending_payment" -> "Pago pendiente"
                            else -> reserva.status
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (reserva.status) {
                            "confirmed" -> Verde
                            "seated" -> Color(0xFF456A91)
                            "completed" -> Verde
                            "cancelled", "no_show" -> Color(0xFFAD4545)
                            "pending_payment" -> Color(0xFFAD782A)
                            else -> Color.DarkGray
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(text = "📅  ${reserva.date}", fontSize = 15.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "🕒  ${reserva.time}", fontSize = 15.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "👥  ${reserva.party} personas", fontSize = 15.sp, color = Color.DarkGray)

            if (reserva.status == "confirmed" && !reserva.qr.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Pulsa para mostrar QR ➔",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Cafe
                )
            }
        }
    }
}