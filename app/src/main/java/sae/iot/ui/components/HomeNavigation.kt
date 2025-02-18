package sae.iot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeNavigation(
    selectedIndex: Int,
    onSelectedIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),

                onClick = { onSelectedIndex(0) },
                selected = selectedIndex == 0,

                label = { Text("Salles") }
            )
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),

                onClick = { onSelectedIndex(1) },
                selected = selectedIndex == 1,

                label = { Text("Sensors") }
            )
        }
    }
}