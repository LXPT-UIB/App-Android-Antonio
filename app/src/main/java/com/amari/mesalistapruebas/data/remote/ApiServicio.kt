
package com.amari.mesalistapruebas.data.remote


import com.amari.mesalistapruebas.data.model.AuthResponse
import com.amari.mesalistapruebas.data.model.LoginRequest
import com.amari.mesalistapruebas.data.model.RegisterRequest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import okhttp3.ResponseBody

interface ApiService {

    // Comprobar configuración del servidor
    @GET("config")
    suspend fun obtenerConfiguracion(): Response<ResponseBody>

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

}

