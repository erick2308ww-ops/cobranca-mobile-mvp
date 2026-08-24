package com.alves.cestabasica.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.alves.cestabasica.data.local.entity.Parcela
import com.alves.cestabasica.data.local.entity.Venda
import kotlinx.coroutines.flow.Flow

@Dao
interface VendaDao {

    @Insert
    suspend fun inserirVenda(venda: Venda): Long

    @Insert
    suspend fun inserirParcelas(parcelas: List<Parcela>)

    @Transaction
    suspend fun registrarVendaComParcelas(venda: Venda, parcelas: (Long) -> List<Parcela>): Long {
        val vendaId = inserirVenda(venda)
        inserirParcelas(parcelas(vendaId))
        return vendaId
    }

    @Query(
        """
        SELECT v.id AS id,
               v.clienteId AS clienteId,
               c.nome AS clienteNome,
               v.cestaId AS cestaId,
               cs.nome AS cestaNome,
               v.dataVenda AS dataVenda,
               v.valorTotal AS valorTotal,
               v.numeroParcelas AS numeroParcelas,
               (SELECT COUNT(*) FROM parcelas p WHERE p.vendaId = v.id AND p.paga = 1) AS parcelasPagas
        FROM vendas v
        INNER JOIN clientes c ON c.id = v.clienteId
        INNER JOIN cestas cs ON cs.id = v.cestaId
        ORDER BY v.dataVenda DESC, v.id DESC
        """
    )
    fun observarVendasComDetalhes(): Flow<List<VendaComDetalhes>>

    @Query("DELETE FROM vendas WHERE id = :vendaId")
    suspend fun excluirVenda(vendaId: Long)
}
