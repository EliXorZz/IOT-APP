package sae.iot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import sae.iot.model.Discomfort
import sae.iot.model.Prediction
import sae.iot.model.Room
import sae.iot.ui.components.HomeNavigation
import sae.iot.ui.components.LineChart
import sae.iot.ui.components.LoadingSpin
import sae.iot.ui.components.RoomSelector
import sae.iot.ui.viewmodels.HomeViewModel
import sae.iot.ui.viewmodels.PredictionUiState
import sae.iot.ui.viewmodels.PredictionViewModel
import sae.iot.ui.viewmodels.RoomUiState

@Composable
fun PredictionScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    predictionViewModel: PredictionViewModel,
    modifier: Modifier = Modifier
) {
    val roomSelected by predictionViewModel.roomSelectedUiState.collectAsStateWithLifecycle()

    val roomUiState = predictionViewModel.roomUiState
    val predictionUiState = predictionViewModel.predictionUiState

    var prediction = Prediction( List(12) { 1.0 }, List(12) { 1.0 })
    var loading = false

    var rooms = listOf<Room>()
    when (roomUiState) {
        is RoomUiState.Loading -> {}
        is RoomUiState.Success -> {
            rooms = roomUiState.rooms
        }

        is RoomUiState.Error -> {}
    }

    when (predictionUiState) {
        is PredictionUiState.Loading -> {
            loading = true
        }

        is PredictionUiState.Success -> {
            prediction = predictionUiState.prediction
            loading = false
        }

        is PredictionUiState.Error -> {
            loading = true
        }
    }

    LaunchedEffect(Unit) {
        predictionViewModel.fresh()
    }

    Column(modifier = modifier) {
        if (loading) LoadingSpin() else {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),

                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 20.dp)
            ) {
                Row {
                    RoomSelector(
                        changeRoomState = { room ->
                            predictionViewModel.changeRoom(room)
                            predictionViewModel.getPrediction()
                        },
                        roomSelected = roomSelected,
                        rooms = rooms,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row {
                    LineChart(
                        title = "Prediction",
                        measurement = "Température",
                        listY = prediction.y,
                        listX = prediction.x,
                        discomfort = Discomfort("",false, 0),
                        color = Color(0xFFFFA500),
                        scrollStart = true
                    )
                }
            }
        }
    }
}