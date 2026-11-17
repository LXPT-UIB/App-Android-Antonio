package com.amari.mesalistapruebas

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amari.mesalistapruebas.MainActivity
import com.amari.mesalistapruebas.ui.theme.MesaListaPruebasTheme


// Navegación entre pantallas

@Composable
fun AppNavigation() {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "bienvenida"
        ) {
            //Pantalla de bienvenida
            composable("bienvenida") {
                BienvenidaNavegacion(navController)
            }
            //Pantalla de entrar
            composable("entrar"){
                    EntrarNavegacion(navController)
            }
            composable("inicio") {


            }
            //Pantalla para crear una cuenta
            composable("registro") {
                    RegistroNavegacion(navController)
            }

        }


}


