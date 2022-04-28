package com.udit.sample.playground.data

import androidx.compose.runtime.Immutable

@Immutable
data class Person(
    val firstName: String,
    val lastName: String
)