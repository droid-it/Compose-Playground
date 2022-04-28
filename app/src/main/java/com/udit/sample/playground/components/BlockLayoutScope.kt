package com.udit.sample.playground.components

import android.util.Log
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Composable
fun BlockLayout(
    modifier: Modifier = Modifier,
    content: @Composable BlockLayoutScope.() -> Unit
) {
    Layout(
        modifier = modifier,
        content = { BlockLayoutScopeInstance.content() }
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
//        val constraints1 = BlockLayoutConstraints(constraints)
        val layoutSizeParentData = Array(measurables.size) { measurables[it].data }

        Log.d("compose", "maxWidth : ${constraints.maxWidth}")
        Log.d("compose", "minWidth : ${constraints.minWidth}")
        Log.d("compose", "maxHeight : ${constraints.maxHeight}")
        Log.d("compose", "minHeight : ${constraints.minHeight}")
        Log.d("compose", "hasBoundedWidth : ${constraints.hasBoundedWidth}")
        Log.d("compose", "hasFixedWidth : ${constraints.hasFixedWidth}")


        val placeables = measurables.mapIndexed { index, measurable ->
            // Measure each children
            measurable.measure(constraints).also {
                Log.d("compose", "child measuredWidth : ${it.measuredWidth}")
                Log.d("compose", "child width : ${it.width}")
                Log.d("compose", "child constraints : ${layoutSizeParentData[index]}")
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

@LayoutScopeMarker
@Immutable
interface BlockLayoutScope {

    @Stable
    fun Modifier.minMaxWidth(minWidth: Dp, maxWidth: Dp): Modifier

}

internal object BlockLayoutScopeInstance : BlockLayoutScope {

    @Stable
    override fun Modifier.minMaxWidth(minWidth: Dp, maxWidth: Dp): Modifier {
        return if (minWidth != Dp.Unspecified && maxWidth != Dp.Unspecified) {
            this.then(
                LayoutSizeImpl(
                    minWidth = minWidth,
                    maxWidth = maxWidth
                )
            )
        } else {
            this
        }
    }
}

//internal class BlockLayoutConstraints(
//    val minWidth: Int,
//    val maxWidth: Int,
//    val minHeight: Int,
//    val maxHeight: Int,
//) {
//    constructor(c: Constraints) : this(
//        c.minWidth, c.maxWidth, c.minHeight, c.maxHeight
//    )
//}

private val IntrinsicMeasurable.data: LayoutSizeImpl?
    get() = parentData as? LayoutSizeImpl

internal class LayoutSizeImpl(
    val minWidth: Dp,
    val maxWidth: Dp
): ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?): Any? {
        return ((parentData as? BlockLayoutParentData) ?: BlockLayoutParentData()).also {
            it.minWidth = minWidth
            it.maxWidth = maxWidth
        }
    }

    override fun hashCode(): Int {
        return ((minWidth * 7) + (maxWidth * 3)).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherModifier = other as? LayoutSizeImpl ?: return false
        return minWidth != otherModifier.minWidth &&
                maxWidth != otherModifier.maxWidth
    }

    override fun toString(): String {
        return "layout bounds : min = $minWidth dp | max = $maxWidth dp"
    }

}

internal data class BlockLayoutParentData(
    var minWidth: Dp = Dp.Unspecified,
    var maxWidth: Dp = Dp.Unspecified
)