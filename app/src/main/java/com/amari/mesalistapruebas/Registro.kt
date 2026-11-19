package com.amari.mesalistapruebas


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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun RegistroContenido(
    mensajeError:String = "",
    onCrearCuentaClick: (String,String,String,String) -> Unit,
    onIniciarSesionClick: () -> Unit
){

    val focusManager = LocalFocusManager.current
    //Nombre usuario introducido por el usuario
    var textoUsuario by remember { mutableStateOf("") }
    //Nombre introducido por el usuario
    var textoCorreo by remember { mutableStateOf("") }
    //Contraseña introducida por el usuario
    var textoContrasena by remember { mutableStateOf("") }
    //Telefono introducido por el usuario
    var textoTelefono by remember { mutableStateOf("") }
    //Variable auxiliar para mostrar/oculatar la contraseña
    var mostrar by remember { mutableStateOf(false) }

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
                value = textoCorreo,
                textStyle = TextStyle(
                    color = Color.White
                ),
                onValueChange = { textoCorreo = it },
                singleLine = true,
                label = {
                    Text(
                        text = "Correo electrónico",
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
                value = textoUsuario,
                textStyle = TextStyle(
                    color = Color.White
                ),
                onValueChange = { textoUsuario = it },
                singleLine = true,
                label = {
                    Text(
                        text = "Nombre de usuario",
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
                value = textoTelefono,
                textStyle = TextStyle(
                    color = Color.White
                ),
                onValueChange = { textoTelefono = it },
                singleLine = true,
                label = {
                    Text(
                        text = "Número de teléfono",
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
                    onDone = { onCrearCuentaClick(textoCorreo,textoUsuario,textoTelefono,textoContrasena)

                    }
                ),
                //Podemos mostrar/ocualtar la contraseña
                visualTransformation = if (mostrar){
                    //No oculatamos la contraseña
                    VisualTransformation.None}
                else{
                    //Mostramos la contraseña
                    PasswordVisualTransformation()},
                //Ponemos un icono que se pueda pulsar y cambiar entre mostrar/ocultar
                trailingIcon = {
                    Text(
                        //Los emoticonnos pulsables los podemos obtener de emojipedia
                        text = if (mostrar) "\uD83D\uDD12" else "👁️",
                        modifier = Modifier.clickable { mostrar = !mostrar }
                    )
                }
            )

            FilledTonalButton(
                onClick = { onCrearCuentaClick(textoCorreo,textoUsuario,textoTelefono,textoContrasena) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ){Text(
                text = "Crear cuenta",
                fontWeight = FontWeight.W800,
                color = Color.Red
            )
                }



            if (mensajeError.isNotEmpty()) {
                Text(
                    text = mensajeError,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Row (
                modifier = Modifier.offset(0.dp,200.dp),
                horizontalArrangement = Arrangement.Center
                ){
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
    }

}


@Composable
fun RegistroNavegacion(navController: NavController) {

    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val repository = remember {
        com.amari.mesalistapruebas.data.repository.AuthRepository()
    }

    val scope = rememberCoroutineScope()

    RegistroContenido(
        mensajeError = mensajeError,

        onCrearCuentaClick = { correo, usuario, telefono, contrasena ->

            // Evitamos enviar otra petición mientras hay una en curso
            if (!cargando) {

                scope.launch {

                    mensajeError = ""

                    // Comprobaciones básicas
                    when {
                        correo.isBlank() ||
                                usuario.isBlank() ||
                                telefono.isBlank() ||
                                contrasena.isBlank() -> {

                            mensajeError = "Todos los campos son obligatorios"
                        }

                        usuario.length > 80 -> {

                            mensajeError = "El nombre no puede superar 80 caracteres"
                        }

                        contrasena.length < 10 -> {

                            mensajeError = "La contraseña debe tener al menos 10 caracteres"
                        }

                        else -> {

                            cargando = true

                            val resultado = repository.registrarUsuario(
                                name = usuario,
                                email = correo,
                                phone = telefono,
                                password = contrasena
                            )

                            resultado.onSuccess { respuesta ->

                                // Registro correcto
                                // El servidor ya nos ha devuelto el usuario y el token

                                navController.navigate("inicio") {
                                    popUpTo("registro") {
                                        inclusive = true
                                    }
                                }
                            }

                            resultado.onFailure { error ->

                                mensajeError = error.message
                                    ?: "No se pudo crear la cuenta"
                            }

                            cargando = false
                        }
                    }
                }
            }
        },

        onIniciarSesionClick = {
            navController.navigate("entrar")
        }
    )
}




@Preview(showBackground = true)
@Composable
fun RegistroPreview(){
    RegistroContenido(
        onCrearCuentaClick = {_,_,_,_-> }
    ) { }

}