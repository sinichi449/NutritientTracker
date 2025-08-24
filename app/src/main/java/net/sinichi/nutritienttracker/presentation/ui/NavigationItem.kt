package net.sinichi.nutritienttracker.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.ui.graphics.vector.ImageVector

// The base sealed class remains the same
sealed class NavigationItem(
    open val route: String, // 'open' allows overriding if needed, though direct access is fine for objects
    val title: String,
    val icon: ImageVector,
) {
    data object Dashboard : NavigationItem("dashboard", "Dashboard", Icons.Outlined.GridView)
    data object Statistics : NavigationItem("statistics", "Statistics", Icons.Outlined.PieChartOutline)
    data object Chat : NavigationItem("chat", "Chat", Icons.Outlined.ChatBubbleOutline)
    data object Profile : NavigationItem("profile", "Profile", Icons.Outlined.PersonOutline)

    data object AddFood : NavigationItem("add_food", "Add Food", Icons.Outlined.Add)
    // --- Modified Edit object ---
    data object Edit : NavigationItem("edit/{foodId}", "Edit", Icons.Outlined.Edit) {
        /**
         * The base route pattern with a placeholder for the foodId.
         * Used by NavHost to define the destination.
         */
        const val routeWithArgument = "edit/{foodId}"

        /**
         * The argument name as used in the route pattern.
         */
        const val foodIdArgument = "foodId"

        /**
         * Creates the actual navigation route to the Edit screen for a specific food item.
         * @param foodId The ID of the food item to edit.
         * @return The complete route string, e.g., "edit/123".
         */
        fun createRoute(foodId: String): String {
            return "edit/$foodId"
        }
    }
}