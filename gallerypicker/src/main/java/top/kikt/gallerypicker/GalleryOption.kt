package top.kikt.gallerypicker

import android.graphics.Color
import android.support.annotation.ColorInt

class GalleryOption {

    var rowCount = 4  // 单行的图片数量

    var maxSelected = 9 //最大选择数量

    var itemRadio = 1f //item的宽高比

    var padding = 1 // item间的间距,单位是像素(px)

    @ColorInt
    var textColor = Color.WHITE  //图片的颜色

    @ColorInt
    var themeColor = Color.parseColor("#333333") // 主题色(头部,底部颜色)

    @ColorInt
    var dividerColor = Color.parseColor("#333333") //分割线的颜色

    @ColorInt
    var disableColor = Color.GRAY   // 当不可继续选择时,checkBox的颜色

    val maxTip: String   // 当选择达到上限时的提示语
        get() = tipBuilder?.buildTip(maxSelected) ?: "已经选择了${maxSelected}张图片"

    interface TipBuilder {

        fun buildTip(maxSelected: Int): String

    }

    var tipBuilder: TipBuilder? = null

    companion object {
        var config = GalleryOption()
    }
}