package top.kikt.gallerypicker.ui

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
import top.kikt.gallerypicker.engine.ImageProvider
import top.kikt.gallerypicker.engine.ImageSelectFinishCallback
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.entity.PathEntity
import top.kikt.gallerypicker.kotterknife.bindView

class GalleryContentFragment : Fragment(), ImageSelectedProvider, View.OnClickListener {
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

    var adapter: GalleryItemAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView != null) {
            return rootView
        }

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        rootView = view
        mRecyclerImage.layoutManager = GridLayoutManager(context, config.rowCount)
        mRecyclerImage.addItemDecoration(GalleryDecoration(config))
        val galleryItemAdapter = GalleryItemAdapter(imageDatas, this, config)
        this.adapter = galleryItemAdapter
        mRecyclerImage.adapter = galleryItemAdapter

        mTvCurrentGalleryName.setOnClickListener(this)

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
        return view
    }

    private var config = GalleryOption.config

    override fun getView(): View? {
        return rootView
    }

    fun changeTitle(path: PathEntity) {
        if (path == ALL) {
            showAllImage()
        } else {
            showImageWithPath(path)
        }
        mRecyclerImage.scrollTo(0, 0)
    }

    private fun showAllImage() {
        val imageList = provider.getImageWithPath(null)
        imageDatas.clear()
        imageDatas.addAll(imageList)
        adapter?.notifyDataSetChanged()
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
    }

    override val selectedList: ArrayList<ImageEntity>
        get() = ArrayList(this.selectList)

    override fun addImageEntity(entity: ImageEntity): Boolean {
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

    override fun onClick(v: View?) {
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
}