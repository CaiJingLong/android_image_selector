package top.kikt.gallerypicker.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import top.kikt.gallerypicker.GalleryOption
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.ImageProvider
import top.kikt.gallerypicker.engine.ImageSelectedProvider
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.entity.PathEntity
import top.kikt.gallerypicker.kotterknife.bindView

class GalleryContentFragment : Fragment(), ImageSelectedProvider, AdapterView.OnItemClickListener, View.OnClickListener {
    private val mTvTitle: TextView by bindView(R.id.tv_title)
    private val mLayoutTitle: LinearLayout by bindView(R.id.layout_title)
    private val mRecyclerImage: RecyclerView by bindView(R.id.recycler_image)
    private val mTvCurrentGalleryName: TextView by bindView(R.id.tv_current_gallery_name)

    private var rootView: View? = null

    companion object {
        val ALL = PathEntity("all", "所有")
    }

    private lateinit var provider: ImageProvider

    val imageDatas = ArrayList<ImageEntity>()
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

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val pathEntityList = provider.getPathEntityList()
        val pathEntity = pathEntityList[position]
        changeTitle(pathEntity)
    }

    override fun addImageEntity(entity: ImageEntity): Boolean {
        if (selectList.count() >= config.maxSelected) {
            return false
        }
        return selectList.add(entity)
    }

    override val selectedList: List<ImageEntity>
        get() = ArrayList(this.selectList)

    override fun removeImageEntity(entity: ImageEntity) {
        selectList.remove(entity)
    }

    override fun containsImageEntity(entity: ImageEntity): Boolean {
        return selectList.contains(entity)
    }

    override fun indexOfImageEntity(entity: ImageEntity): Int {
        return selectList.indexOf(entity)
    }

    override fun onClick(v: View?) {
        ListPopupWindow(activity!!)
                .apply {
                    anchorView = mTvCurrentGalleryName
                    setAdapter(GalleryNameAdapter(provider.getPathEntityList()))
                    setOnItemClickListener(this@GalleryContentFragment)
                }.show()
    }
}