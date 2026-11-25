
package com.amari.mesalistapruebas

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
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
import com.amari.mesalistapruebas.pantallas.PantallaInicioNavegacion
import com.amari.mesalistapruebas.pantallas.ReservarNavegacion
import com.amari.mesalistapruebas.pantallas.BienvenidaNavegacion
import com.amari.mesalistapruebas.pantallas.EntrarNavegacion
import com.amari.mesalistapruebas.pantallas.RegistroNavegacion
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.amari.mesalistapruebas.data.repository.Client
import com.amari.mesalistapruebas.pantallas.CuentaNavegacion
import com.amari.mesalistapruebas.pantallas.RecuperarContenido
import com.amari.mesalistapruebas.pantallas.RecuperarNavegacion
import com.amari.mesalistapruebas.pantallas.VerReservasNavegacion

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    // Usuario que ha iniciado sesión, tenemos los datos del usuario,
    // para poder usarlo en las distintas pantallas si es necesario
    var usuarioActual by remember {
        mutableStateOf<Client?>(null)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    val rutasSinBarra = listOf(
        "bienvenida",
        "entrar",
        "recuperar",
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
                EntrarNavegacion(navController = navController,
                    onLoginSuccess = { usuario ->
                        usuarioActual = usuario
                    })
            }

            composable("registro") {
                RegistroNavegacion(navController)
            }

            composable("recuperar") {
                RecuperarNavegacion(navController)
            }

            composable("inicio") {
                usuarioActual?.let { usuario ->
                    PantallaInicioNavegacion(
                        navController = navController,
                        usuario = usuario
                    )
                }
            }

            composable("reservar") {
                ReservarNavegacion(navController)
            }

            composable("VerReservas") {
                usuarioActual?.let { usuario ->
                    VerReservasNavegacion(
                        usuario = usuario
                    )
                }
            }

            composable("cuenta") {
                usuarioActual?.let { usuario ->
                    CuentaNavegacion(
                        navController = navController,
                        usuario = usuario,
                        onCerrarSesionClick = {
                            context.getSharedPreferences(
                                "MesaListaSesion",
                                Context.MODE_PRIVATE
                            ).edit()
                                .remove("token")
                                .apply()

                            usuarioActual = null

                            navController.navigate("bienvenida") {
                                popUpTo("bienvenida") {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
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

                        if (ruta == "inicio") {

                            // Volvemos a la pantalla de inicio que ya está
                            // en la pila de navegación
                            val haVuelto = navController.popBackStack(
                                "inicio",
                                inclusive = false
                            )

                            // Si no estaba en la pila, navegamos a inicio
                            if (!haVuelto) {
                                navController.navigate("inicio") {
                                    launchSingleTop = true
                                }
                            }

                        } else {

                            navController.navigate(ruta) {
                                popUpTo("inicio") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

