package com.myowntrip.app.ui.components.date

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
internal fun motSelectableDates(min: LocalDate, max: LocalDate): SelectableDates =
  object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
      val date = utcTimeMillis.toLocalDateUtc()
      return isDateInSelectableRange(date, min, max)
    }

    override fun isSelectableYear(year: Int): Boolean =
      year in min.year..max.year
  }
