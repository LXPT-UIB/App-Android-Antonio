package com.amari.mesalistapruebas.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
OkHttp3: se encarga de establecer y gestionar las comunicaciones HTTP entre la aplicación Android y el servidor.
Retrofit: facilita el envío de peticiones a la API y la recepción de sus respuestas.
Gson: convierte los datos JSON recibidos del servidor en objetos Kotlin y viceversa.
AuthInterceptor: intercepta las peticiones HTTP y añade automáticamente el token de autenticación para identificar al usuario.
 */

// Crea un objeto Retrofit
object RetrofitClient {
    // Constante que es la base de la URL (lo hacemos constante porque
    // esta parte siempre es fija)
    private const val BASE_URL =
        "https://mesalista.spaincentral.cloudapp.azure.com/api/v1/"

    // Contexto de la aplicación
    private lateinit var appContext: Context

    // Inicializar con el contexto de Android
    fun inicializar(context: Context) {
        appContext = context.applicationContext
    }

    // Cliente HTTP que añade el token automáticamente
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(appContext))
            .build()
    }
        // Creamos un valor Retrofit, que hará peticiones HTTP y
        // hará traducciones JSON-Kotlin
        // 'lazy' inicializa el objeto cuando se necesita por primera vez
        val retrofit: Retrofit by lazy {
            // Se construye el objeto por partes
            Retrofit.Builder()
                // La primera parte es la parte fija de la URL
                .baseUrl(BASE_URL)

                .client(client)
                // Se configura el conversor JSON a Kotlin
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                // Se construye Retrofit
                .build()
        }
        // Se crea una instancia ApiService que hemos creado en otro archivo (ApiServicio.kt),
        //
        val api: ApiService by lazy {
            // Creamos una implementación de esa interfaz
            retrofit.create(ApiService::class.java)
        }
    }

