package com.alves.cestabasica.ui.screens.cestas

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alves.cestabasica.R
import com.alves.cestabasica.data.local.entity.Cesta
import com.alves.cestabasica.ui.common.formatarMoeda

@Composable
fun CestasScreen(viewModel: CestasViewModel) {
    val cestas by viewModel.cestas.collectAsState()

    var cestaEmEdicao by remember { mutableStateOf<Cesta?>(null) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var cestaParaExcluir by remember { mutableStateOf<Cesta?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                cestaEmEdicao = null
                mostrarFormulario = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.cestas_nova))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.cestas_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            if (cestas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.cestas_vazio))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cestas, key = { it.id }) { cesta ->
                        CestaItem(
                            cesta = cesta,
                            onEditar = {
                                cestaEmEdicao = cesta
                                mostrarFormulario = true
                            },
                            onExcluir = { cestaParaExcluir = cesta }
                        )
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        CestaFormDialog(
            cesta = cestaEmEdicao,
            onDismiss = { mostrarFormulario = false },
            onSalvar = { nome, descricao, preco ->
                viewModel.salvar(cestaEmEdicao?.id, nome, descricao, preco)
                mostrarFormulario = false
            }
        )
    }

    cestaParaExcluir?.let { cesta ->
        AlertDialog(
            onDismissRequest = { cestaParaExcluir = null },
            title = { Text(stringResource(R.string.acao_confirmar_exclusao)) },
            text = { Text(cesta.nome) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.excluir(cesta)
                    cestaParaExcluir = null
                }) { Text(stringResource(R.string.acao_excluir)) }
            },
            dismissButton = {
                TextButton(onClick = { cestaParaExcluir = null }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            }
        )
    }
}

@Composable
private fun CestaItem(cesta: Cesta, onEditar: () -> Unit, onExcluir: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cesta.nome, style = MaterialTheme.typography.titleMedium)
                Text(cesta.descricao, style = MaterialTheme.typography.bodyMedium)
                Text(
                    cesta.preco.formatarMoeda(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.cestas_editar))
            }
            IconButton(onClick = onExcluir) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.acao_excluir))
            }
        }
    }
}

@Composable
private fun CestaFormDialog(
    cesta: Cesta?,
    onDismiss: () -> Unit,
    onSalvar: (nome: String, descricao: String, preco: Double) -> Unit
) {
    var nome by remember { mutableStateOf(cesta?.nome ?: "") }
    var descricao by remember { mutableStateOf(cesta?.descricao ?: "") }
    var precoTexto by remember { mutableStateOf(cesta?.preco?.toString() ?: "") }
    var erroNome by remember { mutableStateOf(false) }
    var erroPreco by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(if (cesta == null) R.string.cestas_nova else R.string.cestas_editar)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it; erroNome = false },
                    label = { Text(stringResource(R.string.campo_nome_cesta)) },
                    isError = erroNome,
                    singleLine = true
                )
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text(stringResource(R.string.campo_descricao)) }
                )
                OutlinedTextField(
                    value = precoTexto,
                    onValueChange = { precoTexto = it; erroPreco = false },
                    label = { Text(stringResource(R.string.campo_preco)) },
                    isError = erroPreco,
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                if (erroNome) {
                    Text(
                        stringResource(R.string.campo_obrigatorio),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (erroPreco) {
                    Text(
                        stringResource(R.string.campo_preco_invalido),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val preco = precoTexto.replace(",", ".").toDoubleOrNull()
                when {
                    nome.isBlank() -> erroNome = true
                    preco == null || preco <= 0.0 -> erroPreco = true
                    else -> onSalvar(nome.trim(), descricao.trim(), preco)
                }
            }) { Text(stringResource(R.string.acao_salvar)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.acao_cancelar)) }
        }
    )
}
