package net.sinichi.nutritienttracker.core

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Calculates the total calories for a given amount of carbohydrates, protein, and fat.
 *
 * This function uses the standard Atwater general factors for calorie calculation:
 * - Carbohydrates: 4 calories per gram
 * - Protein: 4 calories per gram
 * - Fat: 9 calories per gram
 *
 * @param carbs The amount of carbohydrates in grams.
 * @param protein The amount of protein in grams.
 * @param fat The amount of fat in grams.
 * @return The total calculated calories as a Double.
 */
fun calculateCalories(carbs: Double, protein: Double, fat: Double): Double {
    val caloriesFromCarbs = carbs * 4.0
    val caloriesFromProtein = protein * 4.0
    val caloriesFromFat = fat * 9.0
    return caloriesFromCarbs + caloriesFromProtein + caloriesFromFat
}

/**
 * Formats a timestamp (in milliseconds) to a time string like "h:mm a" (e.g., "9:30 AM").
 * This version uses SimpleDateFormat for compatibility with Android API levels below 26,
 * including Nougat 7.0 (API 24 & 25), without requiring core library desugaring
 * for this specific formatting task.
 *
 * @param timestamp The time in milliseconds since the epoch.
 * @return A formatted time string (e.g., "3:45 PM").
 */
fun formatTimestampToAmPm(timestamp: Long): String {
    val date = Date(timestamp) // Create a Date object from the milliseconds timestamp
    // "h" for 12-hour format (1-12)
    // "mm" for minutes
    // "a" for AM/PM marker
    // Locale.US is used to ensure AM/PM is in English.
    // Use system's default locale: SimpleDateFormat("h:mm a", Locale.getDefault())
    // if you want localized AM/PM markers.
    val sdf = SimpleDateFormat("h:mm a", Locale.US)
    return sdf.format(date)
}

// Helper functions to check if a date is today or yesterday
fun isToday(timestamp: Long): Boolean {
    val today = Calendar.getInstance()
    val comparison = Calendar.getInstance().apply { timeInMillis = timestamp }
    return today.get(Calendar.YEAR) == comparison.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == comparison.get(Calendar.DAY_OF_YEAR)
}

fun isYesterday(timestamp: Long): Boolean {
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val comparison = Calendar.getInstance().apply { timeInMillis = timestamp }
    return yesterday.get(Calendar.YEAR) == comparison.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == comparison.get(Calendar.DAY_OF_YEAR)
}
