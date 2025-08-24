package net.sinichi.nutritienttracker.presentation

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.sinichi.nutritienttracker.NutrientTrackerApp
import net.sinichi.nutritienttracker.presentation.ui.NavigationItem
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
                // Add this block to control the system bar icon colors
                val view = LocalView.current
                if (!view.isInEditMode) {
                    LaunchedEffect(Unit) { // The key can just be Unit now
                        val window = (view.context as Activity).window
                        val insetsController = WindowCompat.getInsetsController(window, view)

                        // Always set to true because your app is always light
                        insetsController.isAppearanceLightStatusBars = true
                    }
                }

                val navController = rememberNavController()

                var showAddFoodDialog by remember { mutableStateOf(false) }

                if (showAddFoodDialog) {
                    AddFoodOptionsDialog(
                        onDismissRequest = { showAddFoodDialog = false },
                        onAddManuallyClick = {
                            showAddFoodDialog = false
                            navController.navigate(NavigationItem.AddFood.route)
                        },
                        onScanClick = {
                            showAddFoodDialog = false
                            // TODO: Navigate to camera screen
                        }
                    )
                }

                // This factory is now only for ViewModels without arguments
                val viewModelFactory = ViewModelFactory(application.foodRepository)

                Scaffold(
                    bottomBar = {
                        AppBottomNavigation(
                            navController = navController,
                            onFabClick = { showAddFoodDialog = true}
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = NavigationItem.Dashboard.route,
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                    ) {
                        // Home Screen Destination
                        composable(
                            route = NavigationItem.Dashboard.route,
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
                                    navController.navigate(NavigationItem.Edit.createRoute(foodId))
                                },
                            )
                        }
                        composable(NavigationItem.Statistics.route) { Box { Text("Statistics Screen") } }
                        composable(NavigationItem.Chat.route) { Box { Text("Chat Screen") } }
                        composable(NavigationItem.Profile.route) { Box { Text("Profile Screen") } }

                        // Add Food Screen Destination
                        composable(
                            route = NavigationItem.AddFood.route,
                            // Animation when this screen enters (comes from AddFood)
                            enterTransition = { slideInHorizontally(initialOffsetX = { 300 }) + fadeIn() },
                            popExitTransition = { slideOutHorizontally(targetOffsetX = { 300 }) + fadeOut() }
                        ) {
                            val addFoodViewModel: AddFoodViewModel =
                                viewModel(factory = viewModelFactory)
                            val uiState by addFoodViewModel.uiState.collectAsState()
                            AddFoodScreen(
                                uiState = uiState,
                                onNameChange = addFoodViewModel::onNameChange,
                                onCarbsChange = addFoodViewModel::onCarbsChange,
                                onProteinChange = addFoodViewModel::onProteinChange,
                                onFatChange = addFoodViewModel::onFatChange,
                                onQuantityChange = addFoodViewModel::onQuantityChange,
                                onSaveClick = addFoodViewModel::saveFoodItem,
                                onNavigateBack = {
                                    // 4. Update back navigation call
                                    navController.popBackStack()
                                }
                            )
                        }

                        // Edit Food Screen Destination
                        composable(
                            route = NavigationItem.Edit.routeWithArgument,
                            arguments = listOf(navArgument("foodId") { type = NavType.StringType })
                        ) { backStackEntry -> // Get the backStackEntry

                            // Retrieve the foodId from the navigation arguments
                            val foodId = backStackEntry.arguments?.getString("foodId")

                            // Create the EditFoodViewModel with its specific foodId
                            val editFoodViewModel: EditFoodViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        // Make sure the ID is not null before creating
                                        return EditFoodViewModel(
                                            application.foodRepository,
                                            foodId!!
                                        ) as T
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
                                onQuantityChange = editFoodViewModel::onQuantityChange,
                                onSaveClick = editFoodViewModel::updateFoodItem,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AppBottomNavigation(
    navController: NavController,
    onFabClick: () -> Unit = {},
) {
    val items = listOf(
        NavigationItem.Dashboard,
        NavigationItem.Statistics,
        NavigationItem.Chat,
        NavigationItem.Profile,
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.clip(RoundedCornerShape(20.dp)),
            tonalElevation = 4.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Left items
            BottomNavItem(
                item = items[0],
                isSelected = currentDestination?.hierarchy?.any { it.route == items[0].route } == true,
                onClick = { navController.navigate(items[0].route) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } }
            )
            BottomNavItem(
                item = items[1],
                isSelected = currentDestination?.hierarchy?.any { it.route == items[1].route } == true,
                onClick = { navController.navigate(items[1].route) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } }
            )

            // Spacer for the FAB
            Spacer(Modifier.weight(1f, true))

            // Right items
            BottomNavItem(
                item = items[2],
                isSelected = currentDestination?.hierarchy?.any { it.route == items[2].route } == true,
                onClick = { navController.navigate(items[2].route) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } }
            )
            BottomNavItem(
                item = items[3],
                isSelected = currentDestination?.hierarchy?.any { it.route == items[3].route } == true,
                onClick = { navController.navigate(items[3].route) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } }
            )
        }

        // Floating Action Button for the Camera
        FloatingActionButton(
            onClick = onFabClick,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .offset(y = (-10).dp)
                .size(70.dp)
        ) {
            Icon(
                Icons.Outlined.PhotoCamera,
                contentDescription = "Scan Food",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun RowScope.BottomNavItem(
    item: NavigationItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = { Icon(item.icon, contentDescription = item.title) },
        label = { Text(item.title, fontSize = 10.sp) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
fun AddFoodOptionsDialog(
    onDismissRequest: () -> Unit,
    onAddManuallyClick: () -> Unit,
    onScanClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add Food Item") },
        text = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onAddManuallyClick)
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Add Manually")
                    Spacer(Modifier.width(16.dp))
                    Text("Add Manually")
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onScanClick)
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.PhotoCamera, contentDescription = "Scan with Camera")
                    Spacer(Modifier.width(16.dp))
                    Text("Scan with Camera")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}