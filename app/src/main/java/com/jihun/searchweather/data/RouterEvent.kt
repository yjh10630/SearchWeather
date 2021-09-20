package com.jihun.searchweather.data


const val INTENT_EXTRA_STRING_PARAM = "intent_extra_string_param"

open class RouterEvent (
    val type: Landing,
    var paramString: String? = null
)

enum class Landing {
    MAIN,
    DETAIL
}