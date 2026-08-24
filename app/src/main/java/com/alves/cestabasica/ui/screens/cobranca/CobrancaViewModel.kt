package com.alves.cestabasica.ui.screens.cobranca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alves.cestabasica.data.local.dao.ParcelaComDetalhes
import com.alves.cestabasica.data.repository.CestaBasicaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

enum class FiltroCobranca { TODAS, PENDENTES, ATRASADAS, PAGAS }

data class CobrancaUiState(
    val parcelas: List<ParcelaComDetalhes> = emptyList(),
    val filtro: FiltroCobranca = FiltroCobranca.PENDENTES
)

class CobrancaViewModel(private val repository: CestaBasicaRepository) : ViewModel() {

    private val filtro = MutableStateFlow(FiltroCobranca.PENDENTES)

    val uiState: StateFlow<CobrancaUiState> = combine(
        repository.observarParcelas(),
        filtro
    ) { parcelas, filtroAtual ->
        val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val filtradas = when (filtroAtual) {
            FiltroCobranca.TODAS -> parcelas
            FiltroCobranca.PENDENTES -> parcelas.filter { !it.paga }
            FiltroCobranca.ATRASADAS -> parcelas.filter { !it.paga && it.dataVencimento < hoje }
            FiltroCobranca.PAGAS -> parcelas.filter { it.paga }
        }
        CobrancaUiState(parcelas = filtradas, filtro = filtroAtual)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CobrancaUiState()
    )

    fun selecionarFiltro(novoFiltro: FiltroCobranca) {
        filtro.value = novoFiltro
    }

    fun marcarComoPaga(parcelaId: Long) {
        viewModelScope.launch { repository.marcarParcelaPaga(parcelaId) }
    }

    fun desfazerPagamento(parcelaId: Long) {
        viewModelScope.launch { repository.desfazerPagamentoParcela(parcelaId) }
    }
}
