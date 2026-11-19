
package com.amari.mesalistapruebas.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amari.mesalistapruebas.data.remote.RetrofitClient

@Composable
fun ApiTestScreen() {

    var resultado by remember {
        mutableStateOf("Conectando con MesaLista...")
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {

        try {

            val respuesta =
                RetrofitClient.api.obtenerConfiguracion()

            resultado = if (respuesta.isSuccessful) {

                val cuerpo = respuesta.body()?.string()

                "CONEXIÓN CORRECTA\n\n" +
                        "Código HTTP: ${respuesta.code()}\n\n" +
                        "Respuesta del servidor:\n$cuerpo"

            } else {

                "ERROR HTTP\n\n" +
                        "Código: ${respuesta.code()}\n" +
                        "Mensaje: ${respuesta.message()}"
            }

        } catch (e: Exception) {

            resultado = "ERROR DE CONEXIÓN:\n${e.javaClass.simpleName}\n${e.message}"

        } finally {

            cargando = false

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Prueba de conexión MesaLista",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            CircularProgressIndicator()
        }

        Text(
            text = resultado,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        )
    }
}
