
package com.amari.mesalistapruebas.data.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

// Clase que nos ayuda a recibir el token de la sesión, almacenarlo
// y utilizarlo en las peticiones
class AuthInterceptor(context: Context) : Interceptor {

    private val preferences = context.getSharedPreferences(
        "MesaListaSesion",
        Context.MODE_PRIVATE
    )

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = preferences.getString("token", null)

        val request = chain.request()
            .newBuilder()

        if (!token.isNullOrBlank()) {
            request.header(
                "Authorization",
                "Bearer $token"
            )
        }

        return chain.proceed(request.build())
    }
}
