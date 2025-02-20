package sae.iot.data

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
    override suspend fun getDataSensorsByRoom(room: String, location: String): Map<String, DataSensor> = SensorApiService.getDataSensorsByRoom(room,location)
    override suspend fun getDataSensor(sensorId: String, location: String): DataSensor = SensorApiService.getData(sensorId, location)
}