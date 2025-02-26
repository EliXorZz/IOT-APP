package sae.iot.network

import retrofit2.http.GET
import sae.iot.model.Prediction

interface PredictionApiService {
    @GET("api/prediction")
    suspend fun getPrediction(): Prediction
}