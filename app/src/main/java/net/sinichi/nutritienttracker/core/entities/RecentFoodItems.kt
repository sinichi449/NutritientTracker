package net.sinichi.nutritienttracker.core.entities

data class RecentFoodItems(
    val id: String,
    val name: String,
    val time: String,
    val carbs: Double,
    val protein: Double,
    val fat: Double
) {
    // Calories are now a calculated property
    val calories: Int
        get() = calculateCalories(carbs, protein, fat).toInt()

    fun toFoodItem(): FoodItem {
        return FoodItem(
            id = id,
            name = name,
            timestamp = System.currentTimeMillis(),
            carbs = carbs,
            protein = protein,
            fat = fat
        )
    }

    companion object {
        fun List<FoodItem>.toRecentFoodItems(): List<RecentFoodItems> {
            return this.map {
                RecentFoodItems(
                    id = it.id,
                    name = it.name,
                    time = formatTimestampToAmPm(it.timestamp),
                    carbs = it.carbs,
                    protein = it.protein,
                    fat = it.fat
                )
            }
        }
    }
}