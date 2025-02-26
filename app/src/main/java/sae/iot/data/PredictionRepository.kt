package sae.iot.data

import sae.iot.model.Prediction
import sae.iot.network.PredictionApiService

interface PredictionRepository {
    suspend fun getPrediction(): Prediction
}

class NetworkPredictionRepository(
    private val PredictionApiService: PredictionApiService
) : PredictionRepository {
    override suspend fun getPrediction(): Prediction = PredictionApiService.getPrediction()
}