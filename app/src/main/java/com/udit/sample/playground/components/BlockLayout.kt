package com.udit.sample.playground.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BlockLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children

        Log.d("compose", "maxWidth : ${constraints.maxWidth}")
        Log.d("compose", "minWidth : ${constraints.minWidth}")
        Log.d("compose", "maxHeight : ${constraints.maxHeight}")
        Log.d("compose", "minHeight : ${constraints.minHeight}")
        Log.d("compose", "hasBoundedWidth : ${constraints.hasBoundedWidth}")
        Log.d("compose", "hasFixedWidth : ${constraints.hasFixedWidth}")


        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints).also {
                Log.d("compose", "child measuredWidth : ${it.measuredWidth}")
                Log.d("compose", "child width : ${it.width}")
            }
        }


        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

@Composable
private fun CompositeBlock(modifier: Modifier = Modifier) {
    StaggeredGrid(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Gray)
    ) {
        DataBlock2()
        DataBlock2()
    }
}

@Composable
fun DataBlock1() {
    val size = 90.dp
    BoxWithConstraints {
        when {
            this.maxWidth <= 410.dp -> {
                Column {
                    Box(
                        Modifier
                            .background(Color.Black)
                            .size(size)
                    )
                    Box(
                        Modifier
                            .background(Color.Red)
                            .size(size)
                    )
                }
            }
            else -> {
                Row() {
                    Box(
                        Modifier
                            .background(Color.Black)
                            .size(size)
                    )
                    Box(
                        Modifier
                            .background(Color.Red)
                            .size(size)
                    )
                }
            }
        }
    }
}

@Composable
fun DataBlock2() {
    Text(text = "Block number 2")
}

@Preview
@Composable
fun PreviewSampleBlock() {
    var width by remember {
        mutableStateOf(100.dp)
    }
    Column(Modifier.padding(20.dp)) {
        Spacer(modifier = Modifier.height(60.dp))
        Slider(
            value = width.value,
            onValueChange = {
                width = it.dp
            },
            valueRange = 100f..500f
        )
        Spacer(modifier = Modifier.height(60.dp))
        CompositeBlock()
    }
}