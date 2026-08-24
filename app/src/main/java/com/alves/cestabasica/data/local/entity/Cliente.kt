package com.alves.cestabasica.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nome: String,
    val telefone: String,
    val endereco: String
)
