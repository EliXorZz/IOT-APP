package sae.iot.network

import sae.iot.model.DataSensor
import sae.iot.model.Sensor
import kotlinx.serialization.json.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SensorApiService {
    /**
     * Returns a [List] of [Sensor] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "sensors" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("api/sensors")
    suspend fun getSensors(@Query("location") location: String = "iut"): Map<String, Sensor>

    @GET("api/room/{room}/sensor-list")
    suspend fun getSensorsByRoom(@Path("room") room: String, @Query("location") location: String = "iut"): List<String>

    @GET("api/sensor/{sensorId}")
    suspend fun getData(@Path("sensorId") sensorId: String, @Query("location") location: String = "iut"): DataSensor

    @GET("api/room/{room}/sensors")
    suspend fun getDataSensorsByRoom(@Path("room") room: String, @Query("location") location: String = "iut"): Map<String, DataSensor>

}

val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}