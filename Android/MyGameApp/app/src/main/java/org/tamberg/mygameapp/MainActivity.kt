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

class MyGameDotModel(val col: MyGameColModel) {
    var enabled by mutableStateOf(true)
        private set

    var checked by mutableStateOf(true)
        private set

    fun update() {
        enabled = checked && col.enabled
    }

    fun setChecked2(value: Boolean) {
        //assert(col.enabled)
        checked = value
        if (value == false) {
            for (c in col.board.cols) {
                c.setSelected2(false)
            }
            col.setSelected2(true)
            col.board.model.stepEnabled = true
        }
        update()
    }
}

class MyGameColModel(val board: MyGameBoardModel, n: Int) {
    var enabled by mutableStateOf(false)
        private set

    var selected by mutableStateOf(false)
        private set

    val dots = Array(n) { MyGameDotModel(this) }

    fun update() {
        enabled = selected && board.enabled
        for (dot in dots) {
            dot.update()
        }
    }

    fun setSelected2(value: Boolean) {
        //assert(board.enabled)
        selected = value
        update();
    }
}

class MyGameBoardModel(val model: MyGameViewModel) {
    var enabled by mutableStateOf(false)
        private set

    var activated by mutableStateOf(false)
        private set

    val cols = listOf(
        MyGameColModel(this, 3),
        MyGameColModel(this, 5),
        MyGameColModel(this, 6),
        MyGameColModel(this, 6),
        MyGameColModel(this, 5),
        MyGameColModel(this, 3)
    )

    fun update() {
        enabled = activated
        for (col in cols) {
            col.update()
        }
    }

    fun setActivated2(value: Boolean) {
        activated = value
        update()
    }
}

enum class State { START, PLAY, PASS, WIN, LOSE }

class MyGameViewModel: ViewModel() {
    var state = State.START
    var info by mutableStateOf("");
    var label by mutableStateOf("");
    var stepEnabled by mutableStateOf(false)

    val board = MyGameBoardModel(this)

    fun update() {
        if (state == State.START) {
            info = "Bubble Game"
            label = "Start"
            for (col in board.cols) {
                for (dot in col.dots) {
                    dot.setChecked2(true)
                }
                col.setSelected2(true)
            }
            board.setActivated2(false)
            stepEnabled = true
        } else if (state == State.PLAY) {
            for (col in board.cols) {
                col.setSelected2(true)
            }
            info = "Tap some boxes..."
            label = "Next"
            board.setActivated2(true)
            stepEnabled = false
        } else if (state == State.PASS) {
            info = "Pass to other player..."
            label = "Ready"
            board.setActivated2(false)
            stepEnabled = true
        } else if (state == State.WIN) {
            info = "You win :)"
            label = "Again"
            board.setActivated2(false)
            stepEnabled = true
        } else if (state == State.LOSE) {
            info = "You lose :("
            label = "Again"
            board.setActivated2(false)
            stepEnabled = true
        }
    }

    private fun count(): Int {
        var count = 0
        for (col in board.cols) {
            for (dot in col.dots) {
                if (dot.checked) {
                    count++
                }
            }
        }
        return count
    }

    fun step() {
        if (state == State.START) {
            state = State.PLAY
        } else if (state == State.PLAY) {
            val count = count()
            if (count == 1) {
                state = State.WIN
            } else if (count == 0) {
                state = State.LOSE
            } else {
                state = State.PASS
            }
        } else if (state == State.PASS) {
            state = State.PLAY
        } else if (state == State.WIN) {
            state = State.START
        } else if (state == State.LOSE) {
            state = State.START
        }
        update()
    }

    init {
        update()
    }
}

@Composable
fun MyGameColumn(
    col: MyGameColModel,
    modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly)
    {
        Column {
            for (dot in col.dots) {
                Checkbox(
                    enabled = dot.enabled,
                    checked = dot.checked,
                    onCheckedChange = {
                        dot.setChecked2(it)
                    })
            }
        }
    }
}

@Composable
fun MyGameBoard(
    board: MyGameBoardModel,
    modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        for (col in board.cols) {
            MyGameColumn(col)
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
            Text(model.info, fontSize = 23.sp)
        }
        Spacer(modifier = Modifier.weight(0.1f))
        MyGameBoard(board = model.board, modifier = Modifier.weight(0.8f))
        Spacer(modifier = Modifier.weight(0.1f))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { model.step() }, enabled = model.stepEnabled) {
                Text(model.label)
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