package com.example.creating_hints
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Looper
import android.os.Looper.getMainLooper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.os.postDelayed
import java.util.logging.Handler
import kotlin.text.HexFormat.Builder.*
class Tooltip private constructor(builder: Builder) {

    private val context: Context = builder.anchorView.context
    private val anchorView: View = builder.anchorView
    private val message: String = builder.message
    private val position: TooltipPosition = builder.position
    private val backgroundColor: Int = builder.backgroundColor
    private val textColor: Int = builder.textColor
    private val cornerRadius: Float = builder.cornerRadius
    private val dismissOnClick: Boolean = builder.dismissOnClick

    private var popupWindow: PopupWindow? = null

    fun show() {
        val tooltipView = createTooltipView()
        setupPopupWindow(tooltipView)
        showAtPosition(tooltipView)
        setupAutoDismiss()
    }

    private fun createTooltipView(): View {
        val inflater = LayoutInflater.from(context)
        val tooltipView = inflater.inflate(R.layout.layout_tooltip, null)

        val textView = tooltipView.findViewById<TextView>(R.id.tooltip_text)
        textView.text = message
        textView.setTextColor(textColor)

        val container = tooltipView.findViewById<LinearLayout>(R.id.tooltip_container)
        container.background = createRoundedBackground()

        return tooltipView
    }
    private fun createRoundedBackground(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(backgroundColor)
        shape.cornerRadius = cornerRadius
        return shape
    }

    private fun setupPopupWindow(tooltipView: View) {
        popupWindow = PopupWindow(
            tooltipView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            elevation = 10f
            isOutsideTouchable = true

            if (dismissOnClick) {
                setOnDismissListener {
                    // Очистка ресурсов
                }
            }
        }
    }

    private fun showAtPosition(tooltipView: View) {
        tooltipView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val tooltipWidth = tooltipView.measuredWidth
        val tooltipHeight = tooltipView.measuredHeight

        val anchorLocation = IntArray(2)
        anchorView.getLocationOnScreen(anchorLocation)

        val x = calculateXPosition(anchorLocation[0], tooltipWidth)
        val y = calculateYPosition(anchorLocation[1], tooltipHeight)

        popupWindow?.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y)
    }

    private fun calculateXPosition(anchorX: Int, tooltipWidth: Int): Int {
        return when (position) {
            TooltipPosition.LEFT -> anchorX - tooltipWidth - MARGIN
            TooltipPosition.RIGHT -> anchorX + anchorView.width + MARGIN
            TooltipPosition.TOP, TooltipPosition.BOTTOM ->
                anchorX + (anchorView.width - tooltipWidth) / 2
        }
    }

    private fun calculateYPosition(anchorY: Int, tooltipHeight: Int): Int {
        return when (position) {
            TooltipPosition.TOP -> anchorY - tooltipHeight - MARGIN
            TooltipPosition.BOTTOM -> anchorY + anchorView.height + MARGIN
            TooltipPosition.LEFT, TooltipPosition.RIGHT ->
                anchorY + (anchorView.height - tooltipHeight) / 2
        }
    }

    private fun setupAutoDismiss() {
        android.os.Handler(getMainLooper()).postDelayed({
            popupWindow?.dismiss()
        }, AUTO_DISMISS_DELAY)
    }

    fun dismiss() {
        popupWindow?.dismiss()
    }

    class Builder(val anchorView: View, val message: String) {
        var position: TooltipPosition = TooltipPosition.TOP
        var backgroundColor: Int = Color.BLACK
        var textColor: Int = Color.WHITE
        var cornerRadius: Float = 8f
        var dismissOnClick: Boolean = true

        fun setPosition(position: TooltipPosition): Builder {
            this.position = position
            return this
        }

        fun setBackgroundColor(color: Int): Builder {
            this.backgroundColor = color
            return this
        }

        fun setTextColor(color: Int): Builder {
            this.textColor = color
            return this
        }

        fun setCornerRadius(radius: Float): Builder {
            this.cornerRadius = radius
            return this
        }

        fun setDismissOnClick(dismiss: Boolean): Builder {
            this.dismissOnClick = dismiss
            return this
        }

        fun build(): Tooltip {
            return Tooltip(this)
        }
    }

    companion object {
        private const val MARGIN = 16
        private const val AUTO_DISMISS_DELAY = 3000L
    }
}

enum class TooltipPosition {
    TOP, BOTTOM, LEFT, RIGHT
}