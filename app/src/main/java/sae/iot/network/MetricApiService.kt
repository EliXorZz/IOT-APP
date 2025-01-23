package sae.iot.network

import sae.iot.model.DataSensor
import sae.iot.model.Metric
import kotlinx.serialization.json.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

/**
 * A public interface that exposes the [getMetrics] method
 */
interface MetricApiService {
    /**
     * Returns a [List] of [Metric] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "sensors" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("api/sensors")
    suspend fun getMetrics(): Map<String, Metric>

    @GET("api/room/{room}/sensor-list")
    suspend fun getSensorsByRoom(@Path("room") room: String): List<String>

    @GET("api/sensor/{sensorId}")
    suspend fun getData(@Path("sensorId") sensorId: String): DataSensor

    @GET("api/room/{room}/sensors")
    suspend fun getMetricsByRoom(@Path("room") room: String): Map<String, DataSensor>

    @GET("api/room/{room}/occupancy")
    suspend fun getOccupancy(@Path("room") room: String): Boolean


}

val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}
