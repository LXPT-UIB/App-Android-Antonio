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

data class ReservationClient(
    val id: Int,
    val status: String,
    val source: String?,
    val date: String,
    val time: String,
    val end_time: String?,
    val party: Int,
    val table: String?,
    val tables: List<String>?,
    val zone: String?,
    val menu_options: Any?,
    val note: String?,
    val deposit: Any?,
    val modifiable: Boolean?,
    val deadline: String?,
    val arrival_window: Any?,
    val qr: String?,
    val group: Any?,
    val payable: Any?
)

//Clase que se utiliza para ver la disponibilidad de horas
data class AvailabilityResponse(
    val times: List<String> = emptyList()
)

//Se utiliza para crear una reserva
data class CreateReservationRequest(
    val date: String,
    val time: String,
    val party: Int,
    val note: String? = null,
    @SerializedName("menu_options")
    val menuOptions: List<String>? = null,
    val consent: Boolean? = null
)

//Es una respuesta del servidor que confirma la reserva
data class CreateReservationResponse(
    val reservation: ReservationClient
)

//Sacar la configuración del restaurante
data class ConfigResponse(
    @SerializedName("menu_options")
    val menuOptions: List<String>? = emptyList(),

    // Otros parámetros habituales de /config (opcionales por si los necesitas más adelante)
    @SerializedName("max_party")
    val maxParty: Int? = null,

    @SerializedName("deposit_min_party")
    val depositMinParty: Int? = null,

    @SerializedName("deposit_amount")
    val depositAmount: Double? = null
)