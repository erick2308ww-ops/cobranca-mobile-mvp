package com.alves.cestabasica.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alves.cestabasica.data.repository.CestaBasicaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val totalAReceber: Double = 0.0,
    val totalRecebido: Double = 0.0,
    val parcelasAtrasadas: Int = 0,
    val clientesAtrasados: Int = 0,
    val carregando: Boolean = true
)

class DashboardViewModel(repository: CestaBasicaRepository) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.observarTotalAReceber(),
        repository.observarTotalRecebido(),
        repository.observarQtdParcelasAtrasadas(),
        repository.observarQtdClientesAtrasados()
    ) { totalAReceber, totalRecebido, parcelasAtrasadas, clientesAtrasados ->
        DashboardUiState(
            totalAReceber = totalAReceber,
            totalRecebido = totalRecebido,
            parcelasAtrasadas = parcelasAtrasadas,
            clientesAtrasados = clientesAtrasados,
            carregando = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )
}
