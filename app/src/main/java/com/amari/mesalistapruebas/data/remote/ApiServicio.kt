
package com.amari.mesalistapruebas.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("config")
    suspend fun obtenerConfiguracion(): Response<ResponseBody>




}
