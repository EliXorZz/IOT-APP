package sae.iot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import sae.iot.ui.IOTScreen

@Composable
fun HomeNavigation(
    navController: NavHostController,
    index: Int
    ) {
    var selectedIndex = index
    Column {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                onClick = {
                    selectedIndex = 0
                    navController.navigate(IOTScreen.Room.name)
                },
                selected = selectedIndex == 0,
                label = { Text("Salles") }
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                onClick = {
                    selectedIndex = 1
                    navController.navigate(IOTScreen.Sensor.name)
                },
                selected = selectedIndex == 1,
                label = { Text("Sensors") }
            )
        }
    }
}