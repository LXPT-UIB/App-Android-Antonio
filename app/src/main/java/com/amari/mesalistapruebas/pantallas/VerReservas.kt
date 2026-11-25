
package com.amari.mesalistapruebas.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amari.mesalistapruebas.data.repository.Client
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.amari.mesalistapruebas.R
import com.amari.mesalistapruebas.data.model.ReservationsResponse
import com.amari.mesalistapruebas.data.remote.RetrofitClient
import androidx.compose.runtime.*
import com.amari.mesalistapruebas.data.repository.ReservationClient

// Colores de MesaLista
private val Crema = Color(0xFFF5F0EA)
private val Cafe = Color(0xFF795548)
private val Verde = Color(0xFF4D8060)
private val Gris = Color(0xFF777777)





@Composable
fun VerReservasContenido(
    nombreUsuario: String,
    reservas: ReservationsResponse
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Fondo común de las pantallas con sesión iniciada
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
        )  {

        // Encabezado
        Text(
            text = "Mis reservas",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Hola, $nombreUsuario. \nAquí tienes tus reservas.",
            fontSize = 15.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Contador de reservas
        Text(
            text = "${reservas.reservations.size} reservas",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Lista desplazable
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

                    TarjetaReserva(reserva = reserva)
                }

                }
            }
        }
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
            // Se hace la petición al servidor
            val respuesta = RetrofitClient.api.obtenerReservas()
            // Respuesta del servidor
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
        // Mientras llega la respuesta ponemos una pantalla de carga
        // Actúa como un 'if'
        cargando -> {
            CircularProgressIndicator()
        }
        // Actúa como un 'if', si error no es null, se imprime el error
        error != null -> {
            Text(text = error!!)
        }
        // Tenemos respuesta y sin error
        else -> {
            VerReservasContenido(
                nombreUsuario = usuario.name,
                reservas = reservas ?: ReservationsResponse(emptyList())
            )
        }
    }
}

@Composable
fun TarjetaReserva(reserva: ReservationClient) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            // Nombre del restaurante y estado
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
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                    )
                }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Fecha
            Text(
                text = "📅  ${reserva.date}",
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Hora
            Text(
                text = "🕒  ${reserva.time}",
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Número de personas
            Text(
                text = "👥  ${reserva.party} personas",
                fontSize = 15.sp,
                color = Color.DarkGray
            )
        }
    }

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerReservasPreview() {

    MesaListaPruebasTheme {
        VerReservasContenido(
            nombreUsuario = "Antonio",
            reservas = listOf(
                ReservaUi(
                    restaurante = "Restaurante La Terraza",
                    fecha = "25 noviembre 2026",
                    hora = "14:00",
                    personas = 2,
                    estado = "Confirmada"
                ),
                ReservaUi(
                    restaurante = "Mar y Tierra",
                    fecha = "28 noviembre 2026",
                    hora = "21:00",
                    personas = 4,
                    estado = "Pendiente"
                ),
                ReservaUi(
                    restaurante = "Can Costa",
                    fecha = "2 diciembre 2026",
                    hora = "13:30",
                    personas = 3,
                    estado = "Confirmada"
                )
            )
        )
    }
}

 */