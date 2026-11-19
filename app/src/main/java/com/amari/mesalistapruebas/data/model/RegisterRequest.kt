package com.amari.mesalistapruebas.data.model

data class RegisterRequest (
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)