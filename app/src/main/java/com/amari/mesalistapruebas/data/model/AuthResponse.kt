
package com.amari.mesalistapruebas.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val user: Client,
    val session: Session
)

data class Client(
    val name: String,
    val email: String,
    val phone: String?
)

data class Session(
    val token: String,
    val kind: String,

    @SerializedName("expires_at")
    val expiresAt: String
)
