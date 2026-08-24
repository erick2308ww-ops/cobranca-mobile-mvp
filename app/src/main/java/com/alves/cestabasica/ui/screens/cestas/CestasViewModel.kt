package com.alves.cestabasica.ui.screens.cestas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alves.cestabasica.data.local.entity.Cesta
import com.alves.cestabasica.data.repository.CestaBasicaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CestasViewModel(private val repository: CestaBasicaRepository) : ViewModel() {

    val cestas: StateFlow<List<Cesta>> = repository.observarCestas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun salvar(id: Long?, nome: String, descricao: String, preco: Double) {
        viewModelScope.launch {
            if (id == null) {
                repository.salvarCesta(Cesta(nome = nome, descricao = descricao, preco = preco))
            } else {
                repository.atualizarCesta(Cesta(id = id, nome = nome, descricao = descricao, preco = preco))
            }
        }
    }

    fun excluir(cesta: Cesta) {
        viewModelScope.launch { repository.excluirCesta(cesta) }
    }
}
