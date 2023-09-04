package com.example.koinandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.koinandroid.ui.theme.KoinAndroidTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        setContent {
            KoinAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val forecastData by viewModel.daysForecastList.observeAsState()
                    val onError by viewModel.errorMessage.observeAsState()
                    if (forecastData != null) {
                        HomeScreen(data = forecastData!!)
                    } else
                        LoadingView()
                    onError?.let { error ->
                        ShowErrorMessage(error = error, viewModel = viewModel)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDaysForecast()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowErrorMessage(
    error: String,
    viewModel: MainViewModel
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Box(modifier = Modifier.padding(it)) {
            LaunchedEffect(snackBarHostState) {
                val result =
                    snackBarHostState.showSnackbar(
                        error,
                        actionLabel = "Retry",
                        duration = SnackbarDuration.Indefinite,
                    )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        viewModel.getDaysForecast()
                    }

                    SnackbarResult.Dismissed -> {
                        snackBarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(data: List<Data>) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(data) { item ->
                ForecastItem(item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KoinAndroidTheme {}
}

@Composable
fun ForecastItem(data: Data) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .padding(vertical = 7.5.dp, horizontal = 15.dp)
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${data.temp.min.toInt()} ℃ - ${data.temp.max.toInt()} ℃",
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Blue,
                        )
                    )
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = "http://openweathermap.org/img/wn/${data.weather[0].icon}.png",
                            filterQuality = FilterQuality.High,
                            contentScale = ContentScale.Crop
                        ), contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Rain: ${data.rain}%",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray,
                        )
                    )
                    Text(
                        "Humidity: ${data.humidity}%",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray,
                        )
                    )
                    Text(
                        "Wind: ${data.speed}m/s",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x20000000))
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }
}