package com.jihun.searchweather.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.jihun.searchweather.data.*
import com.jihun.searchweather.ui.detail.DetailActivity
import com.jihun.searchweather.ui.main.MainActivity

object LandingRouter {
    fun move(context: Context, event: RouterEvent) {
        try {
            when (event.type) {
                Landing.MAIN -> gotoMain(context, event)
                Landing.DETAIL -> gotoDetail(context, event)
            }
        } catch (e: Exception) {
            Log.e("####", "${event.type} -> ${e.localizedMessage}")
        }
    }

    private fun gotoMain(context: Context, event: RouterEvent) {
        context.startActivity(Intent(context, MainActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    private fun gotoDetail(context: Context, event: RouterEvent) {
        context.startActivity(Intent(context, DetailActivity::class.java).apply {
            putExtra(LAT, event.lat)
            putExtra(LONG, event.long)
        })
    }
}