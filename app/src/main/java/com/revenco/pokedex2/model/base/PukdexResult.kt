package com.revenco.pokedex2.model.base

/**
 *  Copyright © 2021/3/27 Hugecore Information Technology (Guangzhou) Co.,Ltd. All rights reserved.
 *  author: chenqi
 */
sealed class PukdexResult<out T : Any>(
    val ShowLoading: Boolean = false,
    val ErrorCode: Int = 1111, val ErrorMsg: String = "",
    val successResult: T? = null
) {
    data class Success<out T : Any>(
        val success: T? = null,
        val isShowLoading: Boolean = false,
        val isShowLoadMore: Boolean = false
    ) : PukdexResult<T>(
        ShowLoading = isShowLoading,
        successResult = success
    )

    data class Error(val errorCodes: Int = 9999, val errorMsgs: String = "") :
        PukdexResult<Nothing>(ErrorCode = errorCodes, ErrorMsg = errorMsgs)
}