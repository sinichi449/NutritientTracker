package net.sinichi.nutritienttracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val timestamp: Long,
    val carbs: Double,
    val protein: Double,
    val fat: Double,
)