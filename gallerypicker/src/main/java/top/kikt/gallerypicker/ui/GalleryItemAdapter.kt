package top.kikt.gallerypicker.ui

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import top.kikt.gallerypicker.GalleryOption
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.kotterknife.bindView
import top.kikt.gallerypicker.ui.drawable.Selector
import java.io.File

class GalleryItemAdapter(private val list: List<ImageEntity>, private val selectedProvider: ImageSelectedProvider, option: GalleryOption?) : RecyclerView.Adapter<GalleryItemAdapter.VH>() {


    private val themeColor = option?.themeColor ?: Color.parseColor("#333")
    private val textColor = option?.textColor ?: Color.WHITE

    private val selectorDrawable = Selector(themeColor)
    private val radio = option?.itemRadio ?: 1f

    private var selectArray = ArrayList<Int>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): VH {
        val viewHolder = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_photo, viewGroup, false)
        val vh = VH(viewHolder)
        vh.mLayoutRoot.radio = radio
        return vh
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        val entity = list[position]

        viewHolder.mIvImage.layoutParams.height = viewHolder.mIvImage.width
        viewHolder.mIvImage.requestLayout()
        Glide.with(viewHolder.itemView)
                .load(File(entity.path))
                .thumbnail(0.15f)
                .into(viewHolder.mIvImage)

        viewHolder.mTvSelector.background = selectorDrawable.getDrawable()
        val isSelected = selectedProvider.containsImageEntity(entity)
        viewHolder.mTvSelector.isSelected = isSelected

        viewHolder.mTvSelector.text = if (isSelected) (selectedProvider.indexOfImageEntity(entity) + 1).toString() else ""
        viewHolder.mTvSelector.setTextColor(textColor)

        if (isSelected) {
            selectArray.add(position)
        } else {
            selectArray.remove(position)
        }

        viewHolder.mFrameLayoutSelector.setOnClickListener {
            if (isSelected) {
                removeSelectedPosition(position)
            } else {
                if (selectedProvider.addImageEntity(entity)) notifyItemChanged(position)
            }
        }
    }

    private fun removeSelectedPosition(position: Int) {
        val changeList = ArrayList<Int>()

        val entity = list[position]

        changeList.add(position)

        selectArray.forEach {
            val selectedPosition = selectedProvider.indexOfImageEntity(list[it])
            val changedPosition = selectedProvider.indexOfImageEntity(entity)
            if (selectedPosition > changedPosition) {
                changeList.add(it)
            }
        }

        selectedProvider.removeImageEntity(entity)

        changeList.forEach {
            notifyItemChanged(it)
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mIvImage: ImageView by bindView(R.id.iv_image)
        val mTvSelector: TextView by bindView(R.id.tv_selector)
        val mFrameLayoutSelector: FrameLayout by bindView(R.id.frameLayoutSelector)
        val mLayoutRoot: RadioFrameLayout by bindView(R.id.layout_root)
    }

}