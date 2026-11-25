package com.amari.mesalistapruebas.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
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
import com.amari.mesalistapruebas.data.repository.Client
import com.amari.mesalistapruebas.ui.theme.MesaListaPruebasTheme

// Contenido de la pantalla de cuenta
@Composable
fun CuentaContenido(
    usuario: Client,
    onCerrarSesionClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(R.drawable.fondo_marron),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Título
            Text(
                text = "Mi cuenta",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Inicial del usuario
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = usuario.name.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF5D4037)
                )
            }

            // Nombre del usuario
            Text(
                text = usuario.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Usuario de MesaLista",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            // Tarjeta con información personal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Información personal",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )

                    HorizontalDivider(
                        color = Color(0xFFE0E0E0)
                    )

                    // Correo electrónico
                    Text(
                        text = "Correo electrónico",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )

                    Text(
                        text = usuario.email,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    HorizontalDivider(
                        color = Color(0xFFE0E0E0)
                    )

                    // Teléfono
                    Text(
                        text = "Teléfono",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )

                    Text(
                        text = usuario.phone ?: "No indicado",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Botón cerrar sesión
            FilledTonalButton(
                onClick = onCerrarSesionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF5D4037)
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


// Gestiona la navegación y las acciones de la pantalla
@Composable
fun CuentaNavegacion(
    navController: NavController,
    usuario: Client,
    onCerrarSesionClick: () -> Unit
) {
    CuentaContenido(
        usuario = usuario,
        onCerrarSesionClick = onCerrarSesionClick
    )
}


// Preview de la pantalla
@Preview(showBackground = true)
@Composable
fun CuentaPreview() {
    MesaListaPruebasTheme {
        CuentaContenido(
            usuario = Client(
                "Antonio Marín",
                "antonio@example.com",
                "600123456"
            ),
            onCerrarSesionClick = {}
        )
    }
}