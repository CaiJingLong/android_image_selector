package top.kikt.gallerypicker.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import top.kikt.gallerypicker.GalleryOption
import top.kikt.gallerypicker.GalleryOption.Companion.config
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.engine.PreviewCurrentImageProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.helper.ColorHelper
import top.kikt.gallerypicker.kotterknife.bindView

class GalleryPreviewFragment : Fragment(), GalleryPreviewThumbAdapter.OnChangeListener, ViewPager.OnPageChangeListener, PreviewCurrentImageProvider {
    private val mCheckboxSelected: ImageView by bindView(R.id.checkbox_selected)
    private val mTvCheck: TextView by bindView(R.id.tv_check)
    private val mTvPreviewTitle: TextView by bindView(R.id.tv_preview_title)
    private val mViewPagerPreview: ViewPager by bindView(R.id.viewPager_preview)
    private val mTvSure: TextView by bindView(R.id.tv_sure)
    private val mLayoutTopBar: LinearLayout by bindView(R.id.layout_top_bar)
    private val mRecyclerSmallImage: RecyclerView by bindView(R.id.recycler_small_image)
    private val mLayoutChecked: LinearLayout by bindView(R.id.layout_checked)
    private val mLayoutBottomBar: FrameLayout by bindView(R.id.layout_bottom_bar)
    private val mIvBack: ImageView by bindView(R.id.iv_back)

    companion object {
        fun newInstance(selectorProvider: ImageSelectedProvider, previewList: List<ImageEntity>, initIndex: Int = 0, removeThumbFromInit: Boolean = false): GalleryPreviewFragment {
            val fragment = GalleryPreviewFragment()
            fragment.setInitIndex(initIndex)
            fragment.setSelectorProvider(selectorProvider)
            fragment.setPreviewList(previewList)
            fragment.removeThumbFromInit = removeThumbFromInit
            return fragment
        }

    }

    private var removeThumbFromInit: Boolean = false

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
        galleryPreviewThumbAdapter = GalleryPreviewThumbAdapter(inflater.context, selectorProvider, this, this, removeThumbFromInit = removeThumbFromInit)

        initViewPager()

        mRecyclerSmallImage.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerSmallImage.adapter = galleryPreviewThumbAdapter

        mLayoutChecked.setOnClickListener {
            mLayoutChecked.isSelected = !mLayoutChecked.isSelected
            onCheckChanged(mCheckboxSelected.isSelected)
        }

        mTvSure.text = "确认(${selectorProvider.selectedList.count()}/${config.maxSelected})"
        updateSelected()

        mIvBack.setOnClickListener {
            activity?.onBackPressed()
        }

        mTvPreviewTitle.setTextColor(config.textColor)

        initCheckBoxColor()

        val textColorStateList = ColorHelper.convertColorToColorStateList(GalleryOption.config.textColor)

        mLayoutTopBar.setBackgroundColor(config.themeColor)
        mLayoutBottomBar.setBackgroundColor(config.themeColor)

        mTvCheck.setTextColor(config.textColor)
        mTvSure.setTextColor(textColorStateList)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIvBack.imageTintList = textColorStateList
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 根据当前选中重新排序

        if (!removeThumbFromInit) {
            selectorProvider.sortForInitList(galleryPreviewThumbAdapter.initList)
        }else{
            selectorProvider.notifyUpdate()
        }
    }

    private fun onCheckChanged(checked: Boolean) {
        val currentItem = mViewPagerPreview.currentItem
        val entity = previewList[currentItem]
        if (checked) {
            if (selectorProvider.addImageEntity(entity)) {
                selectorProvider.notifyUpdate()
            }
        } else {
            selectorProvider.removeImageEntity(entity)
            selectorProvider.notifyUpdate()
        }
        galleryPreviewThumbAdapter.notifyDataSetChanged()

        mTvSure.text = "确认(${selectorProvider.selectedList.count()}/${config.maxSelected})"
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
            mViewPagerPreview.setCurrentItem(i, false)
        } else {
            // nothing
        }

        updateSelected()
    }

    private lateinit var galleryPreviewViewPagerAdapter: GalleryPreviewViewPagerAdapter

    private fun initViewPager() {
        galleryPreviewViewPagerAdapter = GalleryPreviewViewPagerAdapter(previewList, childFragmentManager)
        mViewPagerPreview.adapter = galleryPreviewViewPagerAdapter
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

        val selected = selectorProvider.containsImageEntity(currentImage())

        mCheckboxSelected.isEnabled = !(selected.not() && selectorProvider.selectedList.count() == config.maxSelected)

        mCheckboxSelected.isSelected = selected

        mTvPreviewTitle.text = "${currentItem + 1}/${previewList.count()}"

        galleryPreviewThumbAdapter.notifyDataSetChanged()
    }


    private fun initCheckBoxColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val stateList = ColorHelper.convertColorToColorStateList(GalleryOption.config.textColor, disableColor = GalleryOption.config.disableColor)
            mCheckboxSelected.imageTintList = stateList
        }
    }

    override fun currentImage(): ImageEntity {
        val currentItem = mViewPagerPreview.currentItem
        return previewList[currentItem]
    }
}