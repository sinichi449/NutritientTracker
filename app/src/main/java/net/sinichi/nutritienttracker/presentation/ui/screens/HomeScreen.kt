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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.sinichi.nutritienttracker.core.entities.FoodItem
import net.sinichi.nutritienttracker.core.entities.MacroNutrientInfo
import net.sinichi.nutritienttracker.core.entities.formatTimestampToAmPm
import net.sinichi.nutritienttracker.presentation.states.HomeUiState
import net.sinichi.nutritienttracker.presentation.ui.theme.NutritientTrackerTheme

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onDeleteItem: (FoodItem) -> Unit,
    onNavigateToEditFood: (String) -> Unit,
    onNavigateToSettings: () -> Unit = {},
) {
    Scaffold(
        topBar = { NutritientTrackerHeader() },
//        bottomBar = { AppBottomNavigation(onFabClick = { showAddFoodDialog = true }) },
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
                DateSelector(onEditClick = onNavigateToSettings)
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
            // --- REFACTORED RECENT FOODS SECTION ---
            // We build the card's content directly into the LazyColumn
            // to provide a unique key for each food item.

            // 1. Card Header
            item {
                RecentFoodsHeader()
            }

            // 2. The list of food items, now with the correct function call
            items(
                items = uiState.recentFoods,
                key = { foodItem -> foodItem.id } // The key is crucial
            ) { foodItem ->
                RecentFoodItemRow(
                    item = foodItem,
                    onDelete = { onDeleteItem(foodItem) }, // Assuming you have a mapper
                    onEdit = { onNavigateToEditFood(foodItem.id) },
                )
            }

            // 3. Card Footer
            item {
                CardFooter()
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
fun DateSelector(onEditClick: () -> Unit = {}) {
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
            IconButton(onClick = onEditClick) {
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
fun RecentFoodsCard(
    recentFoods: List<FoodItem>,
    onDeleteItem: (FoodItem) -> Unit,
    onEditItem: (String) -> Unit,
) {
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

            if (recentFoods.isEmpty()) {
                // TODO
            } else {
                Column {
                    recentFoods.forEach { foodItem ->
                        RecentFoodItemRow(
                            item = foodItem,
                            onDelete = { onDeleteItem(foodItem) },
                            onEdit = { onEditItem(foodItem.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentFoodItemRow(
    item: FoodItem,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    // 1. This state tracks the swipe gesture.
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            // This lambda is triggered when the swipe is completed.
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete() // Call the delete function passed from the ViewModel.
                true // Return true to confirm the state change (item is dismissed).
            } else {
                false // Don't dismiss for other swipe directions.
            }
        },
        // We can set a positional threshold, e.g., swipe past 50% to trigger.
        positionalThreshold = { it * .50f }
    )

    // 2. This is the main composable that provides the swipe functionality
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color = when (dismissState.targetValue) {
                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                else -> Color.Transparent
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1f else 0.75f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onEdit) // The item itself is clickable for editing.
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = item.name, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = formatTimestampToAmPm(item.timestamp),
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
    }
}

@Composable
fun RecentFoodsHeader() {
    Card(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
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
        }
    }
}

@Composable
fun CardFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
            )
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
        FoodItem("Chicken Salad", "12:15 PM", carbs = 10.0, protein = 30.0, fat = 21.1),
        FoodItem("Apple", "10:02 AM", carbs = 25.0, protein = 0.5, fat = 0.3),
        FoodItem("Protein Shake", "07:30 AM", carbs = 5.0, protein = 25.0, fat = 3.3),
        FoodItem("Greek Yogurt", "07:30 AM", carbs = 10.0, protein = 15.0, fat = 5.6)
    )

    NutritientTrackerTheme {
        HomeScreen(
            uiState = HomeUiState(
                dailyIntakeProgress = progress,
                caloriesConsumed = consumed,
                calorieGoal = goal,
                macroNutrients = macroData,
                recentFoods = recentFoods
            ),
            onDeleteItem = {},
            onNavigateToEditFood = {},
        )
    }
}
