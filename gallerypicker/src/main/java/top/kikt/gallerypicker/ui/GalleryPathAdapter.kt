package top.kikt.gallerypicker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import top.kikt.gallerypicker.entity.PathEntity

class GalleryPathAdapter(context: Context?, objects: MutableList<PathEntity>?) : ArrayAdapter<PathEntity>(context, android.R.layout.select_dialog_item, objects) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)

        val view = LayoutInflater.from(parent?.context).inflate(android.R.layout.select_dialog_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item.title

        return view
    }

    private class ViewHolder() {

    }
}