package net.sinichi.nutritienttracker.core.entities

enum class FoodCategory(val emoji: String) {
    MEAL("🍽️"),      // For breakfast, lunch, dinner
    SNACK("🍿"),      // For snacks
    DRINK("🍹"),      // For drinks
    FRUIT("🍎"),      // For fruits
    DESSERT("🍰"),    // For sweets and desserts
    UNASSIGNED("❔") // A default category
}