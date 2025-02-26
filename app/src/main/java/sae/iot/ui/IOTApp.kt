package sae.iot.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sae.iot.IotApplication
import sae.iot.R
import sae.iot.ui.components.topbar.BottomBar
import sae.iot.ui.components.topbar.TopBar
import sae.iot.ui.screens.ActuatorScreen
import sae.iot.ui.screens.MainScreen
import sae.iot.ui.screens.PredictionScreen
import sae.iot.ui.screens.RoomScreen
import sae.iot.ui.screens.SensorScreen
import sae.iot.ui.screens.SettingScreen
import sae.iot.ui.viewmodels.ActuatorViewModel
import sae.iot.ui.viewmodels.HomeViewModel
import sae.iot.ui.viewmodels.PredictionViewModel
import sae.iot.ui.viewmodels.SensorRoomViewModel
import sae.iot.ui.viewmodels.SensorsViewModel

enum class IOTScreen {
    Main,
    Actions,
    Settings,
    Room,
    Sensor,
    Prediction
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
        NavigationItem(
            title = LocalContext.current.getString(R.string.home),
            icon = Icons.Outlined.Home,
            screen = IOTScreen.Room
        ),
        NavigationItem(
            title = LocalContext.current.getString(R.string.prediction),
            icon = Icons.Outlined.AutoGraph,
            IOTScreen.Prediction
        ),
        NavigationItem(
            title = LocalContext.current.getString(R.string.actuator),
            icon = Icons.Outlined.PlayArrow,
            IOTScreen.Actions
        ),
        NavigationItem(
            title = LocalContext.current.getString(R.string.settings),
            icon = Icons.Outlined.Settings,
            IOTScreen.Settings
        )
    )

    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)

    val subMenuIndex by homeViewModel.selectedIndexUiState.collectAsStateWithLifecycle()
    val currentSite by homeViewModel.currentSiteUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current.applicationContext as IotApplication
    val roomsRepository = context.container.RoomsRepository
    val sensorsRepository = context.container.SensorsRepository
    val predictionRepository = context.container.PredictionRepository

    val sensorsViewModel = remember {
        SensorsViewModel(
            homeViewModel = homeViewModel,
            sensorRepository = sensorsRepository
        )
    }
    val predictionViewModel = remember {
        PredictionViewModel(
            homeViewModel = homeViewModel,
            roomsRepository = roomsRepository,
            predictionRepository = predictionRepository
        )
    }
    val sensorRoomViewModel = remember {
        SensorRoomViewModel(
            homeViewModel = homeViewModel,
            sensorRepository = sensorsRepository,
            roomsRepository = roomsRepository
        )
    }
    val actuatorViewModel = remember {
        ActuatorViewModel(
            homeViewModel = homeViewModel,
            roomsRepository = roomsRepository
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                if (currentSite != null) {
                    TopBar(
                        homeViewModel,
                        navController,
                        site = currentSite!!,
                        modifier = Modifier
                    )
                }
            },

            bottomBar = {
                if (currentSite != null) {
                    BottomBar(items, navController)
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = IOTScreen.Main.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(innerPadding)
                    .padding(top = 5.dp)
            ) {
                composable(route = IOTScreen.Main.name) {
                    MainScreen(homeViewModel, navController)
                }
                composable(route = IOTScreen.Prediction.name) {
                    PredictionScreen(homeViewModel, navController, predictionViewModel)
                }
                composable(route = IOTScreen.Actions.name) {
                    ActuatorScreen(actuatorViewModel)
                }
                composable(route = IOTScreen.Settings.name) {
                    SettingScreen()
                }
                composable(route = IOTScreen.Room.name) {
                    RoomScreen(homeViewModel, navController, sensorRoomViewModel)
                }
                composable(route = IOTScreen.Sensor.name) {
                    SensorScreen(homeViewModel, navController, sensorsViewModel)
                }
            }
        }
    }
}