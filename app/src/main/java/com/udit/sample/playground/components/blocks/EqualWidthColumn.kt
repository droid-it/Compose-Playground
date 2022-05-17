package com.udit.sample.playground.components.blocks

import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import java.lang.Integer.max
import java.lang.Math.min


@Composable
fun EqualWidthColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SubcomposeLayout(
        modifier = modifier,
    ) { constraints ->

        var slotIndex = 0
        var placeables = subcompose(slotIndex++, content = content).map {
            it.measure(constraints = constraints)
        }

        val columnSize: IntSize = placeables.fold(
            initial = IntSize.Zero,
            operation = { currentMax: IntSize, placeable: Placeable ->
                IntSize(
                    width = max(currentMax.width, placeable.width),
                    height = currentMax.height + placeable.height
                )
            }
        )

        if (!placeables.isEmpty() && placeables.size > 1) {
            val newConstraints = Constraints(
                minWidth = columnSize.width,
                maxWidth = min(columnSize.width, constraints.maxWidth),
            )
            placeables = subcompose(slotIndex, content = content).map {
                it.measure(constraints = newConstraints)
            }
        }

        layout(columnSize.width, columnSize.height) {
            var yPos = 0
            placeables.forEach {
                it.placeRelative(x = 0, y = yPos)
                yPos += it.height
            }
        }
    }
}


@Preview
@Composable
fun PreviewEqualWidthColumn() {
    EqualWidthColumn(Modifier.background(Color.LightGray)) {
        Text("I am a long text", Modifier.background(Color.Green))
        Text("Small", Modifier.background(Color.Yellow))
        Text("Medium 2", Modifier.background(Color.Blue))
    }
}