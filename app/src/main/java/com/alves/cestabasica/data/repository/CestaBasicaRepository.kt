package com.alves.cestabasica.data.repository

import com.alves.cestabasica.data.local.AppDatabase
import com.alves.cestabasica.data.local.dao.ParcelaComDetalhes
import com.alves.cestabasica.data.local.dao.VendaComDetalhes
import com.alves.cestabasica.data.local.entity.Cesta
import com.alves.cestabasica.data.local.entity.Cliente
import com.alves.cestabasica.data.local.entity.Parcela
import com.alves.cestabasica.data.local.entity.Venda
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class CestaBasicaRepository(private val db: AppDatabase) {

    // Clientes
    fun observarClientes(): Flow<List<Cliente>> = db.clienteDao().observarTodos()

    suspend fun buscarCliente(id: Long): Cliente? = db.clienteDao().buscarPorId(id)

    suspend fun salvarCliente(cliente: Cliente): Long = db.clienteDao().salvar(cliente)

    suspend fun atualizarCliente(cliente: Cliente) = db.clienteDao().atualizar(cliente)

    suspend fun excluirCliente(cliente: Cliente) = db.clienteDao().excluir(cliente)

    // Cestas
    fun observarCestas(): Flow<List<Cesta>> = db.cestaDao().observarTodas()

    suspend fun buscarCesta(id: Long): Cesta? = db.cestaDao().buscarPorId(id)

    suspend fun salvarCesta(cesta: Cesta): Long = db.cestaDao().salvar(cesta)

    suspend fun atualizarCesta(cesta: Cesta) = db.cestaDao().atualizar(cesta)

    suspend fun excluirCesta(cesta: Cesta) = db.cestaDao().excluir(cesta)

    // Vendas
    fun observarVendas(): Flow<List<VendaComDetalhes>> = db.vendaDao().observarVendasComDetalhes()

    suspend fun excluirVenda(vendaId: Long) = db.vendaDao().excluirVenda(vendaId)

    suspend fun registrarVenda(
        clienteId: Long,
        cestaId: Long,
        valorTotal: Double,
        numeroParcelas: Int,
        dataPrimeiraParcela: LocalDate
    ): Long {
        val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val venda = Venda(
            clienteId = clienteId,
            cestaId = cestaId,
            dataVenda = hoje,
            valorTotal = valorTotal,
            numeroParcelas = numeroParcelas
        )
        val valorParcela = valorTotal / numeroParcelas
        return db.vendaDao().registrarVendaComParcelas(venda) { vendaId ->
            (1..numeroParcelas).map { numero ->
                Parcela(
                    vendaId = vendaId,
                    numero = numero,
                    valor = valorParcela,
                    dataVencimento = dataPrimeiraParcela.plusMonths(numero - 1)
                )
            }
        }
    }

    // Parcelas / Cobrança
    fun observarParcelas(): Flow<List<ParcelaComDetalhes>> = db.parcelaDao().observarParcelasComDetalhes()

    suspend fun marcarParcelaPaga(parcelaId: Long) {
        val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
        db.parcelaDao().marcarComoPaga(parcelaId, hoje)
    }

    suspend fun desfazerPagamentoParcela(parcelaId: Long) = db.parcelaDao().desfazerPagamento(parcelaId)

    fun observarTotalAReceber(): Flow<Double> = db.parcelaDao().observarTotalAReceber()

    fun observarTotalRecebido(): Flow<Double> = db.parcelaDao().observarTotalRecebido()

    fun observarQtdParcelasAtrasadas(): Flow<Int> {
        val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return db.parcelaDao().observarQtdParcelasAtrasadas(hoje)
    }

    fun observarQtdClientesAtrasados(): Flow<Int> {
        val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return db.parcelaDao().observarQtdClientesAtrasados(hoje)
    }
}

internal fun LocalDate.plusMonths(months: Int): LocalDate {
    val totalMonths = this.monthNumber - 1 + months
    var year = this.year + totalMonths / 12
    var month = totalMonths % 12
    if (month < 0) {
        month += 12
        year -= 1
    }
    val diasNoMes = diasNoMes(year, month + 1)
    val dia = this.dayOfMonth.coerceAtMost(diasNoMes)
    return LocalDate(year, month + 1, dia)
}

private fun diasNoMes(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (isAnoBissexto(year)) 29 else 28
    else -> 30
}

private fun isAnoBissexto(year: Int): Boolean =
    (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
