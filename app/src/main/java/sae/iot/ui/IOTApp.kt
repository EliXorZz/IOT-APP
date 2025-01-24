package sae.iot.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sae.iot.R
import sae.iot.ui.components.topbar.BottomBar
import sae.iot.ui.components.topbar.TopBar
import sae.iot.ui.screens.ActuatorScreen
import sae.iot.ui.screens.HomeScreen
import sae.iot.ui.screens.HomeViewModel

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
        NavigationItem(title = LocalContext.current.getString(R.string.home), icon = Icons.Outlined.Home, screen = IOTScreen.Home),
        NavigationItem(title = LocalContext.current.getString(R.string.actuator), icon = Icons.Outlined.PlayArrow, IOTScreen.Actions),
        NavigationItem(title = LocalContext.current.getString(R.string.settings), icon = Icons.Outlined.Settings, IOTScreen.Settings)
    )

    val iotViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    from = "IUT Annecy",
                    alert = LocalContext.current.getString(R.string.alertDefault),
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
                   HomeScreen(roomUiState = iotViewModel.roomUiState)
                }

                composable(route = IOTScreen.Actions.name) {
                    ActuatorScreen(roomUiState = iotViewModel.roomUiState)
                }

                composable(route = IOTScreen.Settings.name) {
                    Text("params")
                }
            }
        }
    }
}