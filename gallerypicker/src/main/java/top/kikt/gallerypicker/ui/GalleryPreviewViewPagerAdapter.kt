package top.kikt.gallerypicker.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import top.kikt.gallerypicker.entity.ImageEntity
import java.io.File

class GalleryPreviewViewPagerAdapter(list: List<ImageEntity>, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()

    init {
        list.forEach {
            fragmentList.add(PreviewFragment.newInstance(it.path))
        }

    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.count()
    }

}

class PreviewFragment : Fragment() {

    lateinit var path: String

    companion object {
        fun newInstance(path: String): PreviewFragment {
            val fragment = PreviewFragment()
            fragment.path = path
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val imageView = ImageView(context)
        Glide.with(this)
                .load(File(path))
                .thumbnail(0.3f)
                .into(imageView)
        return imageView
    }

}