package top.kikt.gallerypicker.ui.drawable

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

class Selector(val color: Int) {

    fun getDrawable(): Drawable {
        val stateListDrawable = StateListDrawable()

        val selected = GradientDrawable()
        selected.cornerRadius = 5f
        selected.setColor(color)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selected)

        val normal = GradientDrawable()
        normal.cornerRadius = 5f
        normal.setColor(Color.TRANSPARENT)
        normal.setStroke(2, color)
        stateListDrawable.addState(intArrayOf(), normal)

        return stateListDrawable
    }

}