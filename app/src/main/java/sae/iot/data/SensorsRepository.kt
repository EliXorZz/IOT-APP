package sae.iot.data

import sae.iot.model.DataSensor
import sae.iot.model.Sensor
import sae.iot.network.SensorApiService


interface SensorsRepository {
    suspend fun getSensorsName(): Map<String, Sensor>
    suspend fun getSensorsByRoom(room: String): List<String>
    suspend fun getDataSensorsByRoom(room: String): Map<String, DataSensor>
    suspend fun getDataSensor(sensorId: String): DataSensor
    suspend fun getOccupancy(room: String): Boolean
}

class NetworkMetricsRepository(
    private val SensorApiService: SensorApiService
) : SensorsRepository {
    override suspend fun getSensorsName(): Map<String, Sensor> = SensorApiService.getSensors()
    override suspend fun getSensorsByRoom(room: String): List<String> = SensorApiService.getSensorsByRoom(room)
    override suspend fun getDataSensorsByRoom(room: String): Map<String, DataSensor> = SensorApiService.getDataSensorsByRoom(room)
    override suspend fun getDataSensor(sensorId: String): DataSensor = SensorApiService.getData(sensorId)
    override suspend fun getOccupancy(room: String): Boolean = SensorApiService.getOccupancy(room)
}