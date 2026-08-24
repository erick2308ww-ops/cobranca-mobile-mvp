package com.alves.cestabasica.data.local.dao

import kotlinx.datetime.LocalDate

data class VendaComDetalhes(
    val id: Long,
    val clienteId: Long,
    val clienteNome: String,
    val cestaId: Long,
    val cestaNome: String,
    val dataVenda: LocalDate,
    val valorTotal: Double,
    val numeroParcelas: Int,
    val parcelasPagas: Int
)

data class ParcelaComDetalhes(
    val id: Long,
    val vendaId: Long,
    val numero: Int,
    val numeroParcelas: Int,
    val valor: Double,
    val dataVencimento: LocalDate,
    val paga: Boolean,
    val dataPagamento: LocalDate?,
    val clienteId: Long,
    val clienteNome: String,
    val clienteTelefone: String,
    val cestaNome: String
)
