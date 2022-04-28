package com.udit.sample.playground

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.udit.sample.playground.data.Person
import com.udit.sample.playground.ui.theme.PlaygroundTheme

private const val TAG = "COMPOSE"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RandomPerson()
                }
            }
        }
    }
}

@Composable
fun RandomPerson() {
    var firstName by remember {
        mutableStateOf("udit")
    }
    var lastName by remember {
        mutableStateOf("verma")
    }
    var person by remember {
        mutableStateOf(Person(firstName , lastName))
    }
    Column {
        Greeting(person = person)
        Greeting(firstName, lastName)
        Greeting(lastName)
        Button(onClick = {
            firstName = "new"
            person = Person(firstName, lastName)
        }) {
            Text(text = "Generate Person")
        }
    }
}

@Composable
fun Greeting(firstName: String, lastName: String) {
    Log.d(TAG, "separate: composition is triggered")
    Row {
        Log.d(TAG, "separate: composition is triggered for row")
        Text(text = "Hello $firstName")
        Spacer(Modifier.width(20.dp))
        Column {
            Log.d(TAG, "separate: composition is triggered for column")
            Text(text = "Hello $lastName")
        }
    }
}

@Composable
fun Greeting(person: Person) {
    Log.d(TAG, "composition is triggered")
    Row {
        Log.d(TAG, "composition is triggered for row")
        Text(text = "Hello ${person.firstName}")
        Spacer(Modifier.width(20.dp))
        Column {
            Log.d(TAG, "composition is triggered for column")
            Text(text = "Hello ${person.lastName}")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Log.d(TAG, "singled: composition is triggered")
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlaygroundTheme {
        Greeting("Android")
    }
}