package top.kikt.gallerypicker.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class RadioFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    var radio = 1f
        set(value) {
            field = value
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = View.MeasureSpec.makeMeasureSpec((size * radio).toInt(), View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightSpec)
    }


}