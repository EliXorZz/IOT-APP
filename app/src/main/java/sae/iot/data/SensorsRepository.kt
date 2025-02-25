package sae.iot.data

import android.util.Log
import sae.iot.model.DataSensor
import sae.iot.model.Sensor
import sae.iot.network.SensorApiService


interface SensorsRepository {
    suspend fun getSensorsName(location: String): Map<String, Sensor>
    suspend fun getSensorsByRoom(room: String, location: String): List<String>
    suspend fun getDataSensorsByRoom(room: String, location: String): Map<String, DataSensor>
    suspend fun getDataSensor(sensorId: String, location: String): DataSensor
}

class NetworkSensorsRepository(
    private val SensorApiService: SensorApiService
) : SensorsRepository {
    override suspend fun getSensorsName(location: String): Map<String, Sensor> = SensorApiService.getSensors(location)
    override suspend fun getSensorsByRoom(room: String, location: String): List<String> = SensorApiService.getSensorsByRoom(room, location)
    override suspend fun getDataSensorsByRoom(room: String, location: String): Map<String, DataSensor> {
        val originalData = SensorApiService.getDataSensorsByRoom(room, location)

        return originalData
            .map { (key, sensor) ->
                val newKey = when (key) {
                    "air_temperature" -> "Température"
                    "binary" -> "Détecteur de mouvement"
                    "co2_level" -> "Niveau de CO2"
                    "dew_point" -> "Point de rosée"
                    "humidity" -> "Humidité"
                    "illuminance" -> "Luminosité"
                    "loudness" -> "Niveau sonore"
                    "smoke_density" -> "Densité de fumée"
                    "ultraviolet" -> "Niveau UV"
                    "volatile_organic_compound_level" -> "COV (Composés Organiques Volatils)"
                    else -> "Error"
                }
                newKey to sensor
            }.toMap()
    }
    override suspend fun getDataSensor(sensorId: String, location: String): DataSensor = SensorApiService.getData(sensorId, location)
}