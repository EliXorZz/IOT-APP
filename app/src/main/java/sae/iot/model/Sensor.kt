package sae.iot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sensor(
    @SerialName(value = "friendly_name_str")
    override val name: String,
    @SerialName(value = "measurement")
    val measurement: String,
    @SerialName(value = "room")
    val room: String,
    @SerialName(value = "domain")
    val domain: String
) : Identifiable {
    init {
        require(name.isNotBlank()) { "Metric name cannot be blank" }
        require(measurement.isNotBlank()) { "Metric measurement cannot be blank" }
        require(room.isNotBlank()) { "Room cannot be blank" }
        require(domain.isNotBlank()) { "Domain cannot be blank" }
    }
}
