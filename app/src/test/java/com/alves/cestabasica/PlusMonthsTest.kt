package com.alves.cestabasica

import com.alves.cestabasica.data.repository.plusMonths
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class PlusMonthsTest {

    @Test
    fun `soma meses dentro do mesmo ano`() {
        val data = LocalDate(2026, 1, 15)
        assertEquals(LocalDate(2026, 3, 15), data.plusMonths(2))
    }

    @Test
    fun `soma meses cruzando o ano`() {
        val data = LocalDate(2026, 11, 10)
        assertEquals(LocalDate(2027, 1, 10), data.plusMonths(2))
    }

    @Test
    fun `ajusta dia quando o mes destino tem menos dias`() {
        val data = LocalDate(2026, 1, 31)
        assertEquals(LocalDate(2026, 2, 28), data.plusMonths(1))
    }

    @Test
    fun `ajusta dia 29 de fevereiro em ano bissexto`() {
        val data = LocalDate(2027, 12, 31)
        assertEquals(LocalDate(2028, 2, 29), data.plusMonths(2))
    }

    @Test
    fun `soma zero meses retorna a mesma data`() {
        val data = LocalDate(2026, 5, 20)
        assertEquals(data, data.plusMonths(0))
    }
}
