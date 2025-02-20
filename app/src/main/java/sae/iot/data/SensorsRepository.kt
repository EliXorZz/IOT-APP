package sae.iot.data

import sae.iot.model.DataSensor
import sae.iot.model.Sensor
import sae.iot.network.SensorApiService


interface SensorsRepository {
    suspend fun getSensorsName(location: String): Map<String, Sensor>
    suspend fun getSensorsByRoom(location: String, room: String): List<String>
    suspend fun getDataSensorsByRoom(location: String, room: String): Map<String, DataSensor>
    suspend fun getDataSensor(location: String, sensorId: String): DataSensor
}

class NetworkSensorsRepository(
    private val SensorApiService: SensorApiService
) : SensorsRepository {
    override suspend fun getSensorsName(location: String): Map<String, Sensor> = SensorApiService.getSensors(location)
    override suspend fun getSensorsByRoom(location: String, room: String): List<String> = SensorApiService.getSensorsByRoom(location, room)
    override suspend fun getDataSensorsByRoom(location: String, room: String): Map<String, DataSensor> = SensorApiService.getDataSensorsByRoom(location,room)
    override suspend fun getDataSensor(location: String, sensorId: String): DataSensor = SensorApiService.getData(location, sensorId)
}