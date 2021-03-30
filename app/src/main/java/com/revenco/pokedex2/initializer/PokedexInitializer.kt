package com.revenco.pokedex2.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer

/**
 * 谷歌推出的app异步启动优化方案，避免在application中初始化太多东西导致app启动太慢
 */
class PokedexInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        //todo 进行初始化操作，比如Log.init(context)
        Log.i("PokedexInitializer===>", "我被初始化了")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}