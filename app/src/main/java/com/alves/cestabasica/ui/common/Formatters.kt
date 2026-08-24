package com.alves.cestabasica.ui.common

import kotlinx.datetime.LocalDate
import java.util.Locale

fun Double.formatarMoeda(): String =
    String.format(Locale("pt", "BR"), "R$ %,.2f", this)

fun LocalDate.formatarData(): String =
    "%02d/%02d/%04d".format(dayOfMonth, monthNumber, year)
