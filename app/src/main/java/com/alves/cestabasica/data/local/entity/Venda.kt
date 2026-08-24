package com.alves.cestabasica.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "vendas",
    foreignKeys = [
        ForeignKey(
            entity = Cliente::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Cesta::class,
            parentColumns = ["id"],
            childColumns = ["cestaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("clienteId"), Index("cestaId")]
)
data class Venda(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val clienteId: Long,
    val cestaId: Long,
    val dataVenda: LocalDate,
    val valorTotal: Double,
    val numeroParcelas: Int
)
