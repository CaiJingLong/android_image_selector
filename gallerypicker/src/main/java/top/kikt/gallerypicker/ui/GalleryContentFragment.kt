package top.kikt.gallerypicker.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.codingending.popuplayout.PopupLayout
import com.codingending.popuplayout.PopupLayout.POSITION_BOTTOM
import top.kikt.gallerypicker.GalleryOption
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.FragmentStack
import top.kikt.gallerypicker.engine.ImageProvider
import top.kikt.gallerypicker.engine.ImageSelectFinishCallback
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.entity.PathEntity
import top.kikt.gallerypicker.helper.ColorHelper
import top.kikt.gallerypicker.kotterknife.bindView
import top.kikt.gallerypicker.ui.widget.RadioImageView

class GalleryContentFragment : Fragment(), ImageSelectedProvider, GalleryItemAdapter.OnItemClickListener {
    private val mTvPreview: TextView by bindView(R.id.tv_preview)

    private val mLayoutBottomBar: LinearLayout by bindView(R.id.layout_bottom_bar)
    private val mIvBack: RadioImageView by bindView(R.id.iv_back)
    private val mTvSure: TextView by bindView(R.id.tv_sure)
    private val mTvTitle: TextView by bindView(R.id.tv_title)
    private val mLayoutTitle: LinearLayout by bindView(R.id.layout_title)
    private val mRecyclerImage: RecyclerView by bindView(R.id.recycler_image)
    private val mTvCurrentGalleryName: TextView by bindView(R.id.tv_current_gallery_name)

    private var rootView: View? = null

    companion object {
        val ALL = PathEntity("all", "所有")
    }

    private lateinit var provider: ImageProvider

    private val imageDatas = ArrayList<ImageEntity>()
    private val selectList = ArrayList<ImageEntity>()

    private var adapter: GalleryItemAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView != null) {
            return rootView
        }

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        rootView = view

        mLayoutTitle.setBackgroundColor(config.themeColor)
        mLayoutBottomBar.setBackgroundColor(config.themeColor)

        mRecyclerImage.layoutManager = GridLayoutManager(context, config.rowCount)
        mRecyclerImage.addItemDecoration(GalleryDecoration(config))
        val galleryItemAdapter = GalleryItemAdapter(imageDatas, this, config)
        this.adapter = galleryItemAdapter
        galleryItemAdapter.onItemClickListener = this
        mRecyclerImage.adapter = galleryItemAdapter


        mTvCurrentGalleryName.setOnClickListener {
            selectedGallery()
        }

        mTvSure.setTextColor(config.textColor)
        mTvSure.setOnClickListener {
            val act = activity
            if (act is ImageSelectFinishCallback) {
                act.selectedSure(selectList)
            }
        }
        mIvBack.setOnClickListener {
            val act = activity
            if (act is ImageSelectFinishCallback) {
                act.selectedCancel()
            }
        }
        updateCountText()

        mTvPreview.setOnClickListener {
            preview()
        }

        val textColorStateList = ColorHelper.convertColorToColorStateList(GalleryOption.config.textColor)

        mTvCurrentGalleryName.setTextColor(config.textColor)
        mTvTitle.setTextColor(config.textColor)
        mTvPreview.setTextColor(config.textColor)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIvBack.imageTintList = textColorStateList
        }

        return view
    }

    private var config = GalleryOption.config

    override fun getView(): View? {
        return rootView
    }

    fun changeTitle(path: PathEntity) {
        showImageWithPath(path)
        mRecyclerImage.layoutManager?.scrollToPosition(0)
        mTvCurrentGalleryName.text = path.title
    }


    private fun showImageWithPath(path: PathEntity) {
        val imageList = provider.getImageWithPath(path)
        imageDatas.clear()
        imageDatas.addAll(imageList)
        adapter?.notifyDataSetChanged()
    }

    fun setProvider(provider: ImageProvider) {
        this.provider = provider
    }

    private fun updateCountText() {
        mTvSure.text = "确认(${selectList.count()}/${config.maxSelected})"
        mTvPreview.text = "预览(${selectList.count()})"
    }

    override val selectedList: ArrayList<ImageEntity>
        get() = ArrayList(this.selectList)

    override fun addImageEntity(entity: ImageEntity): Boolean {
        if (selectList.contains(entity)) {
            return false
        }
        if (selectList.count() >= config.maxSelected) {
            toast(config.maxTip)
            return false
        }
        val result = selectList.add(entity)
        updateCountText()
        return result
    }

    override fun removeImageEntity(entity: ImageEntity) {
        selectList.remove(entity)
        updateCountText()
    }

    override fun containsImageEntity(entity: ImageEntity): Boolean {
        return selectList.contains(entity)
    }

    override fun indexOfImageEntity(entity: ImageEntity): Int {
        return selectList.indexOf(entity)
    }


    override fun sortForInitList(initList: ArrayList<ImageEntity>) {
        //当前选中的图库中如果还存在,则按照初始化时的顺序重新排序

        val currentList = ArrayList(selectList)

        selectList.clear()

        initList.forEach {
            if (currentList.contains(it)) {
                selectList.add(it)
            }
        }

        notifyUpdate()
    }


    override fun notifyUpdate() {
        adapter?.notifyDataSetChanged()
    }

    private fun selectedGallery() {
        val pathList = provider.getPathEntityList()
        pathList.sortWith(Comparator { o1, o2 ->
            if (o1 == ALL) {
                return@Comparator -1
            }
            if (o2 == ALL) {
                return@Comparator 1
            }
            if (o1.title == "Camera") {
                return@Comparator -1
            }
            if (o2.title == "Camera") {
                return@Comparator 1
            }
            if (o1.title == "Screenshots") {
                return@Comparator -1
            }
            if (o2.title == "Screenshots") {
                return@Comparator 1
            }
            o1.title.compareTo(o2.title)
        })
        val adapter = GalleryPathAdapter(activity, pathList)
        val listView = ListView(context)
        listView.adapter = adapter
        val popupLayout = PopupLayout.init(context, listView)
        popupLayout.setHeight(500, true)
        popupLayout.show(POSITION_BOTTOM)
        listView.setOnItemClickListener { _, _, position, _ ->
            val pathEntity = pathList[position]
            changeTitle(pathEntity)
            popupLayout.dismiss()
        }
    }

    private fun toast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(entity: ImageEntity) {
        preview(entity)
    }

    /**
     * 预览当前图库文件夹
     */
    private fun preview(entity: ImageEntity) {
        val previewFragment = GalleryPreviewFragment.newInstance(this, this.imageDatas, this.imageDatas.indexOf(entity), removeThumbFromInit = true)
        val act = activity
        if (act is FragmentStack) {
            act.pushFragment(previewFragment)
        }
    }

    /**
     * 仅预览选中图片
     */
    private fun preview() {
        val previewFragment = GalleryPreviewFragment.newInstance(this, ArrayList(this.selectList), removeThumbFromInit = false)
        val act = activity
        if (act is FragmentStack) {
            act.pushFragment(previewFragment)
        }
    }

}