package com.alves.cestabasica

import android.app.Application
import com.alves.cestabasica.data.local.AppDatabase
import com.alves.cestabasica.data.repository.CestaBasicaRepository

class CestaBasicaApplication : Application() {

    val repository: CestaBasicaRepository by lazy {
        CestaBasicaRepository(AppDatabase.getInstance(this))
    }
}
