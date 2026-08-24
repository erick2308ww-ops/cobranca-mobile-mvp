package com.alves.cestabasica.ui.screens.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alves.cestabasica.data.local.entity.Cliente
import com.alves.cestabasica.data.repository.CestaBasicaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientesViewModel(private val repository: CestaBasicaRepository) : ViewModel() {

    val clientes: StateFlow<List<Cliente>> = repository.observarClientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun salvar(id: Long?, nome: String, telefone: String, endereco: String) {
        viewModelScope.launch {
            if (id == null) {
                repository.salvarCliente(Cliente(nome = nome, telefone = telefone, endereco = endereco))
            } else {
                repository.atualizarCliente(Cliente(id = id, nome = nome, telefone = telefone, endereco = endereco))
            }
        }
    }

    fun excluir(cliente: Cliente) {
        viewModelScope.launch { repository.excluirCliente(cliente) }
    }
}
