package net.sinichi.nutritienttracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                // 1. Create the NavController
                val navController = rememberNavController()

                // 2. Set up the NavHost
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    // Home Scree Destination
                    composable(
                        route = "home",
                        // Animation when this screen exits (goes to AddFood)
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }) + fadeOut() },
                        popEnterTransition = { slideInHorizontally(initialOffsetX = { -300 }) + fadeIn() }
                    ) {
                        val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
                        val uiState by homeViewModel.uiState.collectAsState()
                        HomeScreen(
                            uiState = uiState,
                            onNavigateToAddFood = {
                                // 3. Update navigation call
                                navController.navigate("add_food")
                            }
                        )
                    }

                    // Add Food Screen Destination
                    composable(
                        route = "add_food",
                        // Animation when this screen enters (comes from AddFood)
                        enterTransition = { slideInHorizontally(initialOffsetX = { 300 }) + fadeIn() },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { 300 }) + fadeOut() }
                    ) {
                        val addFoodViewModel: AddFoodViewModel = viewModel(factory = viewModelFactory)
                        val uiState by addFoodViewModel.uiState.collectAsState()
                        AddFoodScreen(
                            uiState = uiState,
                            onNameChange = addFoodViewModel::onNameChange,
                            onCarbsChange = addFoodViewModel::onCarbsChange,
                            onProteinChange = addFoodViewModel::onProteinChange,
                            onFatChange = addFoodViewModel::onFatChange,
                            onSaveClick = addFoodViewModel::saveFoodItem,
                            onNavigateBack = {
                                // 4. Update back navigation call
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}