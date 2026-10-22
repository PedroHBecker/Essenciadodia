package com.seugrupo.essenciadodia.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Modelos que representam o JSON devolvido pela API
data class ClimaAtual(
    @SerializedName("temperature_2m") val temperatura: Double
)

data class ClimaResposta(
    @SerializedName("current") val atual: ClimaAtual
)

// Interface do Retrofit: descreve a chamada HTTP
interface ClimaApi {
    @GET("v1/forecast")
    suspend fun temperaturaAtual(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") campos: String
    ): ClimaResposta
}

// Cria uma única instância do Retrofit para o app todo
object ClimaService {
    val api: ClimaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClimaApi::class.java)
    }
}