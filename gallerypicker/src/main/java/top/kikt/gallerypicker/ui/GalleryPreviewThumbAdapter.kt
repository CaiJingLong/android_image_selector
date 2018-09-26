package top.kikt.gallerypicker.ui

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import top.kikt.gallerypicker.GalleryOption
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.kotterknife.bindView
import top.kikt.gallerypicker.ui.widget.RadioImageView
import java.io.File

class GalleryPreviewThumbAdapter(private val imageSelectedProvider: ImageSelectedProvider, private val onChangeListener: OnChangeListener, private var galleryOption: GalleryOption? = null) : RecyclerView.Adapter<GalleryPreviewThumbAdapter.VH>() {


    private val list = ArrayList(imageSelectedProvider.selectedList)

    private lateinit var selectedDrawable: Drawable

    init {
        galleryOption = galleryOption ?: GalleryOption.config
        galleryOption?.also {
            val selectedBackground = GradientDrawable()
            selectedBackground.setStroke(2, it.themeColor)
            selectedDrawable = selectedBackground
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_gallery_preview_thumb, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        val entity = list[position]
        val selected = imageSelectedProvider.containsImageEntity(entity)

        Glide.with(viewHolder.itemView)
                .load(File(entity.path))
                .thumbnail(0.8f)
                .into(viewHolder.mIvPreview)

        viewHolder.apply {
            mIvMask.visibility = if (selected) View.GONE else View.INVISIBLE

            itemView.setOnClickListener {
                onChangeListener.onSelectedImage(entity)
            }

            mLayoutPreview.background = if (selected) selectedDrawable else ColorDrawable()
        }
    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mIvPreview: RadioImageView by bindView(R.id.iv_preview)
        val mLayoutPreview: ViewGroup by bindView(R.id.layout_preview)
        val mIvMask: RadioImageView by bindView(R.id.iv_mask)
    }

    interface OnChangeListener {

        fun onSelectedImage(entity: ImageEntity)

    }
}