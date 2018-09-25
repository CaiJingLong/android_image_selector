package top.kikt.gallerypicker.ui

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.util.Log
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.ImageProvider
import top.kikt.gallerypicker.engine.ImageScanner
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.entity.PathEntity
import java.util.concurrent.Executors

class GalleryActivity : FragmentActivity(), ImageProvider {

    private val scanner = ImageScanner(this)
    private val threadPool = Executors.newFixedThreadPool(5)
    private val fragment = GalleryContentFragment()
    private val imageData = ArrayList<ImageEntity>()
    private val pathMap = HashMap<PathEntity, ArrayList<ImageEntity>>()

    companion object {
        val handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val bt = supportFragmentManager.beginTransaction()
        fragment.setProvider(this)
        bt.add(R.id.fragment_container, fragment, "content")
        bt.commit()

        threadPool.execute {
            val arrayList = scanner.scan()
            Log.i("Scanner", "image count = ${arrayList.count()}")
            imageData.clear()
            imageData.addAll(arrayList)
            filterImage()
            notifyAdapterChanged()
        }

    }

    private fun filterImage() {
        imageData.forEach { imageEntity: ImageEntity ->
            var list = pathMap[imageEntity.pathEntity]
            if (list == null) {
                list = arrayListOf()
                pathMap[imageEntity.pathEntity] = list
            }
            list.add(imageEntity)
        }
    }

    override fun getPathEntityList(): MutableList<PathEntity> {
        val list = pathMap.keys.toMutableList()
        list.add(0, GalleryContentFragment.ALL)
        return list
    }

    private fun notifyAdapterChanged() {
        handler.post {
            fragment.changeTitle(GalleryContentFragment.ALL)
        }
    }

    override fun getImageWithPath(pathEntity: PathEntity?): List<ImageEntity> {
        if (pathEntity == null) {
            return imageData.toList()
        }
        return pathMap[pathEntity]?.toList() ?: arrayListOf()
    }
}