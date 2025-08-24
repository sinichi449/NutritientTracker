package net.sinichi.nutritienttracker.core.entities

data class MacroNutrientInfo(
    val name: String,
    val consumed: Double,
    val goal: Double,
) {
    val percentage: Int
         get() = if (goal > 0) ((consumed / goal) * 100).toInt() else 0
}
