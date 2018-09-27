package top.kikt.gallerypicker.helper

import android.content.res.ColorStateList
import android.support.annotation.ColorInt

class ColorHelper {

    companion object {

        @JvmStatic
        fun convertColorToColorStateList(@ColorInt normalColor: Int, @ColorInt disableColor: Int = Int.MAX_VALUE): ColorStateList {
            val dc: Int = if (disableColor == Int.MAX_VALUE) {
                normalColor
            } else {
                disableColor
            }

            val states: Array<IntArray> = arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf())
            val colors: IntArray = intArrayOf(
                    dc,
                    normalColor
            )
            return ColorStateList(states, colors)
        }

    }

}