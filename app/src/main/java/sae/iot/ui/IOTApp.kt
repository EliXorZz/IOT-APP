package sae.iot.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sae.iot.ui.components.LineChart
import sae.iot.ui.components.RoomSelector
import sae.iot.ui.components.topbar.BottomBar
import sae.iot.ui.components.topbar.TopBar

enum class IOTScreen {
    Home,
    Actions,
    Settings
}

data class NavigationItem(
    var title: String,
    var icon: ImageVector,

    var screen: IOTScreen
)

@Composable
fun IOTApp(
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(
        NavigationItem(title = "Home", icon = Icons.Outlined.Home, screen = IOTScreen.Home),
        NavigationItem(title = "Actionneurs", icon = Icons.Outlined.PlayArrow, IOTScreen.Actions),
        NavigationItem(title = "Paramètres", icon = Icons.Outlined.Settings, IOTScreen.Settings)
    )

    val iotViewModel: IotViewModel =
        viewModel(factory = IotViewModel.Factory)

    when (val sensorState = iotViewModel.sensorsUiState) {
        is SensorUiState.Loading -> {
            CircularProgressIndicator()
        }
        is SensorUiState.Success -> {
            Text(sensorState.sensor.measurment)
        }
        is SensorUiState.Error -> {
            Text("Une erreur est survenue")
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    from = "IUT Annecy",
                    alert = "Une alerte de fous !",
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            },

            bottomBar = { BottomBar(items, navController) },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = IOTScreen.Home.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(innerPadding)
                    .padding(top = 20.dp)
            ) {
                composable(route = IOTScreen.Home.name) {
                    val rooms = listOf("D360", "D360", "LOL", "D360", "DTEST", "D360", "D360")

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

                composable(route = IOTScreen.Actions.name) {
                    Text("Actions")
                }

                composable(route = IOTScreen.Settings.name) {
                    Text("Settings")
                }
            }
        }
    }
}