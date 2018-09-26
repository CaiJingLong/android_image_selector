package top.kikt.gallerypicker.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.kotterknife.bindView

class GalleryPreviewFragment : Fragment(), GalleryPreviewThumbAdapter.OnChangeListener, ViewPager.OnPageChangeListener {
    private val mViewPagerPreview: ViewPager by bindView(R.id.viewPager_preview)
    private val mTvSure: TextView by bindView(R.id.tv_sure)
    private val mLayoutTopBar: LinearLayout by bindView(R.id.layout_top_bar)
    private val mRecyclerSmallImage: RecyclerView by bindView(R.id.recycler_small_image)
    private val mCheckboxSelected: AppCompatCheckBox by bindView(R.id.checkbox_selected)
    private val mLayoutChecked: LinearLayout by bindView(R.id.layout_checked)
    private val mLayoutBottomBar: FrameLayout by bindView(R.id.layout_bottom_bar)

    private var rootView: View? = null

    private lateinit var galleryPreviewThumbAdapter: GalleryPreviewThumbAdapter

    private lateinit var selectorProvider: ImageSelectedProvider
    private lateinit var previewList: List<ImageEntity>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView != null) {
            return rootView
        }
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        view.setOnClickListener {
        }
        this.rootView = view
        galleryPreviewThumbAdapter = GalleryPreviewThumbAdapter(selectorProvider, this)

        initViewPager()

        mRecyclerSmallImage.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerSmallImage.adapter = galleryPreviewThumbAdapter

        return view
    }


    companion object {
        fun newInstance(selectorProvider: ImageSelectedProvider, previewList: List<ImageEntity>, initIndex: Int): GalleryPreviewFragment {
            val fragment = GalleryPreviewFragment()
            fragment.setInitIndex(initIndex)
            fragment.setSelectorProvider(selectorProvider)
            fragment.setPreviewList(previewList)
            return fragment
        }

    }

    private fun setPreviewList(previewList: List<ImageEntity>) {
        this.previewList = previewList
    }

    override fun getView() = rootView

    private fun setSelectorProvider(selectorProvider: ImageSelectedProvider) {
        this.selectorProvider = selectorProvider
    }

    private var initIndex: Int = 0

    private fun setInitIndex(initIndex: Int) {
        this.initIndex = initIndex
    }

    override fun onSelectedImage(entity: ImageEntity) {
        val i = previewList.indexOf(entity)
        if (i != -1) {
            // viewPager scroll to i
            mViewPagerPreview.currentItem = i
        } else {
            // nothing
        }

        updateSelected()
    }

    private fun initViewPager() {
        mViewPagerPreview.adapter = GalleryPreviewViewPagerAdapter(previewList, childFragmentManager)
        mViewPagerPreview.addOnPageChangeListener(this)
        mViewPagerPreview.currentItem = initIndex
        onSelectedImage(previewList[initIndex])
    }

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {
        mViewPagerPreview.currentItem = p0

        updateSelected()
    }

    private fun updateSelected() {
        val currentItem = mViewPagerPreview.currentItem

    }
}