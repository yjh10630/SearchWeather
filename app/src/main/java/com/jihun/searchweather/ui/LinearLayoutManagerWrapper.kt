package com.jihun.searchweather.ui

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * RecyclerView and java.lang.IndexOutOfBoundsException: Inconsistency detected Error 예외처리..
 */
class LinearLayoutManagerWrapper @JvmOverloads constructor (
    context: Context, orientation: Int, reverseLayout: Boolean
): LinearLayoutManager(context, orientation, reverseLayout) {
    override fun supportsPredictiveItemAnimations(): Boolean = false
}