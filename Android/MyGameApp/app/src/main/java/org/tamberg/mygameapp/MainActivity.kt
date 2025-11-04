package org.tamberg.mygameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.tamberg.mygameapp.ui.theme.MyGameAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyGameAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyGameScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

class MyGameDotData(b: Boolean) {
    var state by mutableStateOf(b)
}

class MyGameColData(n: Int) {
    val dots = Array(n) { MyGameDotData(true) }
}

class MyGameViewModel: ViewModel() {
    val cols = listOf(
        MyGameColData(3),
        MyGameColData(5),
        MyGameColData(6),
        MyGameColData(6),
        MyGameColData(5),
        MyGameColData(3))
}

@Composable
fun MyGameColumn(
    colData: MyGameColData,
    modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly)
    {
        Column {
            for (dot in colData.dots) {
                Checkbox(
                    enabled = dot.state,
                    checked = dot.state,
                    onCheckedChange = {
                        dot.state = it
                    })
            }
        }
    }
}

@Composable
fun MyGameScreen(
    modifier: Modifier = Modifier,
    model: MyGameViewModel = viewModel()
) {
    Column(modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Spacer(modifier = Modifier.weight(0.1f))
        Row(modifier = Modifier.fillMaxWidth().weight(0.1f),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Text("Tap some boxes...", fontSize = 23.sp)
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Row(modifier = Modifier.fillMaxWidth().weight(0.8f),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            for (col in model.cols) {
                MyGameColumn(col)
            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {}) {
                Text("Next")
            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    MyGameAppTheme {
        MyGameScreen()
    }
}