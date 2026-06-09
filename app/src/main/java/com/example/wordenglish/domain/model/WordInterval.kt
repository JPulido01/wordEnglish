package com.example.wordenglish.domain.model

enum class WordInterval(val hours: Int, val label: String) {
    ONE_HOUR(1, "1 hour"),
    THREE_HOURS(3, "3 hours"),
    SIX_HOURS(6, "6 hours"),
    EIGHT_HOURS(8, "8 hours"),
    TWELVE_HOURS(12, "12 hours"),
    TWENTY_FOUR_HOURS(24, "24 hours")
}
