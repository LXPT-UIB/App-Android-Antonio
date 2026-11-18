
package com.amari.mesalistapruebas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.amari.mesalistapruebas.ui.BottomNavigationBar
import com.amari.mesalistapruebas.ui.theme.PantallaInicioNavegacion

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // Averiguamos en qué pantalla estamos
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    // Pantallas en las que NO queremos mostrar la barra
    val rutasSinBarra = listOf(
        "bienvenida",
        "entrar",
        "registro"
    )


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Contenido de las pantallas
        NavHost(
            navController = navController,
            startDestination = "bienvenida",
            modifier = Modifier.fillMaxSize()
        ) {

            composable("bienvenida") {
                BienvenidaNavegacion(navController)
            }

            composable("entrar") {
                EntrarNavegacion(navController)
            }

            composable("registro") {
                RegistroNavegacion(navController)
            }

            composable("inicio") {
                PantallaInicioNavegacion(navController)
            }

            composable("reservar") {
                // ReservasNavegacion(navController)
            }

            composable("reservas") {
                // VerReservasNavegacion(navController)
            }

            composable("cuenta") {
                // CuentaNavegacion(navController)
            }

            composable("verReservas") {
                // VerReservasNavegacion(navController)
            }

            composable("vincular") {
                // VincularReservaNavegacion(navController)
            }
        }

        // Barra inferior flotante
        if (rutaActual !in rutasSinBarra) {

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
            ) {

                BottomNavigationBar(
                    selectedRoute = rutaActual ?: "inicio",
                    onItemSelected = { ruta ->

                        navController.navigate(ruta) {

                            popUpTo("inicio") {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

}
