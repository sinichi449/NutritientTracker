package net.sinichi.nutritienttracker.core.entities

fun calculateCalories(carbs: Double, protein: Double, fat: Double): Double {
    return carbs * 4 + protein * 4 + fat * 9
}