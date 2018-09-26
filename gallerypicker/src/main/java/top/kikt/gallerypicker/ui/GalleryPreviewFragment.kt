package top.kikt.gallerypicker.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.kikt.gallerypicker.R
import java.nio.channels.spi.SelectorProvider

class GalleryPreviewFragment : Fragment() {

    private var rootView: View? = null

    var selectorProvider: SelectorProvider? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView != null) {
            return rootView
        }
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        this.rootView = view
        return view
    }
}