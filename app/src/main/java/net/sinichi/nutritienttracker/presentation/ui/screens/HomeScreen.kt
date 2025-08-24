package net.sinichi.nutritienttracker.presentation.ui.screens

// Corrected import path for your data class
// Corrected import path for your theme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.sinichi.nutritienttracker.core.entities.MacroNutrientInfo
import net.sinichi.nutritienttracker.core.entities.RecentFoodItems
import net.sinichi.nutritienttracker.presentation.states.HomeUiState
import net.sinichi.nutritienttracker.presentation.ui.theme.NutritientTrackerTheme

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNavigateToAddFood: () -> Unit //
) {
    var showAddFoodDialog by remember { mutableStateOf(false) }

    if (showAddFoodDialog) {
        AddFoodOptionsDialog(
            onDismissRequest = { showAddFoodDialog = false },
            onAddManuallyClick = {
                showAddFoodDialog = false
                onNavigateToAddFood()
            },
            onScanClick = {
                showAddFoodDialog = false
                // TODO: Navigate to camera screen
            }
        )
    }

    Scaffold(
        topBar = { NutritientTrackerHeader() },
        bottomBar = { AppBottomNavigation(onFabClick = { showAddFoodDialog = true }) },
        // Use a theme color for the background
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { paddingValues ->
        // Use LazyColumn to make the whole screen scrollable
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            item {
                DateSelector()
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                // Pass dynamic data to the card
                DailyIntakeCard(
                    progress = uiState.dailyIntakeProgress,
                    consumed = uiState.caloriesConsumed,
                    goal = uiState.calorieGoal,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                // Pass dynamic data to the card
                NutritionCard(macroData = uiState.macroNutrients)
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                RecentFoodsCard(recentFoods = uiState.recentFoods)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritientTrackerHeader() {
    // Use CenterAlignedTopAppBar for centered titles
    CenterAlignedTopAppBar(
        title = {
            // Align the column's content to the center
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Nutritient Tracker",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    // Use theme color for primary text
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Live healthier, live happier.",
                    fontSize = 12.sp,
                    // Use theme color for secondary text
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        // Use the appropriate defaults for a centered app bar
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
    )
}

@Composable
fun DateSelector() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Calendar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Today", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DailyIntakeCard(progress: Float, consumed: Int, goal: Int) {
    Card(
        shape = RoundedCornerShape(20.dp),
        // Use theme color for card surfaces
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("⚡ Daily intake", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    // Use the theme's primary color for emphasis
                    color = MaterialTheme.colorScheme.primary
                )
            }
            CircularProgress(
                progress = progress,
                consumed = consumed,
                goal = goal
            )
        }
    }
}

@Composable
fun CircularProgress(progress: Float, consumed: Int, goal: Int) {
    var animationPlayed by remember { mutableStateOf(false) }
    val currentProgress = animateFloatAsState(
        targetValue = if (animationPlayed) progress else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 200),
        label = "progressAnimation"
    ).value

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    val backgroundRingColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val progressArcColor = MaterialTheme.colorScheme.primary

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(120.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 30f
            drawArc(
                color = backgroundRingColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = progressArcColor,
                startAngle = -90f,
                sweepAngle = 360 * currentProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = consumed.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Text(
                text = goal.toString(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NutritionCard(macroData: List<MacroNutrientInfo>) {
    Card(
        // Added clickable modifier
        modifier = Modifier.clickable { /* TODO: Handle navigation to nutrition details */ },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🕒 Nutritions", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to details",
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            macroData.forEach { macroInfo ->
                MacroNutrientBar(info = macroInfo)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MacroNutrientBar(info: MacroNutrientInfo) {
    var animationPlayed by remember { mutableStateOf(false) }
    val currentProgress = animateFloatAsState(
        targetValue = if (animationPlayed) (info.consumed / info.goal).toFloat() else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "macroProgress"
    ).value

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    val color = when (info.name) {
        "Carbs" -> MaterialTheme.colorScheme.primary
        "Fat" -> MaterialTheme.colorScheme.secondary
        "Protein" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column {
        Row {
            Text(
                text = "${info.name} ${info.percentage}%",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${String.format("%.1f", info.consumed)}g / ${String.format("%.1f", info.goal)}g",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun RecentFoodsCard(recentFoods: List<RecentFoodItems>) {
    Card(
        // Added clickable modifier
        modifier = Modifier.clickable { /* TODO: Handle navigation to recent foods list */ },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🍽️ Recent Foods", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to details",
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.surfaceContainer)
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                recentFoods.forEach { foodItem ->
                    RecentFoodItemRow(item = foodItem)
                }
            }
        }
    }
}

@Composable
fun RecentFoodItemRow(item: RecentFoodItems) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontWeight = FontWeight.SemiBold)
            Text(
                text = item.time,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "${item.calories} kcal",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AppBottomNavigation(onFabClick: () -> Unit) {
    val selectedIndex = 2 // Camera is selected
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
            BottomNavItem(
                icon = Icons.Outlined.GridView,
                label = "Dashboard",
                isSelected = selectedIndex == 0,
                modifier = Modifier.weight(1f)
            ) {}
            BottomNavItem(
                icon = Icons.Outlined.PieChartOutline,
                label = "Statistics",
                isSelected = selectedIndex == 1,
                modifier = Modifier.weight(1f)
            ) {}

            Spacer(modifier = Modifier.weight(1f))

            BottomNavItem(
                icon = Icons.Outlined.ChatBubbleOutline,
                label = "Chat",
                isSelected = selectedIndex == 3,
                modifier = Modifier.weight(1f)
            ) {}
            BottomNavItem(
                icon = Icons.Outlined.PersonOutline,
                label = "Profile",
                isSelected = selectedIndex == 4,
                modifier = Modifier.weight(1f)
            ) {}
        }

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
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label, fontSize = 10.sp) },
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

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HomeScreenPreview() {
    val progress = 0.68f
    val consumed = 1250
    val goal = 1850

    val macroData = listOf(
        MacroNutrientInfo(
            name = "Carbs",
            consumed = 109.0,
            goal = 198.0
        ),
        MacroNutrientInfo(
            name = "Fat",
            consumed = 13.5,
            goal = 52.0
        ),
        MacroNutrientInfo(
            name = "Protein",
            consumed = 34.2,
            goal = 122.0
        )
    )

    val recentFoods = listOf(
        RecentFoodItems("Chicken Salad", "12:15 PM", carbs = 10.0, protein = 30.0, fat = 21.1),
        RecentFoodItems("Apple", "10:02 AM", carbs = 25.0, protein = 0.5, fat = 0.3),
        RecentFoodItems("Protein Shake", "07:30 AM", carbs = 5.0, protein = 25.0, fat = 3.3),
        RecentFoodItems("Greek Yogurt", "07:30 AM", carbs = 10.0, protein = 15.0, fat = 5.6)
    )

    NutritientTrackerTheme {
        HomeScreen(
            HomeUiState(
                dailyIntakeProgress = progress,
                caloriesConsumed = consumed,
                calorieGoal = goal,
                macroNutrients = macroData,
                recentFoods = recentFoods
            ), {}
        )
    }
}
