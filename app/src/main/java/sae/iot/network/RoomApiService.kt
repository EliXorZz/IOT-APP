package sae.iot.network

import kotlinx.serialization.json.Json
import sae.iot.model.Room
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RoomApiService {

    @GET("api/rooms")
    suspend fun getRooms(@Query("location") location: String = "iut"): List<Room>

    @GET("api/room/{room}/occupancy")
    suspend fun getOccupancy(@Path("room") room: String, @Query("location") location: String = "iut"): Boolean

}