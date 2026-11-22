package com.amari.mesalistapruebas.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import androidx.compose.runtime.*
import com.amari.mesalistapruebas.R
import com.amari.mesalistapruebas.data.repository.Client
import kotlinx.coroutines.launch
import com.amari.mesalistapruebas.data.repository.AuthRepository


//Pantalla para iniciar sesión
@Composable
fun EntrarContenido(
        onEntrarClick: (String, String) -> Unit,
        onCrearCuentaClick: () -> Unit,
        onOlvideContrasenaClick: () -> Unit,
        mensajeError:String) {

    val focusManager = LocalFocusManager.current
    //Nombre introducido por el usuario
    var textoUsuario by remember { mutableStateOf("") }
    //Contraseña introducida por el usuario
    var textoContrasena by remember { mutableStateOf("") }



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

        // Contenido encima de la imagen centrado vertical y horizontalmente
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(30.dp)
        ) {
            OutlinedTextField(
                value = textoUsuario,
                textStyle = TextStyle(
                    color = Color.White
                ),
                onValueChange = { textoUsuario = it },
                singleLine = true,
                label = {
                    Text(
                        text = "Correo Electrónico",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            OutlinedTextField(
                value = textoContrasena,
                textStyle = TextStyle(
                    color = Color.White
                ),
                singleLine = true,
                onValueChange = { textoContrasena = it },
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    Text(
                        text = "Contraseña",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEntrarClick(textoUsuario, textoContrasena)
                    }
                )
            )

            FilledTonalButton(
                onClick = {
                    onEntrarClick(textoUsuario, textoContrasena)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Entrar",
                    fontWeight = FontWeight.W800,
                    color = Color.Red
                )
            }

// Mensaje de error FUERA del botón
            if (mensajeError.isNotEmpty()) {
                Text(
                    text = mensajeError,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

// Fila para crear cuenta FUERA del botón
            Row {
                Text(
                    fontSize = 15.sp,
                    text = "¿No tienes una cuenta? Puedes crearla ",
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "aquí",
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.clickable {
                        onCrearCuentaClick()
                    }
                )
            }

// Recuperar contraseña FUERA del botón
            Text(
                text = "¿Has olvidado la contraseña?",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    onOlvideContrasenaClick()
                }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                fontSize = 28.sp,
                text = "¡Bienvenido/a de nuevo!",
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Normal,
                color = Color.White,
                fontWeight = FontWeight.Black,
                modifier = Modifier.offset(x = 5.dp, y = 150.dp)
            )
        }
    }
}

    @Composable
    fun EntrarNavegacion(navController: NavController,
                         onLoginSuccess: (Client) -> Unit) {

        var mensajeError by remember { mutableStateOf("") }
        var cargando by remember { mutableStateOf(false) }

        val repository = remember { AuthRepository() }
        val scope = rememberCoroutineScope()

        EntrarContenido(
            mensajeError = mensajeError,

            onEntrarClick = { correo, contrasena ->

                scope.launch {

                    cargando = true
                    mensajeError = ""

                    val resultado = repository.iniciarSesion(
                        email = correo,
                        password = contrasena
                    )

                    resultado.onSuccess {
                        // Devuelve la respuesta, que es un AuthResponse,
                        // tal y como tenemos definido en AuthRepository
                        respuesta ->
                        onLoginSuccess(respuesta.user)
                        // Inicio de sesión correcto
                        // Aquí recibimos el token de sesión

                        navController.navigate("inicio") {
                            popUpTo("entrar") {
                                inclusive = true
                            }
                        }
                    }

                    resultado.onFailure { error ->
                        mensajeError = error.message
                            ?: "No se pudo iniciar sesión"
                    }

                    cargando = false
                }
            },

            onCrearCuentaClick = {
                navController.navigate("registro")
            },

            onOlvideContrasenaClick = {
                navController.navigate("recuperar")
            }
        )
    }




    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        MesaListaPruebasTheme {
            EntrarContenido(
                onEntrarClick = { _, _ -> },
                onCrearCuentaClick = {},
                onOlvideContrasenaClick = {},
                mensajeError = ""
            )
        }
    }

