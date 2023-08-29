package org.saintleva.heidelberg

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.saintleva.heidelberg.screens.AppNavGraph
import org.saintleva.heidelberg.ui.theme.HeidelbergCatechismReaderTheme

class MainActivity : ComponentActivity() {

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            HeidelbergCatechismReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(navController)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("lifecycle", "MainActivity.onSaveInstanceState()") // TODO: remove it
        Repository.savePositionToPrefs(this)
        super.onSaveInstanceState(outState)
    }
}