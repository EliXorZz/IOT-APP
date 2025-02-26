package sae.iot.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sae.iot.model.Prediction

interface PredictionApiService {
    @GET("api/prediction/{room}")
    suspend fun getPrediction(@Path("room") room: String, @Query("location") location: String = "iut"): Prediction
}