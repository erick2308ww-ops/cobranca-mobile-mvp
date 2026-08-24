package com.alves.cestabasica.ui.screens.cobranca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alves.cestabasica.R
import com.alves.cestabasica.data.local.dao.ParcelaComDetalhes
import com.alves.cestabasica.ui.common.formatarData
import com.alves.cestabasica.ui.common.formatarMoeda
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
fun CobrancaScreen(viewModel: CobrancaViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.cobranca_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(FiltroCobranca.entries.toList()) { filtro ->
                FilterChip(
                    selected = uiState.filtro == filtro,
                    onClick = { viewModel.selecionarFiltro(filtro) },
                    label = { Text(filtro.labelResId()) }
                )
            }
        }

        if (uiState.parcelas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.cobranca_vazio))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.parcelas, key = { it.id }) { parcela ->
                    ParcelaItem(
                        parcela = parcela,
                        atrasada = !parcela.paga && parcela.dataVencimento < hoje,
                        onMarcarPaga = { viewModel.marcarComoPaga(parcela.id) },
                        onDesfazer = { viewModel.desfazerPagamento(parcela.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FiltroCobranca.labelResId(): String = stringResource(
    when (this) {
        FiltroCobranca.TODAS -> R.string.cobranca_filtro_todas
        FiltroCobranca.PENDENTES -> R.string.cobranca_filtro_pendentes
        FiltroCobranca.ATRASADAS -> R.string.cobranca_filtro_atrasadas
        FiltroCobranca.PAGAS -> R.string.cobranca_filtro_pagas
    }
)

@Composable
private fun ParcelaItem(
    parcela: ParcelaComDetalhes,
    atrasada: Boolean,
    onMarcarPaga: () -> Unit,
    onDesfazer: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(parcela.clienteNome, style = MaterialTheme.typography.titleMedium)
                    Text(parcela.cestaNome, style = MaterialTheme.typography.bodyMedium)
                }
                StatusBadge(paga = parcela.paga, atrasada = atrasada)
            }

            Text(
                stringResource(R.string.cobranca_parcela_numero, parcela.numero, parcela.numeroParcelas),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                parcela.valor.formatarMoeda(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (parcela.paga && parcela.dataPagamento != null) {
                Text(
                    stringResource(R.string.cobranca_paga_em, parcela.dataPagamento.formatarData()),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    stringResource(R.string.cobranca_vencimento, parcela.dataVencimento.formatarData()),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (parcela.paga) {
                    TextButton(onClick = onDesfazer) {
                        Text(stringResource(R.string.cobranca_desfazer_pagamento))
                    }
                } else {
                    TextButton(onClick = onMarcarPaga) {
                        Text(stringResource(R.string.cobranca_marcar_paga))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(paga: Boolean, atrasada: Boolean) {
    val (texto, cor) = when {
        paga -> stringResource(R.string.cobranca_status_paga) to MaterialTheme.colorScheme.primary
        atrasada -> stringResource(R.string.cobranca_status_atrasada) to MaterialTheme.colorScheme.error
        else -> stringResource(R.string.cobranca_status_pendente) to MaterialTheme.colorScheme.secondary
    }
    Surface(color = cor.copy(alpha = 0.15f), shape = MaterialTheme.shapes.small) {
        Text(
            text = texto,
            color = cor,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
