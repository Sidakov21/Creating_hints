package com.example.creating_hints

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat

class TooltipManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("tooltip_prefs", Context.MODE_PRIVATE)

    fun showTooltipIfNeeded(tooltipId: String, anchorView: View, message: String) {
        if (!wasTooltipShown(tooltipId)) {
            showTooltip(anchorView, message)
            markTooltipAsShown(tooltipId)
        }
    }

    fun showTooltip(anchorView: View, message: String) {
        Tooltip.Builder(anchorView, message)
            .setPosition(TooltipPosition.BOTTOM)
            .setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setTextColor(Color.WHITE)
            .build()
            .show()
    }

    private fun wasTooltipShown(tooltipId: String): Boolean {
        return sharedPreferences.getBoolean(tooltipId, false)
    }

    private fun markTooltipAsShown(tooltipId: String) {
        sharedPreferences.edit().putBoolean(tooltipId, true).apply()
    }

    fun resetTooltip(tooltipId: String) {
        sharedPreferences.edit().remove(tooltipId).apply()
    }

    fun resetAllTooltips() {
        sharedPreferences.edit().clear().apply()
    }
}
