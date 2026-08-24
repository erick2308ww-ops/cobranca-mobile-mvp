package com.alves.cestabasica.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface ParcelaDao {

    @Query(
        """
        SELECT p.id AS id,
               p.vendaId AS vendaId,
               p.numero AS numero,
               v.numeroParcelas AS numeroParcelas,
               p.valor AS valor,
               p.dataVencimento AS dataVencimento,
               p.paga AS paga,
               p.dataPagamento AS dataPagamento,
               c.id AS clienteId,
               c.nome AS clienteNome,
               c.telefone AS clienteTelefone,
               cs.nome AS cestaNome
        FROM parcelas p
        INNER JOIN vendas v ON v.id = p.vendaId
        INNER JOIN clientes c ON c.id = v.clienteId
        INNER JOIN cestas cs ON cs.id = v.cestaId
        ORDER BY p.dataVencimento ASC, p.numero ASC
        """
    )
    fun observarParcelasComDetalhes(): Flow<List<ParcelaComDetalhes>>

    @Query("UPDATE parcelas SET paga = 1, dataPagamento = :dataPagamento WHERE id = :parcelaId")
    suspend fun marcarComoPaga(parcelaId: Long, dataPagamento: LocalDate)

    @Query("UPDATE parcelas SET paga = 0, dataPagamento = NULL WHERE id = :parcelaId")
    suspend fun desfazerPagamento(parcelaId: Long)

    @Query("SELECT COALESCE(SUM(valor), 0.0) FROM parcelas WHERE paga = 0")
    fun observarTotalAReceber(): Flow<Double>

    @Query("SELECT COALESCE(SUM(valor), 0.0) FROM parcelas WHERE paga = 1")
    fun observarTotalRecebido(): Flow<Double>

    @Query("SELECT COUNT(*) FROM parcelas WHERE paga = 0 AND dataVencimento < :hoje")
    fun observarQtdParcelasAtrasadas(hoje: LocalDate): Flow<Int>

    @Query("SELECT COUNT(DISTINCT v.clienteId) FROM parcelas p INNER JOIN vendas v ON v.id = p.vendaId WHERE p.paga = 0 AND p.dataVencimento < :hoje")
    fun observarQtdClientesAtrasados(hoje: LocalDate): Flow<Int>
}
