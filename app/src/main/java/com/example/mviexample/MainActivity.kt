package com.example.mviexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.api_lib.data.repository.ApiHelperImpl
import com.example.api_lib.data.repository.RetrofitBuilder
import com.example.mviexample.data.MainState
import com.example.mviexample.intent.MainIntent
import com.example.mviexample.presentation.viewmodel.MviViewModel
import com.example.mviexample.presentation.viewmodel.ViewModelFactory
import com.example.mviexample.ui.theme.MviExampleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MviViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelFactory(
            ApiHelperImpl(
                RetrofitBuilder.apiService
            )
        ).create(MviViewModel::class.java)

        setContent {
            val state = viewModel.state.collectAsState(MainState.Idle)
            val coroutineScope = rememberCoroutineScope()
            MviExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val stateUI = state.value) {
                        is MainState.Idle -> {
                            Greeting(name = "Idle", viewModel = viewModel)
                        }

                        is MainState.Loading -> {
                            Greeting(name = "Loading", viewModel = viewModel)
                            CircularProgressIndicator(
                                modifier = Modifier.size(64.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                            //  buttonFetchUser.visibility = View.GONE
                            //  progressBar.visibility = View.VISIBLE
                        }

                        is MainState.Users -> {
                            Greeting(name = "${stateUI.user.first()}", viewModel = viewModel)

                            // progressBar.visibility = View.GONE
                            // buttonFetchUser.visibility = View.GONE
                            // renderList(it.user)
                        }

                        is MainState.Error -> {
                            // progressBar.visibility = View.GONE
                            // buttonFetchUser.visibility = View.VISIBLE
                            Greeting(name = stateUI.error.toString(), viewModel = viewModel)
                            Toast.makeText(this, stateUI.error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: MviViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(16.dp)
        )
        Button(onClick = {
            coroutineScope.launch {
                viewModel.userIntent.send(MainIntent.FetchUser)
            }
        }) {
            Text(text = "fetch_user")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MviExampleTheme {
//        Greeting("Android")
//    }
//}