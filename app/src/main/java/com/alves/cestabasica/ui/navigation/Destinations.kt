package com.alves.cestabasica.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.alves.cestabasica.R

sealed class Destino(val rota: String, val labelResId: Int, val icone: ImageVector) {
    data object Dashboard : Destino("dashboard", R.string.nav_dashboard, Icons.Filled.Home)
    data object Clientes : Destino("clientes", R.string.nav_clientes, Icons.Filled.Groups)
    data object Cestas : Destino("cestas", R.string.nav_cestas, Icons.Filled.ShoppingBasket)
    data object Vendas : Destino("vendas", R.string.nav_vendas, Icons.Filled.ShoppingCart)
    data object Cobranca : Destino("cobranca", R.string.nav_cobranca, Icons.Filled.AccountBalanceWallet)
}

val itensNavegacaoInferior = listOf(
    Destino.Dashboard,
    Destino.Clientes,
    Destino.Cestas,
    Destino.Vendas,
    Destino.Cobranca
)
