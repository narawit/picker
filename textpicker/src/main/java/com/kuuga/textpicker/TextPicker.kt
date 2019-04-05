package com.kuuga.textpicker

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.NumberPicker
import java.lang.reflect.Field

class TextPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {
    fun setData(displays: ArrayList<String>) {
        if (displays.isEmpty()) {
            minValue = 0
            maxValue = 0
            displayedValues = arrayListOf<String>().toTypedArray()
        } else {
            minValue = 0
            maxValue = displays.size - 1
            displayedValues = displays.toTypedArray()
        }
        invalidate()
    }

    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: View) {
        var numberPickerClass: Class<*>? = null
        try {
            numberPickerClass = Class.forName("android.widget.NumberPicker")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }


        var selectionDivider: Field? = null
        try {
            selectionDivider = numberPickerClass!!.getDeclaredField("mSelectionDivider")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }


        try {
            selectionDivider!!.isAccessible = true
            selectionDivider.set(this, null)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        if (view is EditText) {

            val scaleFactor: Float
            val heightDp: Float
            val widthInches: Float
            val widthDpi: Float
            val heightDpi: Float
            val heightInches: Float
            val diagonalInches: Double
            val heightPixels: Int
            val widthPixels: Int
            val width: Int
            val height: Int

            val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            width = windowManager.defaultDisplay.width
            height = windowManager.defaultDisplay.height
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)

            heightPixels = metrics.heightPixels
            scaleFactor = metrics.density
            widthPixels = metrics.widthPixels
            heightDp = heightPixels / scaleFactor
            widthDpi = metrics.xdpi
            heightDpi = metrics.ydpi
            widthInches = widthPixels / widthDpi
            heightInches = heightPixels / heightDpi
            diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

            val diffHeightDp: Float

            diffHeightDp = if (height >= width) {
                if (height < 801 && diagonalInches < 6.0) {
                    800.0f
                } else {
                    592.0f
                }
            } else {
                if (height < 801 && diagonalInches < 6.0) {
                    560.0f
                } else {
                    360.0f
                }
            }

            view.textSize = 18f / diffHeightDp * heightDp
            view.setTextColor(Color.parseColor("#000000"))
        }
    }

}