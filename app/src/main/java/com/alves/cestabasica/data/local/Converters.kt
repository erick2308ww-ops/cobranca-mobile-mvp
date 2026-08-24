package com.alves.cestabasica.data.local

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class Converters {
    @TypeConverter
    fun fromEpochDay(epochDay: Long?): LocalDate? =
        epochDay?.let { LocalDate.fromEpochDays(it.toInt()) }

    @TypeConverter
    fun toEpochDay(date: LocalDate?): Long? =
        date?.toEpochDays()?.toLong()
}
