package com.amari.mesalistapruebas.data.repository


import com.amari.mesalistapruebas.data.model.LoginRequest
import com.amari.mesalistapruebas.data.model.RegisterRequest
import com.amari.mesalistapruebas.data.remote.RetrofitClient
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

// Lo que involucre autenticación estará en esta clase
class AuthRepository {

    private val api = RetrofitClient.api

    // Iniciar sesión
    suspend fun iniciarSesion(
        email: String,
        password: String
    ): Result<AuthResponse> {

        return try {
            // Obtenemos la respuesta del servidor a partir del objeto LoginRequest
            val respuesta = api.iniciarSesion(
                LoginRequest(email, password)
            )

            if (respuesta.isSuccessful) {
                //0btenemos la respuesta
                val datos = respuesta.body()

                if (datos != null) {
                    Result.success(datos)
                } else {
                    Result.failure(
                        Exception("El servidor no ha devuelto datos")
                    )
                }
            } else {
                Result.failure(
                    Exception(
                        obtenerMensajeError(
                            respuesta.code(),
                            respuesta.errorBody()?.string()
                        )
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo conectar con el servidor: ${e.message}")
            )
        }
    }

    // Registrar usuario
    suspend fun registrarUsuario(
        name: String,
        email: String,
        phone: String,
        password: String
    ): Result<AuthResponse> {

        return try {
            val respuesta = api.registrarUsuario(
                RegisterRequest(name, email, phone, password)
            )

            if (respuesta.isSuccessful) {
                val datos = respuesta.body()

                if (datos != null) {
                    Result.success(datos)
                } else {
                    Result.failure(
                        Exception("El servidor no ha devuelto datos")
                    )
                }
            } else {
                Result.failure(
                    Exception(
                        obtenerMensajeError(
                            respuesta.code(),
                            respuesta.errorBody()?.string()
                        )
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo conectar con el servidor: ${e.message}")
            )
        }
    }


    suspend fun olvidoContrasena(email: String): Result<Unit> {

        return try {

            val respuesta = api.olvidoContrasena(
                mapOf("email" to email)
            )

            if (respuesta.isSuccessful) {

                val datos = respuesta.body()

                if (datos != null && datos.ok) {
                    Result.success(Unit)
                } else {
                    Result.failure(
                        Exception("No se pudo procesar la solicitud")
                    )
                }

            } else {

                Result.failure(
                    Exception(
                        obtenerMensajeError(
                            respuesta.code(),
                            respuesta.errorBody()?.string()
                        )
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    "No se pudo conectar con el servidor: ${e.message}"
                )
            )
        }
    }




    // Traducir códigos HTTP a mensajes

    private fun obtenerMensajeError(
        codigo: Int,
        cuerpo: String?
    ): String {

        return try {

            val json = JSONObject(cuerpo ?: "")
            val error = json.getJSONObject("error")

            val mensaje = error.optString(
                "message",
                "Error desconocido"
            )

            val campo = error.optString("field", "")

            if (campo.isNotEmpty()) {
                "$mensaje (Campo: $campo)"
            } else {
                mensaje
            }

        } catch (e: Exception) {

            "Error del servidor (código $codigo)"
        }
    }



}

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