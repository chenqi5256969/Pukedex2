package com.revenco.pokedex2.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revenco.pokedex2.model.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class LiveCoroutinesViewModel : ViewModel() {
    inline fun liveOnUICoroutines(crossinline block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Main)
        { block() }
    }
}
