package com.alves.cestabasica.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alves.cestabasica.R
import com.alves.cestabasica.ui.common.formatarMoeda

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.dashboard_title),
            style = MaterialTheme.typography.titleLarge
        )

        if (uiState.carregando) {
            CircularProgressIndicator()
            return@Column
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IndicadorCard(
                modifier = Modifier.weight(1f),
                titulo = stringResource(R.string.dashboard_total_receber),
                valor = uiState.totalAReceber.formatarMoeda(),
                icone = Icons.Filled.Savings,
                cor = MaterialTheme.colorScheme.primary
            )
            IndicadorCard(
                modifier = Modifier.weight(1f),
                titulo = stringResource(R.string.dashboard_total_recebido),
                valor = uiState.totalRecebido.formatarMoeda(),
                icone = Icons.Filled.CheckCircle,
                cor = MaterialTheme.colorScheme.secondary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IndicadorCard(
                modifier = Modifier.weight(1f),
                titulo = stringResource(R.string.dashboard_parcelas_atrasadas),
                valor = uiState.parcelasAtrasadas.toString(),
                icone = Icons.Filled.Warning,
                cor = MaterialTheme.colorScheme.error
            )
            IndicadorCard(
                modifier = Modifier.weight(1f),
                titulo = stringResource(R.string.dashboard_clientes_atraso),
                valor = uiState.clientesAtrasados.toString(),
                icone = Icons.Filled.People,
                cor = MaterialTheme.colorScheme.error
            )
        }

        if (uiState.parcelasAtrasadas == 0) {
            Text(
                text = stringResource(R.string.dashboard_sem_atrasos),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun IndicadorCard(
    modifier: Modifier = Modifier,
    titulo: String,
    valor: String,
    icone: androidx.compose.ui.graphics.vector.ImageVector,
    cor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icone, contentDescription = null, tint = cor)
            Text(text = titulo, style = MaterialTheme.typography.bodyMedium)
            Text(text = valor, style = MaterialTheme.typography.titleMedium, color = cor)
        }
    }
}
