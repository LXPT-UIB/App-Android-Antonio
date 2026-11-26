
package com.amari.mesalistapruebas.data.remote



import com.amari.mesalistapruebas.data.model.LoginRequest
import com.amari.mesalistapruebas.data.model.RegisterRequest
import com.amari.mesalistapruebas.data.model.ReservationsResponse
import com.amari.mesalistapruebas.data.model.RespuestaRecuperacion
import com.amari.mesalistapruebas.data.repository.AuthResponse
import com.amari.mesalistapruebas.data.repository.AvailabilityResponse
import com.amari.mesalistapruebas.data.repository.CreateReservationRequest
import com.amari.mesalistapruebas.data.repository.CreateReservationResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import okhttp3.ResponseBody
import retrofit2.http.Header
import retrofit2.http.Query

// Definimos las operaciones que podemos realizar contra el servidor

// @POST indica una petición HTTP del tipo POST
// 'suspend' permite ejecutar mediante corrutinas sin bloquear el hilo principal
// @BODY indica qué se enviará en el cuerpo de la petición (LoginRequest, RegisterRequest,...)
// ': Response<XXXXXX>' indica la respuesta del servidor y XXXXXX será el objeto que representa la respuesta del servidor (Clase Kotlin)

interface ApiService {

    // Inicio de sesión
    @POST("auth/login")
    suspend fun iniciarSesion(
        @Body datos: LoginRequest
    ): Response<AuthResponse>

    // Crear cuenta
    @POST("auth/register")
    suspend fun registrarUsuario(
        @Body datos: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/forgot")
    suspend fun olvidoContrasena(
        @Body body: Map<String, String>
    ): Response<RespuestaRecuperacion>

    @GET("reservations")
    suspend fun obtenerReservas(): Response<ReservationsResponse>

    //Consultar disponibilidad de las mesas
    @GET("availability")
    suspend fun consultarDisponibilidad(
        @Query("date") fecha: String,
        @Query("party") comensales: Int
    ): Response<AvailabilityResponse>

    //Crear reserva
    @POST("reservations")
    suspend fun crearReserva(
        @Body datos: CreateReservationRequest,
        @Header("Idempotency-Key") idempotencyKey: String? = null
    ): Response<CreateReservationResponse>//Esto se utiliza para que en caso de que se envíen peticiones
    // duplicadas al servidor, este actúe únicamente una vez.

}

