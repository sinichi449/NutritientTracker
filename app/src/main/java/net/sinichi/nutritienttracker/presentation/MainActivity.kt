package net.sinichi.nutritienttracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import net.sinichi.nutritienttracker.NutrientTrackerApp
import net.sinichi.nutritienttracker.presentation.ui.screens.HomeScreen
import net.sinichi.nutritienttracker.presentation.ui.theme.NutritientTrackerTheme
import net.sinichi.nutritienttracker.presentation.viewmodels.HomeViewModel
import net.sinichi.nutritienttracker.presentation.viewmodels.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add this line to draw behind the system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        setContent {
            NutritientTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Get a reference to the application context to access the repository
                    val application = LocalContext.current.applicationContext as NutrientTrackerApp
                    val viewModel: HomeViewModel = viewModel(
                        factory = ViewModelFactory(application.foodRepository)
                    )
                    HomeScreen()
                }
            }
        }
    }
}