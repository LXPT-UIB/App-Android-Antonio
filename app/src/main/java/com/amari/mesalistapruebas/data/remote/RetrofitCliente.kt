package com.amari.mesalistapruebas.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// Crea un objeto Retrofit con 3 parámetros
object RetrofitClient {
    // Constante que es la base de la URL (lo hacemos constante porque
    // esta parte siempre es fija)
    private const val BASE_URL =
        "https://mesalista.spaincentral.cloudapp.azure.com/api/v1/"


    // Creamos un valor Retrofit, que hará peticiones HTTP y
    // hará traducciones JSON-Kotlin
    // 'lazy' inicializa el objeto cuando se necesita por primera vez
    val retrofit: Retrofit by lazy {
        // Se construye el objeto por partes
        Retrofit.Builder()
            // La primera parte es la parte fija de la URL
            .baseUrl(BASE_URL)
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
