package com.alves.cestabasica.ui.screens.clientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alves.cestabasica.R
import com.alves.cestabasica.data.local.entity.Cliente

@Composable
fun ClientesScreen(viewModel: ClientesViewModel) {
    val clientes by viewModel.clientes.collectAsState()

    var clienteEmEdicao by remember { mutableStateOf<Cliente?>(null) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var clienteParaExcluir by remember { mutableStateOf<Cliente?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                clienteEmEdicao = null
                mostrarFormulario = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.clientes_novo))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.clientes_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            if (clientes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.clientes_vazio))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clientes, key = { it.id }) { cliente ->
                        ClienteItem(
                            cliente = cliente,
                            onEditar = {
                                clienteEmEdicao = cliente
                                mostrarFormulario = true
                            },
                            onExcluir = { clienteParaExcluir = cliente }
                        )
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        ClienteFormDialog(
            cliente = clienteEmEdicao,
            onDismiss = { mostrarFormulario = false },
            onSalvar = { nome, telefone, endereco ->
                viewModel.salvar(clienteEmEdicao?.id, nome, telefone, endereco)
                mostrarFormulario = false
            }
        )
    }

    clienteParaExcluir?.let { cliente ->
        AlertDialog(
            onDismissRequest = { clienteParaExcluir = null },
            title = { Text(stringResource(R.string.acao_confirmar_exclusao)) },
            text = { Text(cliente.nome) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.excluir(cliente)
                    clienteParaExcluir = null
                }) { Text(stringResource(R.string.acao_excluir)) }
            },
            dismissButton = {
                TextButton(onClick = { clienteParaExcluir = null }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            }
        )
    }
}

@Composable
private fun ClienteItem(cliente: Cliente, onEditar: () -> Unit, onExcluir: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cliente.nome, style = MaterialTheme.typography.titleMedium)
                Text(cliente.telefone, style = MaterialTheme.typography.bodyMedium)
                Text(cliente.endereco, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.clientes_editar))
            }
            IconButton(onClick = onExcluir) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.acao_excluir))
            }
        }
    }
}

@Composable
private fun ClienteFormDialog(
    cliente: Cliente?,
    onDismiss: () -> Unit,
    onSalvar: (nome: String, telefone: String, endereco: String) -> Unit
) {
    var nome by remember { mutableStateOf(cliente?.nome ?: "") }
    var telefone by remember { mutableStateOf(cliente?.telefone ?: "") }
    var endereco by remember { mutableStateOf(cliente?.endereco ?: "") }
    var erro by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(if (cliente == null) R.string.clientes_novo else R.string.clientes_editar)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it; erro = false },
                    label = { Text(stringResource(R.string.campo_nome)) },
                    isError = erro && nome.isBlank(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    label = { Text(stringResource(R.string.campo_telefone)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = endereco,
                    onValueChange = { endereco = it },
                    label = { Text(stringResource(R.string.campo_endereco)) }
                )
                if (erro && nome.isBlank()) {
                    Text(
                        stringResource(R.string.campo_obrigatorio),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nome.isBlank()) {
                    erro = true
                } else {
                    onSalvar(nome.trim(), telefone.trim(), endereco.trim())
                }
            }) { Text(stringResource(R.string.acao_salvar)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.acao_cancelar)) }
        }
    )
}
