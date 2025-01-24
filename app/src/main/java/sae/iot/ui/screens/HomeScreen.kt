package sae.iot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import sae.iot.model.Room
import sae.iot.ui.components.LineChart
import sae.iot.ui.components.RoomSelector
import sae.iot.ui.viewmodels.HomeViewModel
import sae.iot.ui.viewmodels.RoomUiState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier) {

    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)

    val roomSelected by homeViewModel.roomSelectedUiState.collectAsStateWithLifecycle()
    val roomUiState = homeViewModel.roomUiState
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
            changeRoomState = { room ->
                homeViewModel.changeRoom(room)
            },
            roomSelected = roomSelected,
            rooms = rooms,
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