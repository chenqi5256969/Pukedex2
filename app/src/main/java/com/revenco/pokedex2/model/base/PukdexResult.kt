package com.revenco.pokedex2.model.base

/**
 *  Copyright © 2021/3/27 Hugecore Information Technology (Guangzhou) Co.,Ltd. All rights reserved.
 *  author: chenqi
 *  用来处理加载控件的显示与隐藏，数据的吐出
 */
sealed class PukdexResult<out T : Any>(
    val ShowLoading: Boolean = false,
    val ErrorCode: Int? = 9999, val ErrorMsg: String? = null,
    val successResult: T? = null
) {
    data class Success<out T : Any>(
        private val success: T? = null,
        private val isShowLoading: Boolean = false,
        private val isShowLoadMore: Boolean = false
    ) : PukdexResult<T>(
        ShowLoading = isShowLoading,
        successResult = success
    )

    data class Error(private val errorCodes: Int = 9999, private val errorMsgs: String? = null) :
        PukdexResult<Nothing>(ErrorCode = errorCodes, ErrorMsg = errorMsgs)
}