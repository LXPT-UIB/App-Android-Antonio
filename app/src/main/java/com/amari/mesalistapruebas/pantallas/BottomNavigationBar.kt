
package com.amari.mesalistapruebas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amari.mesalistapruebas.R
import com.amari.mesalistapruebas.ui.theme.MesaListaPruebasTheme

// Barra de navegación para navegar entre pantallas una vez se ha iniciado sesión
data class NavigationItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomNavigationBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit
) {

    val items = listOf(
        NavigationItem("Inicio", "inicio", Icons.Filled.Home),
        NavigationItem("Reservar", "reservar", Icons.Filled.Add),
        NavigationItem("Reservas", "VerReservas", Icons.Filled.List),
        NavigationItem("Cuenta", "cuenta", Icons.Filled.Person)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {

        // IMAGEN DE FONDO
        Image(
            painter = painterResource(R.drawable.fondo_marron),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize(),
            contentScale = ContentScale.Crop
        )

        // BARRA PERSONALIZADA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFFFFE4C4))
                .padding(
                    vertical = 8.dp,
                    horizontal = 4.dp
                ),

            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { item ->

                val seleccionado = selectedRoute == item.route

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            if (seleccionado)
                                Color.DarkGray
                            else
                                Color.Black
                        )
                        .clickable {
                            onItemSelected(item.route)
                        }
                        .padding(vertical = 8.dp),

                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PantallaInicioPreview() {
    MesaListaPruebasTheme {
        BottomNavigationBar(
            "",
            onItemSelected = {}
        )
    }
}

