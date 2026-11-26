package com.amari.mesalistapruebas.data.model

// Clase para enviar al servidor el alta de un usuario
data class RegisterRequest (
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)