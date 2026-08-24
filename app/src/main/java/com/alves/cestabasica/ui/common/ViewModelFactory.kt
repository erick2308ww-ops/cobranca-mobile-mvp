package com.alves.cestabasica.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.alves.cestabasica.data.repository.CestaBasicaRepository

class ViewModelFactory(
    private val repository: CestaBasicaRepository,
    private val creators: Map<Class<out ViewModel>, (CestaBasicaRepository) -> ViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val creator = creators[modelClass]
            ?: creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("ViewModel desconhecido: $modelClass")
        return creator(repository) as T
    }
}
