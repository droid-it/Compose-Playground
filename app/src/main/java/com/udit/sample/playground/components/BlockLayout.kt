package com.udit.sample.playground.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun CompositeBlock(modifier: Modifier = Modifier) {
    BlockLayout(
        modifier = modifier
            .background(Color.Gray)
    ) {
        DataBlock2(
            modifier = Modifier.minMaxWidth(100.dp, 120.dp)
        )
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
fun DataBlock2(modifier: Modifier = Modifier) {
    Text(text = "Block number 2", modifier = modifier)
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