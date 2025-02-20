package sae.iot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrentChart(
    title: String,
    measurement: String,
    listY: List<Double>,
    modifier: Modifier = Modifier
) {
    val last = listY.last()

    Box {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),

            modifier = modifier
                .size(200.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,

                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp, vertical = 15.dp)
            ) {
                Text(title + " (${measurement})")
                Text(
                    text = "$last",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }
}

@Preview
@Composable
fun CurrentChartPreview() {
    CurrentChart(
        title = "Température",
        measurement = "température",
        listY = listOf(12.0),
    )
}
