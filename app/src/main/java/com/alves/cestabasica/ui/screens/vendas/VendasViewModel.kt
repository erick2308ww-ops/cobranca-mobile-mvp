package com.alves.cestabasica.ui.screens.vendas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alves.cestabasica.data.local.dao.VendaComDetalhes
import com.alves.cestabasica.data.local.entity.Cesta
import com.alves.cestabasica.data.local.entity.Cliente
import com.alves.cestabasica.data.repository.CestaBasicaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class VendasViewModel(private val repository: CestaBasicaRepository) : ViewModel() {

    val vendas: StateFlow<List<VendaComDetalhes>> = repository.observarVendas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val clientes: StateFlow<List<Cliente>> = repository.observarClientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val cestas: StateFlow<List<Cesta>> = repository.observarCestas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun registrarVenda(
        clienteId: Long,
        cestaId: Long,
        valorTotal: Double,
        numeroParcelas: Int,
        dataPrimeiraParcela: LocalDate
    ) {
        viewModelScope.launch {
            repository.registrarVenda(clienteId, cestaId, valorTotal, numeroParcelas, dataPrimeiraParcela)
        }
    }

    fun excluir(vendaId: Long) {
        viewModelScope.launch { repository.excluirVenda(vendaId) }
    }
}
