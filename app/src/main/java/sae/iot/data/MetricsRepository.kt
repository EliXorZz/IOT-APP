package sae.iot.data

import sae.iot.model.DataSensor
import sae.iot.model.Metric
import sae.iot.network.MetricApiService


interface MetricsRepository {
    suspend fun getMetricsNames(): Map<String, Metric>
    suspend fun getSensorsByRoom(room: String): List<String>
    suspend fun getDataSensor(sensorId: String): DataSensor
    suspend fun getOccupancy(room: String): Boolean
}

class NetworkMetricsRepository(
    private val MetricApiService: MetricApiService
) : MetricsRepository {
    override suspend fun getMetricsNames(): Map<String, Metric> = MetricApiService.getMetrics()
    override suspend fun getSensorsByRoom(room: String): List<String> = MetricApiService.getSensorsByRoom(room)
    override suspend fun getDataSensor(sensorId: String): DataSensor = MetricApiService.getData(sensorId)
    override suspend fun getOccupancy(room: String): Boolean = MetricApiService.getOccupancy(room)
}