package net.sinichi.nutritienttracker.data.local.mapper

// Import the core domain entity
import net.sinichi.nutritienttracker.core.entities.FoodItem
// Import the local database entity
import net.sinichi.nutritienttracker.data.local.FoodItemEntity

/**
 * Converts a database [FoodItemEntity] to a domain [FoodItem].
 * This mapper lives in the 'data' layer because it depends on both
 * FoodItemEntity (data layer) and FoodItem (core layer).
 * The 'core' layer does not know about this mapper or FoodItemEntity.
 */
fun FoodItemEntity.toDomain(): FoodItem {
    return FoodItem(
        id = this.id.toString(),
        name = this.name,
        protein = this.protein,
        carbs = this.carbs,
        fat = this.fat,
        timestamp = this.timestamp
    )
}

/**
 * Converts a domain [FoodItem] to a database [FoodItemEntity].
 * This mapper also lives in the 'data' layer.
 */
fun FoodItem.toEntity(): FoodItemEntity {
    return FoodItemEntity(
        id = 0, // If id is 0 for a new item, Room handles auto-generation
        name = this.name,
        protein = this.protein,
        carbs = this.carbs,
        fat = this.fat,
        timestamp = this.timestamp
    )
}

// Optional: List mappers also reside here
fun List<FoodItemEntity>.toDomain(): List<FoodItem> {
    return this.map { it.toDomain() }
}

fun List<FoodItem>.toEntity(): List<FoodItemEntity> {
    return this.map { it.toEntity() }
}