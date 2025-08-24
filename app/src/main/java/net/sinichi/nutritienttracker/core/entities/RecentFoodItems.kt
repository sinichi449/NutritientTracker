package net.sinichi.nutritienttracker.core.entities

data class RecentFoodItems(
    val name: String,
    val time: String,
    val carbs: Double,
    val protein: Double,
    val fat: Double
) {
    // Calories are now a calculated property
    val calories: Int
        get() = calculateCalories(carbs, protein, fat).toInt()

    companion object {
        fun List<FoodItem>.toRecentFoodItems(): List<RecentFoodItems> {
            return this.map {
                RecentFoodItems(
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