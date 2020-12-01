package com.zywczas.rickmorty.localPhotosFragment.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.localCharacterListFragment.adapter.LocalCharacterListItem
import com.zywczas.rickmorty.model.Photo

class LocalPhotosItem (
    val photo : Photo
) : AbstractItem<LocalPhotosItem.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.local_photos_list_item
    override val type: Int
        get() = R.id.localPhotosItem

    override fun getViewHolder(v: View): LocalPhotosItem.ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView : View) : FastAdapter.ViewHolder<LocalPhotosItem>(itemView){
        private val image : ImageView = itemView.findViewById(R.id.image_localPhotosItem)
        private val timeStamp : TextView = itemView.findViewById(R.id.timeStamp_localPhotosItem)

        override fun bindView(item: LocalPhotosItem, payloads: List<Any>) {
            TODO("Not yet implemented")
        }

        override fun unbindView(item: LocalPhotosItem) {
            TODO("Not yet implemented")
        }


    }
}