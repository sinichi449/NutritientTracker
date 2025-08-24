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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.sinichi.nutritienttracker.NutrientTrackerApp
import net.sinichi.nutritienttracker.presentation.ui.screens.AddFoodScreen
import net.sinichi.nutritienttracker.presentation.ui.screens.EditFoodScreen
import net.sinichi.nutritienttracker.presentation.ui.screens.HomeScreen
import net.sinichi.nutritienttracker.presentation.ui.theme.NutritientTrackerTheme
import net.sinichi.nutritienttracker.presentation.viewmodels.AddFoodViewModel
import net.sinichi.nutritienttracker.presentation.viewmodels.EditFoodViewModel
import net.sinichi.nutritienttracker.presentation.viewmodels.HomeViewModel
import net.sinichi.nutritienttracker.presentation.viewmodels.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add this line to draw behind the system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Get the application instance to create the factory
        val application = applicationContext as NutrientTrackerApp

        enableEdgeToEdge()
        setContent {
            NutritientTrackerTheme {
                val navController = rememberNavController()

                // This factory is now only for ViewModels without arguments
                val viewModelFactory = ViewModelFactory(application.foodRepository)

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    // Home Screen Destination
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
                            onDeleteItem = homeViewModel::deleteFoodItem,
                            onNavigateToEditFood = { foodId ->
                                navController.navigate("edit_food/$foodId")
                            },
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

                    // Edit Food Screen Destination
                    composable(
                        route = "edit_food/{foodId}",
                        arguments = listOf(navArgument("foodId") { type = NavType.StringType })
                    ) { backStackEntry -> // Get the backStackEntry

                        // Retrieve the foodId from the navigation arguments
                        val foodId = backStackEntry.arguments?.getString("foodId")

                        // Create the EditFoodViewModel with its specific foodId
                        val editFoodViewModel: EditFoodViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    // Make sure the ID is not null before creating
                                    return EditFoodViewModel(application.foodRepository, foodId!!) as T
                                }
                            }
                        )

                        val uiState by editFoodViewModel.uiState.collectAsStateWithLifecycle()
                        EditFoodScreen(
                            uiState = uiState,
                            onNameChange = editFoodViewModel::onNameChange,
                            onCarbsChange = editFoodViewModel::onCarbsChange,
                            onProteinChange = editFoodViewModel::onProteinChange,
                            onFatChange = editFoodViewModel::onFatChange,
                            onSaveClick = editFoodViewModel::updateFoodItem,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}