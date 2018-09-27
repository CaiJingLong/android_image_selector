package top.kikt.gallerypicker

import android.graphics.Color
import android.support.annotation.ColorInt

class GalleryOption {

    var rowCount = 4

    val maxSelected = 9

    var itemRadio = 1f

    var padding = 1

    @ColorInt
    var textColor = Color.WHITE

    @ColorInt
    var themeColor = Color.parseColor("#333333")

    @ColorInt
    var dividerColor = Color.parseColor("#333333")

    @ColorInt
    var disableColor = Color.GRAY

    var maxTip: String = "已经选择了${maxSelected}张图片"

    companion object {
        var config = GalleryOption()
    }
}