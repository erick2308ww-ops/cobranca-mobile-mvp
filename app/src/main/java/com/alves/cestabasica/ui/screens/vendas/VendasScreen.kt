package com.alves.cestabasica.ui.screens.vendas

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.alves.cestabasica.data.local.dao.VendaComDetalhes
import com.alves.cestabasica.data.local.entity.Cesta
import com.alves.cestabasica.data.local.entity.Cliente
import com.alves.cestabasica.ui.common.formatarData
import com.alves.cestabasica.ui.common.formatarMoeda
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime

@Composable
fun VendasScreen(viewModel: VendasViewModel) {
    val vendas by viewModel.vendas.collectAsState()
    val clientes by viewModel.clientes.collectAsState()
    val cestas by viewModel.cestas.collectAsState()

    var mostrarFormulario by remember { mutableStateOf(false) }
    var vendaParaExcluir by remember { mutableStateOf<VendaComDetalhes?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarFormulario = true }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.vendas_nova))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(R.string.vendas_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            if (vendas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.vendas_vazio))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vendas, key = { it.id }) { venda ->
                        VendaItem(venda = venda, onExcluir = { vendaParaExcluir = venda })
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        NovaVendaDialog(
            clientes = clientes,
            cestas = cestas,
            onDismiss = { mostrarFormulario = false },
            onSalvar = { clienteId, cestaId, valorTotal, numeroParcelas, dataPrimeiraParcela ->
                viewModel.registrarVenda(clienteId, cestaId, valorTotal, numeroParcelas, dataPrimeiraParcela)
                mostrarFormulario = false
            }
        )
    }

    vendaParaExcluir?.let { venda ->
        AlertDialog(
            onDismissRequest = { vendaParaExcluir = null },
            title = { Text(stringResource(R.string.acao_confirmar_exclusao)) },
            text = { Text("${venda.clienteNome} - ${venda.cestaNome}") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.excluir(venda.id)
                    vendaParaExcluir = null
                }) { Text(stringResource(R.string.acao_excluir)) }
            },
            dismissButton = {
                TextButton(onClick = { vendaParaExcluir = null }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            }
        )
    }
}

@Composable
private fun VendaItem(venda: VendaComDetalhes, onExcluir: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(venda.clienteNome, style = MaterialTheme.typography.titleMedium)
                    Text(venda.cestaNome, style = MaterialTheme.typography.bodyMedium)
                    Text(venda.dataVenda.formatarData(), style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = onExcluir) {
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.acao_excluir))
                }
            }
            Text(
                "${venda.valorTotal.formatarMoeda()} em ${venda.numeroParcelas}x",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            LinearProgressIndicator(
                progress = { venda.parcelasPagas.toFloat() / venda.numeroParcelas.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
            Text(
                "${venda.parcelasPagas}/${venda.numeroParcelas} parcelas pagas",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun NovaVendaDialog(
    clientes: List<Cliente>,
    cestas: List<Cesta>,
    onDismiss: () -> Unit,
    onSalvar: (clienteId: Long, cestaId: Long, valorTotal: Double, numeroParcelas: Int, dataPrimeiraParcela: LocalDate) -> Unit
) {
    var clienteSelecionado by remember { mutableStateOf<Cliente?>(null) }
    var cestaSelecionada by remember { mutableStateOf<Cesta?>(null) }
    var expandirClientes by remember { mutableStateOf(false) }
    var expandirCestas by remember { mutableStateOf(false) }
    var valorTexto by remember { mutableStateOf("") }
    var numeroParcelasTexto by remember { mutableStateOf("1") }
    var mostrarDatePicker by remember { mutableStateOf(false) }

    val fusoHorario = TimeZone.currentSystemDefault()
    var dataPrimeiraParcela by remember { mutableStateOf(Clock.System.todayIn(fusoHorario)) }

    var erroCliente by remember { mutableStateOf(false) }
    var erroCesta by remember { mutableStateOf(false) }
    var erroValor by remember { mutableStateOf(false) }
    var erroParcelas by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.vendas_nova)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (clientes.isEmpty()) {
                    Text(stringResource(R.string.vendas_cadastre_cliente_primeiro))
                } else {
                    ExposedDropdownMenuBox(expanded = expandirClientes, onExpandedChange = { expandirClientes = it }) {
                        OutlinedTextField(
                            value = clienteSelecionado?.nome ?: stringResource(R.string.vendas_selecione_cliente),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.campo_cliente)) },
                            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                            isError = erroCliente,
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = expandirClientes, onDismissRequest = { expandirClientes = false }) {
                            clientes.forEach { cliente ->
                                DropdownMenuItem(
                                    text = { Text(cliente.nome) },
                                    onClick = {
                                        clienteSelecionado = cliente
                                        expandirClientes = false
                                        erroCliente = false
                                    }
                                )
                            }
                        }
                    }
                }

                if (cestas.isEmpty()) {
                    Text(stringResource(R.string.vendas_cadastre_cesta_primeiro))
                } else {
                    ExposedDropdownMenuBox(expanded = expandirCestas, onExpandedChange = { expandirCestas = it }) {
                        OutlinedTextField(
                            value = cestaSelecionada?.nome ?: stringResource(R.string.vendas_selecione_cesta),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.campo_cesta)) },
                            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                            isError = erroCesta,
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = expandirCestas, onDismissRequest = { expandirCestas = false }) {
                            cestas.forEach { cesta ->
                                DropdownMenuItem(
                                    text = { Text("${cesta.nome} - ${cesta.preco.formatarMoeda()}") },
                                    onClick = {
                                        cestaSelecionada = cesta
                                        valorTexto = cesta.preco.toString()
                                        expandirCestas = false
                                        erroCesta = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = valorTexto,
                    onValueChange = { valorTexto = it; erroValor = false },
                    label = { Text(stringResource(R.string.campo_preco)) },
                    isError = erroValor,
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = numeroParcelasTexto,
                    onValueChange = { numeroParcelasTexto = it; erroParcelas = false },
                    label = { Text(stringResource(R.string.campo_parcelas)) },
                    isError = erroParcelas,
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = dataPrimeiraParcela.formatarData(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.campo_data_primeira_parcela)) },
                    trailingIcon = {
                        IconButton(onClick = { mostrarDatePicker = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val valor = valorTexto.replace(",", ".").toDoubleOrNull()
                val parcelas = numeroParcelasTexto.toIntOrNull()
                when {
                    clienteSelecionado == null -> erroCliente = true
                    cestaSelecionada == null -> erroCesta = true
                    valor == null || valor <= 0.0 -> erroValor = true
                    parcelas == null || parcelas <= 0 -> erroParcelas = true
                    else -> onSalvar(clienteSelecionado!!.id, cestaSelecionada!!.id, valor, parcelas, dataPrimeiraParcela)
                }
            }) { Text(stringResource(R.string.acao_salvar)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.acao_cancelar)) }
        }
    )

    if (mostrarDatePicker) {
        val estadoDatePicker = rememberDatePickerState(
            initialSelectedDateMillis = dataPrimeiraParcela.atStartOfDayMillis()
        )
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoDatePicker.selectedDateMillis?.let { millis ->
                        dataPrimeiraParcela = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC).date
                    }
                    mostrarDatePicker = false
                }) { Text(stringResource(R.string.acao_salvar)) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            }
        ) {
            DatePicker(state = estadoDatePicker)
        }
    }
}

private fun LocalDate.atStartOfDayMillis(): Long =
    this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
