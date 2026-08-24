package com.alves.cestabasica.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alves.cestabasica.data.repository.CestaBasicaRepository
import com.alves.cestabasica.ui.common.ViewModelFactory
import com.alves.cestabasica.ui.screens.cestas.CestasScreen
import com.alves.cestabasica.ui.screens.cestas.CestasViewModel
import com.alves.cestabasica.ui.screens.clientes.ClientesScreen
import com.alves.cestabasica.ui.screens.clientes.ClientesViewModel
import com.alves.cestabasica.ui.screens.cobranca.CobrancaScreen
import com.alves.cestabasica.ui.screens.cobranca.CobrancaViewModel
import com.alves.cestabasica.ui.screens.dashboard.DashboardScreen
import com.alves.cestabasica.ui.screens.dashboard.DashboardViewModel
import com.alves.cestabasica.ui.screens.vendas.VendasScreen
import com.alves.cestabasica.ui.screens.vendas.VendasViewModel

@Composable
fun CestaBasicaNavHost(repository: CestaBasicaRepository) {
    val navController = rememberNavController()
    val factory = remember(repository) {
        ViewModelFactory(
            repository = repository,
            creators = mapOf(
                DashboardViewModel::class.java to { repo -> DashboardViewModel(repo) },
                ClientesViewModel::class.java to { repo -> ClientesViewModel(repo) },
                CestasViewModel::class.java to { repo -> CestasViewModel(repo) },
                VendasViewModel::class.java to { repo -> VendasViewModel(repo) },
                CobrancaViewModel::class.java to { repo -> CobrancaViewModel(repo) }
            )
        )
    }

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val rotaAtual = backStackEntry?.destination?.route
            NavigationBar {
                itensNavegacaoInferior.forEach { destino ->
                    NavigationBarItem(
                        selected = rotaAtual == destino.rota,
                        onClick = {
                            navController.navigate(destino.rota) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destino.icone, contentDescription = stringResource(destino.labelResId)) },
                        label = { Text(stringResource(destino.labelResId)) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destino.Dashboard.rota,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destino.Dashboard.rota) {
                DashboardScreen(viewModel(factory = factory))
            }
            composable(Destino.Clientes.rota) {
                ClientesScreen(viewModel(factory = factory))
            }
            composable(Destino.Cestas.rota) {
                CestasScreen(viewModel(factory = factory))
            }
            composable(Destino.Vendas.rota) {
                VendasScreen(viewModel(factory = factory))
            }
            composable(Destino.Cobranca.rota) {
                CobrancaScreen(viewModel(factory = factory))
            }
        }
    }
}
