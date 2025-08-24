package net.sinichi.nutritienttracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import net.sinichi.nutritienttracker.NutrientTrackerApp
import net.sinichi.nutritienttracker.presentation.ui.screens.AddFoodScreen
import net.sinichi.nutritienttracker.presentation.ui.screens.HomeScreen
import net.sinichi.nutritienttracker.presentation.ui.theme.NutritientTrackerTheme
import net.sinichi.nutritienttracker.presentation.viewmodels.AddFoodViewModel
import net.sinichi.nutritienttracker.presentation.viewmodels.HomeViewModel
import net.sinichi.nutritienttracker.presentation.viewmodels.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add this line to draw behind the system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Get the application instance to create the factory
        val application = applicationContext as NutrientTrackerApp
        val viewModelFactory = ViewModelFactory(application.foodRepository)

        enableEdgeToEdge()
        setContent {
            NutritientTrackerTheme {
                // State to control which screen is shown
                var currentScreen by remember { mutableStateOf("home") }

                val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
                val addFoodViewModel: AddFoodViewModel = viewModel(factory = viewModelFactory)

                when (currentScreen) {
                    "home" -> {
                        val uiState by homeViewModel.uiState.collectAsState()
                        HomeScreen(
                            uiState = uiState,
                            onNavigateToAddFood = { currentScreen = "add_food" }
                        )
                    }
                    "add_food" -> {
                        // Use BackHandler to intercept the back press
                        BackHandler {
                            currentScreen = "home" // Go back to the home screen
                        }

                        // Collect the state here
                        val uiState by addFoodViewModel.uiState.collectAsState()
                        AddFoodScreen(
                            uiState = uiState,
                            onNameChange = addFoodViewModel::onNameChange,
                            onCarbsChange = addFoodViewModel::onCarbsChange,
                            onProteinChange = addFoodViewModel::onProteinChange,
                            onFatChange = addFoodViewModel::onFatChange,
                            onSaveClick = addFoodViewModel::saveFoodItem,
                            onNavigateBack = { currentScreen = "home" }
                        )
                    }
                }
            }
        }
    }
}