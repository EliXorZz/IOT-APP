package sae.iot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Identifiable {
    val name: String
}

@Serializable
data class Room(
    @SerialName(value = "name")
    override val name: String,
) : Identifiable {
    init {
        require(name.isNotBlank()) { "Classroom name cannot be blank" }
    }
}

