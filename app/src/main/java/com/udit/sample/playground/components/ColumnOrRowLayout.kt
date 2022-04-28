package com.udit.sample.playground.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.udit.sample.playground.ui.theme.Blue400
import com.udit.sample.playground.ui.theme.Orange400
import java.lang.Math.max

private const val TAG = "ColumnOrRow"

@Composable
fun ColumnOrRowExpand(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->

        var slotId = 0
        val maxWidth = constraints.maxWidth
        var measurables = subcompose(slotId++, content = content)
        val count = measurables.size
        var width = 0
        var height = 0
        var allBlocksContained = true

        Log.d(TAG, "Layout Constraints : minWidth = ${constraints.minWidth}")
        Log.d(TAG, "Layout Constraints : maxWidth = ${constraints.maxWidth}")
        Log.d(TAG, "Layout Constraints : childCount = $count")

        var placeables: List<Placeable> = measurables.mapIndexed { index, measurable ->
            measurable.measure(Constraints(if (count == 1) maxWidth else 0, maxWidth = maxWidth))
                .also {
                    width += it.width
                    height = max(height, it.height)
//                    Log.d(TAG, "Child Constraints $index: width = ${it.width}")
//                    Log.d(TAG, "Child Constraints $index: measuredWidth = ${it.measuredWidth}")
                }
        }

        Log.d(TAG, "Total child width : $width")
        allBlocksContained = width <= maxWidth

        if (placeables.isNotEmpty() && placeables.size > 1) {
            if (!allBlocksContained) {
                width = maxWidth
                height = 0
                measurables = subcompose(slotId, content = content)
                placeables = measurables.mapIndexed { index, measurable ->
                    measurable.measure(Constraints(maxWidth, maxWidth))
                        .also {
                            height += it.height
//                            Log.d(TAG, "Child Constraints $index: width = ${it.width}")
//                            Log.d(
//                                TAG,
//                                "Child Constraints $index: measuredWidth = ${it.measuredWidth}"
//                            )
                        }
                }
            } else {
                // fill remaining space
                val occupiedWidth = width
                width = maxWidth
                val remainingSpace = maxWidth - occupiedWidth
                Log.d(TAG, "Remaining space left : $remainingSpace")

                measurables = subcompose(slotId, content = content)
                val oldPlaceables = placeables
                placeables = measurables.mapIndexed { index, measurable ->
                    val oldWidth = oldPlaceables[index].width
                    val fraction = oldWidth / occupiedWidth.toFloat()
                    Log.d(TAG, "Child $index occupied $fraction of occupied width")
                    val newWidth =
                        (oldPlaceables[index].width) + (fraction * remainingSpace).toInt()
                    Log.d(TAG, "Child $index oldWidth is $oldWidth")
                    Log.d(TAG, "Child $index newWidth is $newWidth")
                    measurable.measure(Constraints(newWidth, newWidth))
                        .also {
                            height = max(height, it.height)
                        }
                }
            }
        }

        Log.d(TAG, "Layout Bounds : width = $width")
        Log.d(TAG, "Layout Bounds : height = $height")

        layout(width = width, height = height) {
            if (allBlocksContained) {
                var xPos = 0
                placeables.forEach { placeable: Placeable ->
                    Log.d(TAG, " x position is $xPos")
                    placeable.placeRelative(xPos, 0)
                    xPos += placeable.width
                }
            } else {
                var yPos = 0
                placeables.forEach {
                    it.placeRelative(x = 0, yPos)
                    yPos += it.height
                }
            }
        }
    }
}


@Composable
fun ColumnOrRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->

        var slotId = 0
        val maxWidth = constraints.maxWidth
        var measurables = subcompose(slotId++, content = content)
        val count = measurables.size
        val widthPerComponent = (maxWidth / count)
        var width = 0
        var height = 0
        var allBlocksContained = true

        Log.d(TAG, "Layout Constraints : minWidth = ${constraints.minWidth}")
        Log.d(TAG, "Layout Constraints : maxWidth = ${constraints.maxWidth}")
        Log.d(TAG, "Layout Constraints : childCount = $count")
        Log.d(TAG, "Layout Constraints : widthPerComponent = $widthPerComponent")


        var placeables: List<Placeable> = measurables.mapIndexed { index, measurable ->
            measurable.measure(Constraints(0, widthPerComponent))
                .also {
                    width += it.width
                    height = max(height, it.height)
                    Log.d(TAG, "Child Constraints $index: width = ${it.width}")
                    Log.d(TAG, "Child Constraints $index: measuredWidth = ${it.measuredWidth}")
                    if (it.width == widthPerComponent) {
                        allBlocksContained = false
                    }
                }
        }

        if (placeables.isNotEmpty() && placeables.size > 1 && !allBlocksContained) {
            width = maxWidth
            height = 0
            measurables = subcompose(slotId, content = content)
            placeables = measurables.mapIndexed { index, measurable ->
                measurable.measure(Constraints(0, maxWidth))
                    .also {
                        height += it.height
                        Log.d(TAG, "Child Constraints $index: width = ${it.width}")
                        Log.d(TAG, "Child Constraints $index: measuredWidth = ${it.measuredWidth}")
                    }
            }
        }

        Log.d(TAG, "Layout Bounds : width = $width")
        Log.d(TAG, "Layout Bounds : height = $height")

        layout(width = width, height = height) {
            if (allBlocksContained) {
                var xPos = 0
                placeables.forEach {
                    it.placeRelative(x = xPos, 0)
                    xPos += it.width
                }
            } else {
                var yPos = 0
                placeables.forEach {
                    it.placeRelative(x = 0, yPos)
                    yPos += it.height
                }
            }
        }
    }
}

@Composable
private fun CompositeBlock(modifier: Modifier = Modifier) {
    ColumnOrRowExpand(modifier.background(Color.LightGray)) {
        BlueBlock()
        OrangeBlock()
        BlueBlock()
    }
}

@Composable
fun BlueBlock() {
    Box(
        modifier = Modifier
            .height(100.dp)
//            .requiredWidth(162.dp)
            .widthIn(100.dp, Int.MAX_VALUE.dp)
            .background(Blue400)
    )
}

@Composable
fun OrangeBlock() {
    Box(
        modifier = Modifier
            .height(100.dp)
//            .requiredWidth(100.dp)
            .widthIn(200.dp, Int.MAX_VALUE.dp)
            .background(Orange400)
    )
}

@Preview
@Composable
fun PreviewCompositeBlock() {
    CompositeBlock()
//    var width by remember {
//        mutableStateOf(200.dp)
//    }
//    Column(Modifier.fillMaxSize()) {
//        Spacer(modifier = Modifier.height(20.dp))
//        CompositeBlock(Modifier.width(width = width))
//        Spacer(modifier = Modifier.height(20.dp))
//        Slider(
//            onValueChange = {
//                width = it.dp
//            },
//            valueRange = 100f..2000f,
//            value = width.value
//        )
//    }
}