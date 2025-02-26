package sae.iot.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import sae.iot.model.Discomfort
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LineChart(
    title: String,
    measurement: String,
    listX: List<Double>,
    listY: List<Double>,
    discomfort: Discomfort,
    color: Color = Color(0xffa485e0),
    scrollStart: Boolean = false,
    modifier: Modifier = Modifier
) {
    var alertOpen by remember { mutableStateOf(true) }
    val modelProducer = remember { CartesianChartModelProducer() }

    AnimatedVisibility(
        visible = discomfort.status && alertOpen,
        enter = expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        DiscomfortAlert(
            message = discomfort.causes ?: "",
            onDismiss = { alertOpen = false }
        )
    }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(listX) }
            lineSeries { series(listY) }
        }
    }

    Box {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),

            modifier = modifier
                .wrapContentHeight()
                .height(230.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),

                modifier = Modifier
                    .padding(15.dp)
            ) {
                Text(title + " (${measurement})")
                Chart(color, modelProducer, listX, scrollStart)
            }
        }
    }
}

@Composable
private fun Chart(
    color: Color,
    producer: CartesianChartModelProducer,
    listX: List<Double>,
    scrollStart: Boolean,
    modifier: Modifier = Modifier,
    ) {
    CartesianChartHost(
        scrollState =  rememberVicoScrollState(
            initialScroll = if (scrollStart) Scroll.Absolute.Start else Scroll.Absolute.End
        ),
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(fill(color)),
                        areaFill =
                        LineCartesianLayer.AreaFill.single(
                            fill(
                                ShaderProvider.verticalGradient(
                                    arrayOf(color.copy(alpha = 0.4f), Color.Transparent)
                                )
                            )
                        ),
                    )
                )
            ),
            startAxis = VerticalAxis
                .rememberStart(
                label = TextComponent(color = MaterialTheme.colorScheme.onSurface.toArgb())
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = TextComponent(color = MaterialTheme.colorScheme.onSurface.toArgb()),
                valueFormatter = { _, value, _ ->
                    convertTimestampToDate(listX[value.toInt()]) + "H"
                }
            ),
        ),

        modelProducer = producer,
        modifier = modifier,
    )
}

fun convertTimestampToDate(timestamp: Double): String {
    val date = Date(timestamp.toLong() * 1000)
    val format = SimpleDateFormat("dd-MM HH", Locale.getDefault())
    return format.format(date)
}

@Preview
@Composable
fun LineChartPreview() {
    LineChart(
        title = "Température",
        measurement = "température",
        listX = listOf(12.0),
        listY = listOf(12.0),
        discomfort = Discomfort("", false)
    )
}