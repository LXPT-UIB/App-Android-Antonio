
package com.amari.mesalistapruebas.data.remote



import com.amari.mesalistapruebas.data.model.LoginRequest
import com.amari.mesalistapruebas.data.model.RegisterRequest
import com.amari.mesalistapruebas.data.repository.AuthResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import okhttp3.ResponseBody

// Definimos las operaciones que podemos realizar contra el servidor

// @POST indica una petición HTTP del tipo POST
// 'suspend' permite ejecutar mediante corrutinas sin bloquear el hilo principal
// @BODY indica qué se enviará en el cuerpo de la petición (LoginRequest, RegisterRequest,...)
// ': Response<XXXXXX>' indica la respuesta del servidor y XXXXXX será el objeto que representa la respuesta del servidor (Clase Kotlin)

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

