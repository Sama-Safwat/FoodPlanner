package com.example.foodplanner.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val iso = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
    private val shortFormat = SimpleDateFormat("EEE dd", Locale.getDefault())
    private val rangeFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    fun toIso(date: Date): String = iso.format(date)

    /** "2026-08-19" → "Wednesday, 19 Aug" */
    fun toDisplay(isoDate: String): String =
        iso.parse(isoDate)?.let { displayFormat.format(it) } ?: isoDate

    /** "2026-08-19" → "Wed 19" (for the day chips) */
    fun toShort(isoDate: String): String =
        iso.parse(isoDate)?.let { shortFormat.format(it) } ?: isoDate

    /** "15 Aug - 21 Aug" (week header) */
    fun toRange(dates: List<String>): String {
        if (dates.isEmpty()) return ""
        val first = iso.parse(dates.first())?.let { rangeFormat.format(it) } ?: ""
        val last = iso.parse(dates.last())?.let { rangeFormat.format(it) } ?: ""
        return "$first - $last"
    }

    /** 7 real dates of any week — offset 0 = this week, 1 = next, -1 = previous */
    fun weekDates(offset: Int = 0): List<String> {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = Calendar.SATURDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        cal.add(Calendar.WEEK_OF_YEAR, offset)
        return (0 until 7).map {
            toIso(cal.time).also { cal.add(Calendar.DAY_OF_YEAR, 1) }
        }
    }
}