package top.kikt.gallerypicker.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.kotterknife.bindView
import java.io.File

class GalleryPreviewFragment : Fragment(), GalleryPreviewThumbAdapter.OnChangeListener {
    private val mTvSure: TextView by bindView(R.id.tv_sure)
    private val mLayoutTopBar: LinearLayout by bindView(R.id.layout_top_bar)
    private val mIvPreview: ImageView by bindView(R.id.iv_preview)
    private val mRecyclerSmallImage: RecyclerView by bindView(R.id.recycler_small_image)
    private val mCheckboxSelected: AppCompatCheckBox by bindView(R.id.checkbox_selected)
    private val mLayoutChecked: LinearLayout by bindView(R.id.layout_checked)
    private val mLayoutBottomBar: FrameLayout by bindView(R.id.layout_bottom_bar)

    private var rootView: View? = null

    private lateinit var galleryPreviewThumbAdapter: GalleryPreviewThumbAdapter

    private lateinit var selectorProvider: ImageSelectedProvider

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView != null) {
            return rootView
        }
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        this.rootView = view
        galleryPreviewThumbAdapter = GalleryPreviewThumbAdapter(selectorProvider, this)
        Glide.with(this)
                .load(File(selectorProvider.selectedList[currentIndex].path))
                .into(mIvPreview)

        mRecyclerSmallImage.layoutManager = LinearLayoutManager(activity)

        mRecyclerSmallImage.adapter = galleryPreviewThumbAdapter

        return view
    }

    override fun getView() = rootView

    fun setSelectorProvider(selectorProvider: ImageSelectedProvider) {
        this.selectorProvider = selectorProvider
    }

    private var currentIndex: Int = 0

    fun setInitIndex(initIndex: Int) {
        this.currentIndex = initIndex
    }

    override fun onSelectedImage(entity: ImageEntity) {
        Glide.with(this)
                .load(entity.path)
                .into(mIvPreview)
    }
}