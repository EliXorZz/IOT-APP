package sae.iot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sae.iot.model.Room
import sae.iot.ui.components.LineChart
import sae.iot.ui.components.RoomSelector

@Composable
fun HomeScreen(
    roomUiState: RoomUiState,
    modifier: Modifier = Modifier) {

    var rooms = listOf<Room>()
    when (roomUiState) {
        is RoomUiState.Loading -> {

        }
        is RoomUiState.Success -> {
            rooms = roomUiState.rooms
        }
        is RoomUiState.Error -> {

        }
    }



    Column {
        RoomSelector(
            rooms,
            selected = "LOL",
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 20.dp)
        ) {
            LineChart()
            LineChart()
            LineChart()
            LineChart()
        }
    }
}