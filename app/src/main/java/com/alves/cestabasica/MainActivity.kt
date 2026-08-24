package com.alves.cestabasica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alves.cestabasica.ui.navigation.CestaBasicaNavHost
import com.alves.cestabasica.ui.theme.CestaBasicaAlvesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as CestaBasicaApplication).repository

        setContent {
            CestaBasicaAlvesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CestaBasicaNavHost(repository = repository)
                }
            }
        }
    }
}
