package top.kikt.gallerypicker.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import top.kikt.gallerypicker.GalleryOption
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.engine.PreviewCurrentImageProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.kotterknife.bindView
import top.kikt.gallerypicker.ui.widget.RadioImageView
import java.io.File
import kotlin.math.roundToInt

class GalleryPreviewThumbAdapter(context: Context,
                                 private val imageSelectedProvider: ImageSelectedProvider,
                                 private val onChangeListener: OnChangeListener,
                                 private val currentProvider: PreviewCurrentImageProvider,
                                 private var galleryOption: GalleryOption? = null,
                                 private var removeThumbFromInit: Boolean = true) : RecyclerView.Adapter<GalleryPreviewThumbAdapter.VH>() {
    val initList = ArrayList(imageSelectedProvider.selectedList)

    private val list: ArrayList<ImageEntity>
        get() {
            if (!removeThumbFromInit) {
                return initList
            }
            return ArrayList(imageSelectedProvider.selectedList)
        }

    private lateinit var selectedDrawable: Drawable


    init {


        galleryOption = galleryOption ?: GalleryOption.config
        galleryOption?.also {
            val selectedBackground = GradientDrawable()
            selectedBackground.setStroke((2 * context.resources.displayMetrics.density).roundToInt(), it.themeColor)
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

        viewHolder.apply {

            mIvPreview.scaleType = ImageView.ScaleType.CENTER_CROP

            Glide.with(viewHolder.itemView)
                    .load(File(entity.path))
                    .thumbnail(0.8f)
                    .into(mIvPreview)

            mIvMask.visibility = if (selected) View.GONE else View.VISIBLE

            itemView.setOnClickListener {
                onChangeListener.onSelectedImage(entity)
            }

            mLayoutPreview.background = if (currentProvider.currentImage() == entity) selectedDrawable else ColorDrawable()
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