package com.amari.mesalistapruebas.pantallas

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amari.mesalistapruebas.R
import com.amari.mesalistapruebas.data.remote.RetrofitClient
import com.amari.mesalistapruebas.data.repository.CreateReservationRequest
import com.amari.mesalistapruebas.data.repository.MenuOption
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

//Clase para especificar más detalles de la reserva

@Composable
fun DetallesReservaScreen(
    fechaIso: String,
    hora: String,
    personas: Int,
    onReservaConfirmada: () -> Unit,
    onVolverClick: () -> Unit
) {
    val cafe = Color(0xFF795548)
    val fondo = Color(0xFFF5F0EA)
    val blanco = Color.White

    val scope = rememberCoroutineScope()

    // Opciones de menú cargadas desde GET /config (RF-13)
    var opcionesDisponibles by remember { mutableStateOf<List<MenuOption>>(emptyList()) }
    var cargandoConfig by remember { mutableStateOf(true) }

    var opcionesSeleccionadas by remember { mutableStateOf<List<MenuOption>>(emptyList()) }
    var nota by remember { mutableStateOf("") }
    var consentimiento by remember { mutableStateOf(false) }

    var enviando by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    val tieneExtras = nota.isNotBlank() || opcionesSeleccionadas.isNotEmpty()

    // Pedir la configuración al servidor al entrar en la pantalla
    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitClient.api.obtenerConfiguracion()
            if (respuesta.isSuccessful) {
                opcionesDisponibles = respuesta.body()?.menuOptions ?: emptyList()
            }
        } catch (_: Exception) {
            opcionesDisponibles = emptyList()
        } finally {
            cargandoConfig = false
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
                .padding(horizontal = 20.dp, vertical = 25.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. RESUMEN DE LA SELECCIÓN ANTERIOR
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = blanco),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Resumen de tu reserva",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = cafe
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "📅  $fechaIso   •   🕒  $hora   •   👥  $personas pers.",
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                    if (personas >= 10) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "ℹ️ Grupos de 10 o más personas requieren señal previa.",
                            fontSize = 13.sp,
                            color = Color(0xFFAD782A)
                        )
                    }
                }
            }

            // 2. OPCIONES DE MENÚ DEL SERVIDOR (RF-13)
            if (cargandoConfig || opcionesDisponibles.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = blanco),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Opciones de menú (opcional)",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = cafe
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (cargandoConfig) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = cafe
                            )
                        } else {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                opcionesDisponibles.forEach { opcion ->
                                    val marcada = opcionesSeleccionadas.contains(opcion)
                                    Surface(
                                        modifier = Modifier.clickable {
                                            opcionesSeleccionadas = if (marcada) {
                                                opcionesSeleccionadas - opcion
                                            } else {
                                                opcionesSeleccionadas + opcion
                                            }
                                        },
                                        shape = RoundedCornerShape(50),
                                        color = if (marcada) cafe else fondo
                                    ) {
                                        Text(
                                            text = opcion.name,
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                            color = if (marcada) blanco else cafe,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. NOTA PARA EL RESTAURANTE (RF-12, máx. 200 caracteres)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = blanco),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Nota o alergias (opcional)",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = cafe
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = nota,
                        onValueChange = { if (it.length <= 200) nota = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. Alergia a frutos secos, carrito de bebé...") },
                        minLines = 3,
                        maxLines = 4
                    )
                    Text(
                        text = "${nota.length}/200",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp)
                    )

                    // Consentimiento explícito (RP-04) si hay nota u opciones
                    if (tieneExtras) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { consentimiento = !consentimiento }
                        ) {
                            Checkbox(
                                checked = consentimiento,
                                onCheckedChange = { consentimiento = it },
                                colors = CheckboxDefaults.colors(checkedColor = cafe)
                            )
                            Text(
                                text = "Consiento guardar mis opciones de menú y notas para esta reserva.",
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }

            if (errorMensaje != null) {
                Text(
                    text = errorMensaje!!,
                    color = Color(0xFFFFCDD2),
                    fontWeight = FontWeight.SemiBold
                )
            }

            // BOTONES DE ACCIÓN
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 75.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onVolverClick,
                    enabled = !enviando,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = blanco)
                ) {
                    Text("Atrás", fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        if (tieneExtras && !consentimiento) {
                            errorMensaje = "Debes aceptar el consentimiento para guardar notas o menú."
                            return@Button
                        }
                        errorMensaje = null
                        enviando = true

                        scope.launch {
                            try {
                                val request = CreateReservationRequest(
                                    // Identificador único para evitar duplicados de reserva
                                    requestId = UUID.randomUUID().toString(),
                                    date = fechaIso,
                                    time = hora,
                                    party = personas,
                                    note = nota.trim().takeIf { it.isNotEmpty() },
                                    menuOptions = opcionesSeleccionadas.map { it.id }.takeIf { it.isNotEmpty() },
                                    consent = if (tieneExtras) consentimiento else null
                                )

                                val resp = RetrofitClient.api.crearReserva(datos = request)
                                if (resp.isSuccessful) {
                                    onReservaConfirmada()
                                } else {
                                    val errBody = resp.errorBody()?.string()
                                    errorMensaje = try {
                                        JSONObject(errBody ?: "")
                                            .getJSONObject("error")
                                            .optString("message", "Error ${resp.code()}")
                                    } catch (_: Exception) {
                                        "No se pudo completar la reserva (${resp.code()})"
                                    }
                                }
                            } catch (e: Exception) {
                                errorMensaje = "Error de conexión: ${e.message}"
                            } finally {
                                enviando = false
                            }
                        }
                    },
                    enabled = !enviando,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blanco,
                        contentColor = Color(0xFF5D4037)
                    )
                ) {
                    if (enviando) {
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), color = cafe)
                    } else {
                        Text("Confirmar Reserva", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DetallesReservaNavegacion(
    navController: NavController,
    fechaIso: String,
    hora: String,
    personas: Int
) {
    DetallesReservaScreen(
        fechaIso = fechaIso,
        hora = hora,
        personas = personas,
        onReservaConfirmada = {
            navController.navigate("VerReservas") {
                popUpTo("reservar") { inclusive = true }// Borra también la pantalla "reservar"
            }
        },
        onVolverClick = { navController.popBackStack() }
    )
}

@Preview
@Composable
fun DetallesReservaPreview(){
    DetallesReservaScreen(
        fechaIso="14/02/2027",
        hora="14:00",
        personas=2,
        onReservaConfirmada={},
        onVolverClick={}
    )
}