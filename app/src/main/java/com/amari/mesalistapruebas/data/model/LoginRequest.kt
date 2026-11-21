package com.amari.mesalistapruebas.data.model

// Clase para solicitar al servidor si estos datos de inicio de sesion son correctos
data class LoginRequest(
    val email: String,
    val password: String
)