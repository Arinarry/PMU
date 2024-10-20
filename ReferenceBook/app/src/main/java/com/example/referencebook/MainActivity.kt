package com.example.referencebook

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.referencebook.ui.theme.ReferenceBookTheme

class MainActivity : ComponentActivity() {
    private val vm: MVVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReferenceBookTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.News) }

                when (currentScreen) {
                    Screen.News -> NewsScreen(vm = vm) { currentScreen = it }
                    Screen.OpenGL -> OpenGLScreen(onScreenChange = { currentScreen = it })
                    Screen.MoonInfo -> MoonView(onScreenChange = { currentScreen = it })
                }
            }
        }
    }
}

enum class Screen {
    News,
    OpenGL,
    MoonInfo
}

@Composable
fun NewsScreen(vm: MVVM, onScreenChange: (Screen) -> Unit) {
    val news by vm.currentNews.collectAsState()
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.weight(1f)) {
            NewsCard(news[0], onLike = { vm.getLikes(0) }, Modifier.weight(1f))
            NewsCard(news[1], onLike = { vm.getLikes(1) }, Modifier.weight(1f))
        }
        Row(Modifier.weight(1f)) {
            NewsCard(news[2], onLike = { vm.getLikes(2) }, Modifier.weight(1f))
            NewsCard(news[3], onLike = { vm.getLikes(3) }, Modifier.weight(1f))
        }
        Button(
            onClick = {onScreenChange(Screen.OpenGL)},
            modifier = Modifier.padding(3.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Gray),

        ) {
            Text("Go to space >", fontSize = 20.sp)

        }
    }
}

@Composable
fun NewsCard(news: Data, onLike: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp).background(Color.LightGray),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = modifier.weight(0.6f).fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = news.imageUrl),
                contentDescription = null,
                modifier = modifier.fillMaxSize()
            )
        }
        Box(
            modifier = modifier.weight(0.3f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = news.title,
                fontSize = 15.sp)
        }
        Row(
            modifier = modifier.weight(0.1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = " \uD83D\uDC4D: ${news.likes}",
                fontSize = 20.sp)
            Button(onClick = onLike,
                    colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White),
                    modifier = Modifier.width(100.dp).height(35.dp))
            {
                Text(text = "Like",
                    fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun OpenGLScreen(onScreenChange: (Screen) -> Unit) {
    var currentPlanetIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                GLSurfaceView(context).apply {
                    setEGLContextClientVersion(1)
                    setRenderer(MyRender(context) { currentPlanetIndex })
                }
            },
            update = {
                it.requestRender()
            }
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { moveLeft(currentPlanetIndex) { newIndex -> currentPlanetIndex = newIndex } },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
                modifier = Modifier.width(80.dp)) {
                Text("<", fontSize = 20.sp)
            }
            Button(onClick = { if (currentPlanetIndex == 4) {onScreenChange(Screen.MoonInfo)} },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
                modifier = Modifier.width(150.dp)) {
                Text("Информация",fontSize = 15.sp)
            }
            Button(onClick = { moveRight(currentPlanetIndex) { newIndex -> currentPlanetIndex = newIndex } },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
                modifier = Modifier.width(80.dp)) {
                Text(">", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun MoonView(onScreenChange: (Screen) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                GLSurfaceView(context).apply {
                    setEGLContextClientVersion(2)
                    setRenderer(MoonRender(context))

                }
            },
            update = {
                it.requestRender()
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),

        ) {
            Text(
                text = "Информация о Луне",
                color = Color.White,
                fontSize = 24.sp
            )
            Text(
                text = "Луна — единственный естественный спутник Земли...",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {onScreenChange(Screen.OpenGL)},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
                modifier = Modifier.width(145.dp)) {
                Text("Назад", fontSize = 15.sp)
            }
        }
    }
}

fun moveRight(currentIndex: Int, updateIndex: (Int) -> Unit) {
    val planetsCount = 11
    updateIndex((currentIndex + 1) % planetsCount)
}

fun moveLeft(currentIndex: Int, updateIndex: (Int) -> Unit) {
    val planetsCount = 11
    updateIndex(if (currentIndex - 1 < 0) planetsCount - 1 else currentIndex - 1)
}


