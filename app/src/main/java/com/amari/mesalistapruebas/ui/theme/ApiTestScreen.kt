package com.amari.mesalistapruebas.ui.theme


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amari.mesalistapruebas.data.remote.RetrofitClient
import com.google.gson.GsonBuilder

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

            resultado = GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(respuesta)

        } catch (e: Exception) {

            resultado = "ERROR DE CONEXIÓN:\n${e.message}"

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


@Preview(showBackground = true)
@Composable
fun RegistroPreview(){
    ApiTestScreen()

}