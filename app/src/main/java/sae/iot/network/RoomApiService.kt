package sae.iot.network

import sae.iot.model.Classroom
import retrofit2.http.GET

/**
 * A public interface that exposes the [getClassrooms] method
 */
interface RoomApiService {
    /**
     * Returns a [List] of [Classroom] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "rooms" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("api/rooms")
    suspend fun getClassrooms(): List<Classroom>
}