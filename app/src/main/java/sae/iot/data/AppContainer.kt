package sae.iot.data

import sae.iot.network.RoomApiService
import sae.iot.network.SensorApiService
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val RoomsRepository: RoomsRepository
    val SensorsRepository : SensorsRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
        private val baseUrl = "http://10.7.183.202:5001/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val json = Json {
        ignoreUnknownKeys = true // Ignore les champs inconnus dans la réponse JSON
        isLenient = true         // Accepte les données non strictes
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: RoomApiService by lazy {
        retrofit.create(RoomApiService::class.java)
    }
    private val retrofitServiceMetric: SensorApiService by lazy {
        retrofit.create(SensorApiService::class.java)
    }

    /**
     * DI implementation for repository
     */
    override val RoomsRepository: RoomsRepository by lazy {
        NetworkRoomsRepository(retrofitService)
    }
    override val SensorsRepository: SensorsRepository by lazy {
        NetworkSensorsRepository(retrofitServiceMetric)
    }
}
