package sae.iot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val text = if (measurement != "binary_sensor") "$last $measurement" else "$last"

    Box {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),

            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,

                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp, vertical = 15.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp
                )
                Text(
                    text = text,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Right,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
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
