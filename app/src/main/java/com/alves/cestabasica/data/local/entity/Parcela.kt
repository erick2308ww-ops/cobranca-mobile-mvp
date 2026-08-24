package com.alves.cestabasica.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "parcelas",
    foreignKeys = [
        ForeignKey(
            entity = Venda::class,
            parentColumns = ["id"],
            childColumns = ["vendaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("vendaId")]
)
data class Parcela(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val vendaId: Long,
    val numero: Int,
    val valor: Double,
    val dataVencimento: LocalDate,
    val paga: Boolean = false,
    val dataPagamento: LocalDate? = null
)
