package com.amari.mesalistapruebas

import com.amari.mesalistapruebas.AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amari.mesalistapruebas.ui.theme.MesaListaPruebasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MesaListaPruebasTheme {
                AppNavigation()
            }
        }
    }
}


@Composable
fun BienvenidaContenido(
    onEntrarClick: () -> Unit,
    onCrearCuentaClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Imagen de fondo
        Image(
            painter = painterResource(R.drawable.mesa_inicio),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Texto de bienvenida
        Text(
            text = "¡Bienvenido/a!",
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Normal,
            color = Color.White,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 150.dp)
        )

        // Botones
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {

            FilledTonalButton(
                onClick = onEntrarClick,
                modifier = Modifier.size(150.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Entrar",
                    color = Color.Red
                )
            }

            FilledTonalButton(
                onClick = onCrearCuentaClick,
                modifier = Modifier.size(150.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Crear cuenta",
                    color = Color.Red
                )
            }
        }
    }
}


@Composable
fun BienvenidaNavegacion(navController: NavController) {
    BienvenidaContenido(
        //Si pulsa el botón de entrar
        onEntrarClick = {
            navController.navigate("entrar")
        },
        //Si pulsa el botón de Crear cuenta
        onCrearCuentaClick ={
            navController.navigate("registro")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BienvenidaPreview() {
    MesaListaPruebasTheme {
        BienvenidaContenido(onEntrarClick = {}, onCrearCuentaClick = {})
    }
}