
package com.amari.mesalistapruebas

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.amari.mesalistapruebas.ReservarNavegacion
import com.amari.mesalistapruebas.ReservarScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    val rutasSinBarra = listOf(
        "bienvenida",
        "entrar",
        "registro"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // CONTENIDO DE LAS PANTALLAS
        NavHost(
            navController = navController,
            startDestination = "bienvenida",
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    bottom = if (rutaActual !in rutasSinBarra) 85.dp else 0.dp
                )
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
                ReservarNavegacion(navController)
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

        // BARRA DE NAVEGACIÓN PERSONALIZADA
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

