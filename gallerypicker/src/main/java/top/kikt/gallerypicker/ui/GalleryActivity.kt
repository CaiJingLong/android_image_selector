package top.kikt.gallerypicker.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import top.kikt.gallerypicker.GalleryOption
import top.kikt.gallerypicker.GalleryPicker
import top.kikt.gallerypicker.R
import top.kikt.gallerypicker.engine.FragmentStack
import top.kikt.gallerypicker.engine.ImageProvider
import top.kikt.gallerypicker.engine.ImageScanner
import top.kikt.gallerypicker.engine.ImageSelectFinishCallback
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.entity.PathEntity
import top.kikt.gallerypicker.ui.widget.RadioFrameLayout
import java.util.*
import java.util.concurrent.Executors

class GalleryActivity : FragmentActivity(), ImageProvider, ImageSelectFinishCallback, FragmentStack {


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
        pushFragment(fragment)

        threadPool.execute {
            val arrayList = scanner.scan()
            Log.i("Scanner", "image count = ${arrayList.count()}")
            imageData.clear()
            imageData.addAll(arrayList)
            filterImage()
            notifyAdapterChanged()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = GalleryOption.config.themeColor
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

    override fun selectedSure(selectList: ArrayList<ImageEntity>) {
        if (selectList.isEmpty()) {
            val text = "当前没有选择图片"
            toast(text)
            return
        }

        val intent = Intent().apply {
            putExtra(GalleryPicker.RESULT_LIST, selectList)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun selectedCancel() {
        onBackPressed()
    }

    override fun onBackPressed() {
        popFragment()
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private val stack = Stack<Fragment>()

    override fun pushFragment(fragment: Fragment) {
        stack.push(fragment)

        val bt = supportFragmentManager.beginTransaction()
        if (fragment is GalleryContentFragment) {
            fragment.setProvider(this)
        }
        bt.add(R.id.fragment_container, fragment, "content")
        bt.commit()
    }

    override fun popFragment() {
        if (stack.count() == 1) {
            setResult(Activity.RESULT_CANCELED)
            super.onBackPressed()
            return
        }
        val fragment = stack.pop()

        val bt = supportFragmentManager.beginTransaction()
        bt.remove(fragment)
        bt.commit()
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        if (name == "RadioFrameLayout") {
            return RadioFrameLayout(this, attrs)
        }
        return super.onCreateView(name, context, attrs)
    }
}