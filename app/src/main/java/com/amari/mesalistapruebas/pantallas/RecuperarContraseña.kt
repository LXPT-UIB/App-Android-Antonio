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
import androidx.compose.ui.text.input.KeyboardType
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


//Pantalla para cambiar la contraseña

@Composable
fun RecuperarContenido(
    onEnviarEnlaceClick: (String) -> Unit,
    onIniciarSesionClick: () -> Unit,
    mensajeError: String,
    mensajeExito: String,
    cargando: Boolean
) {
    val focusManager = LocalFocusManager.current

    var correo by remember { mutableStateOf("") }
    var errorLocal by remember { mutableStateOf("") }

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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(30.dp)
        ) {

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    errorLocal = ""
                },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                label = {
                    Text(
                        text = "Correo electrónico",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            FilledTonalButton(
                onClick = {
                    focusManager.clearFocus()

                    // Comprobar que el correo tenga formato válido, como juanillo@example.com
                    if (!android.util.Patterns.EMAIL_ADDRESS
                            .matcher(correo.trim())
                            .matches()
                    ) {
                        errorLocal = "Introduce un correo electrónico válido"
                    } else {
                        errorLocal = ""
                        onEnviarEnlaceClick(correo.trim())
                    }
                },
                enabled = !cargando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = if (cargando) "Enviando..." else "Enviar enlace",
                    fontWeight = FontWeight.W800,
                    color = Color.Red
                )
            }

            // Mensaje de error local o del servidor
            val errorMostrar = errorLocal.ifEmpty { mensajeError }

            if (errorMostrar.isNotEmpty()) {
                Text(
                    text = errorMostrar,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Mensaje de éxito
            if (mensajeExito.isNotEmpty()) {
                Text(
                    text = mensajeExito,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier.offset(y = 200.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Iniciar Sesión",
                    textDecoration = TextDecoration.Underline,
                    color = Color.Green,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.clickable {
                        onIniciarSesionClick()
                    }
                )
            }
        }

        // Título
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Recuperar contraseña",
                fontSize = 28.sp,
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
fun RecuperarNavegacion(
    navController: NavController
) {
    val repository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()
    var mensajeError by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    RecuperarContenido(
        mensajeError = mensajeError,
        mensajeExito = mensajeExito,
        cargando = cargando,

        onEnviarEnlaceClick = { correo ->

            scope.launch {
                cargando = true
                mensajeError = ""
                mensajeExito = ""

                val resultado = repository.olvidoContrasena(correo)

                resultado.onSuccess {
                    mensajeExito =
                        "Si existe una cuenta asociada, recibirás un correo con las instrucciones para cambiar tu contraseña."
                }

                resultado.onFailure { error ->
                    mensajeError = error.message
                        ?: "No se pudo enviar la solicitud. Inténtalo de nuevo."
                }

                cargando = false
            }
        },

        onIniciarSesionClick = {
            navController.navigate("entrar") {
                popUpTo("recuperar") {
                    inclusive = true
                }
            }
        }
    )
}




@Preview(showBackground = true)
@Composable
fun RecuperarPreview() {
    MesaListaPruebasTheme {
        RecuperarContenido(
            mensajeError = "",
            mensajeExito = "",
            onIniciarSesionClick = {},
            onEnviarEnlaceClick = {},
            cargando = false

        )
    }
}

