package top.kikt.gallerypicker.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

open class RadioImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {


    var radio = 1.0
        set(value) {
            field = value
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = width * radio
        setMeasuredDimension(width, height.toInt())
    }

    private fun measureWidth(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec);
        val specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        } else if (specMode == MeasureSpec.EXACTLY) {
        }
        return specSize
    }


}