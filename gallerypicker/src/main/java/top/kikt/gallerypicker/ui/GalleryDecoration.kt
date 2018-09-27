package top.kikt.gallerypicker.ui

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import top.kikt.gallerypicker.GalleryOption

class GalleryDecoration(config: GalleryOption) : RecyclerView.ItemDecoration() {

    private val padding = config.padding
    private val paddingColor = config.dividerColor
    private val rowCount = config.rowCount

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (!isStartRow(adapterPosition)) {
            outRect.left = padding
        } else {
            outRect.left = 0
        }

        if (!isFirstRow(adapterPosition)) {
            outRect.top = padding
        } else {
            outRect.top = 0
        }
    }

    private fun isStartRow(adapterPosition: Int): Boolean {
        return adapterPosition % rowCount == 0
    }

    private fun isFirstRow(adapterPosition: Int): Boolean {
        return adapterPosition < rowCount
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        c.drawColor(paddingColor)
    }
}