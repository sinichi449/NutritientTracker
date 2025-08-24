package net.sinichi.nutritienttracker.core.entities

import net.sinichi.nutritienttracker.core.calculateCalories
import java.util.UUID

data class FoodItem(
    val id: String = UUID.randomUUID().toString(), // Unique ID from Firebase
    val name: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val carbs: Double = 0.0,    // in grams
    val protein: Double = 0.0,  // in grams
    val fat: Double = 0.0,       // in grams
    val category: FoodCategory = FoodCategory.UNASSIGNED,
) {
    val calories = calculateCalories(carbs, protein, fat)
}
