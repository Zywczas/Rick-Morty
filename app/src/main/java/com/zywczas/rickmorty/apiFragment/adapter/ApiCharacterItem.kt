package com.zywczas.rickmorty.apiFragment.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character

class ApiCharacterItem (
    val character : Character,
    private val glide : RequestManager
) : AbstractItem<ApiCharacterItem.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.character_list_item
    override val type: Int
        get() = R.id.character_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView : View) : FastAdapter.ViewHolder<ApiCharacterItem>(itemView){
        private val name : TextView = itemView.findViewById(R.id.name_textView_item)
        private val poster : ImageView = itemView.findViewById(R.id.poster_imageView_item)

        override fun bindView(item: ApiCharacterItem, payloads: List<Any>) {
            name.text = item.character.name
            item.character.imageUrl?.let { glide.load(it).into(poster) }
        }

        override fun unbindView(item: ApiCharacterItem) {
            name.text = null
            poster.setImageDrawable(null)
        }

    }

}
