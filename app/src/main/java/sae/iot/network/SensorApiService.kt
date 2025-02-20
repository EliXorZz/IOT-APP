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
    suspend fun getSensorsByRoom(@Query("location") location: String = "iut", @Path("room") room: String): List<String>

    @GET("api/sensor/{sensorId}")
    suspend fun getData(@Query("location") location: String = "iut", @Path("sensorId") sensorId: String): DataSensor

    @GET("api/room/{room}/sensors")
    suspend fun getDataSensorsByRoom(@Query("location") location: String = "iut", @Path("room") room: String): Map<String, DataSensor>

}

val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}