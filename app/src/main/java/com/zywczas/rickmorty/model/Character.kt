package com.zywczas.rickmorty.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.zywczas.rickmorty.R

class Character (
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val characterType: String,
    val gender: String,
    val origin: String,
    val location: String,
    val imageUrl: String?,
    val created: String,
    private val glide : RequestManager
) : AbstractItem<Character.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.character_list_item
    override val type: Int
        get() = R.id.character_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView : View) : FastAdapter.ViewHolder<Character>(itemView){
        private val name : TextView = itemView.findViewById(R.id.name_textView_item)
        private val poster : ImageView = itemView.findViewById(R.id.poster_imageView_item)

        override fun bindView(item: Character, payloads: List<Any>) {
            name.text = item.name
            item.imageUrl?.let { glide.load(it).into(poster) }
        }

        override fun unbindView(item: Character) {
            name.text = null
            poster.setImageDrawable(null)
        }

    }

}