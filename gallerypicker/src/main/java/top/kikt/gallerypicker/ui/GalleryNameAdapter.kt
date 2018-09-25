package top.kikt.gallerypicker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import top.kikt.gallerypicker.entity.PathEntity

class GalleryNameAdapter(private val pathEntityList: List<PathEntity>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(convertView?.context).inflate(android.R.layout.select_dialog_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.text.text = getItem(position).toString()

        return view
    }

    override fun getItem(position: Int): PathEntity {
        return pathEntityList[position]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int {
        return pathEntityList.count()
    }

    private class ViewHolder(view: View) {

        val text = view.findViewById<TextView>(android.R.id.text1)

    }
}