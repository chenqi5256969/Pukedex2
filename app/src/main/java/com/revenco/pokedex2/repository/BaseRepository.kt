package com.revenco.pokedex2.repository

import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  Copyright © 2021/3/28 Hugecore Information Technology (Guangzhou) Co.,Ltd. All rights reserved.
 *  author: chenqi
 */

open class BaseRepository {

    suspend fun <T> safeHandleResult(
        response: ApiResponse<T>,
        successCallBack: suspend (data: ApiResponse.Success<T>) -> Unit,
        errorCallBack: (message: String) -> Unit
    ) {
        response.suspendOnSuccess {
            successCallBack(this)
        }.onError {
            CoroutineScope(Dispatchers.Main).launch {
                errorCallBack(message())
            }
        }.onException {
            CoroutineScope(Dispatchers.Main).launch {
                errorCallBack(message())
            }
        }

    }
}