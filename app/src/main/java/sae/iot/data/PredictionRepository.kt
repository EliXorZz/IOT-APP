package sae.iot.data

import sae.iot.model.Prediction
import sae.iot.network.PredictionApiService

interface PredictionRepository {
    suspend fun getPredictionByRoom(room: String, location: String): Prediction
}

class NetworkPredictionRepository(
    private val PredictionApiService: PredictionApiService
) : PredictionRepository {
    override suspend fun getPredictionByRoom(room: String, location: String): Prediction = PredictionApiService.getPrediction(room = room, location = location)
}