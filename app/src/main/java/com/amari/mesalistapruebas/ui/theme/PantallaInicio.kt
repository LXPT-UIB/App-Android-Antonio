package com.amari.mesalistapruebas.ui.theme


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amari.mesalistapruebas.R

@Composable
fun PantallaInicioContenido(
    onReservarClick: () -> Unit,
    onVerReservasClick: () -> Unit,
    onAnadirReservaClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        // Imagen de fondo
        Image(
            painter = painterResource(R.drawable.mesa_inicio),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido de la pantalla
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp),
            modifier = Modifier.padding(30.dp)
        ) {

            // Título
            Text(
                text = "¡Bienvenido User!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Botón Reservar
            FilledTonalButton(
                onClick = onReservarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Reservar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }

            // Botón Ver reservas
            FilledTonalButton(
                onClick = onVerReservasClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Ver reservas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }

            // Botón Añadir reserva
            FilledTonalButton(
                onClick = onAnadirReservaClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Añadir reserva",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }
    }
}


@Composable
fun PantallaInicioNavegacion(navController: NavController) {

    PantallaInicioContenido(

        // Botón Reservar
        onReservarClick = {
            navController.navigate("reservas")
        },

        // Botón Ver reservas
        onVerReservasClick = {
            navController.navigate("verReservas")
        },

        // Botón Añadir reserva
        onAnadirReservaClick = {
            navController.navigate("vincular")
        }
    )
}



@Preview(showBackground = true)
@Composable
fun PantallaInicioPreview() {
    MesaListaPruebasTheme {
        PantallaInicioContenido(
            onReservarClick = {},
            onVerReservasClick = {},
            onAnadirReservaClick = {}
        )
    }
}


