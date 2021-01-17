package com.example.memescrolller.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memescrolller.data.Meme
import com.example.memescrolller.databinding.ItemRowBinding
import io.realm.Realm
import io.realm.kotlin.where

class MemeAdapter (val memeClick: MemeOnClickListener, val realm: Realm): PagingDataAdapter<Meme, MemeAdapter.MemeViewHolder>(MEME_COMPARATOR) {

    inner class MemeViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var imageView: ImageView = binding.imageView
        var shareButton: ImageButton = binding.shareButton
        var downloadButton: ImageButton = binding.downloadButton
        var progressBar: ProgressBar = binding.progressBar
        init {
            shareButton.setOnClickListener{
                memeClick.onShareClick(getItem(bindingAdapterPosition)!!.url)
            }
            downloadButton.setOnClickListener {
                val item = getItem(bindingAdapterPosition)
                if(item != null)
                    memeClick.onDownloadClick(item.title, item.url)

            }
        }

        override fun onClick(p0: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val currentItem = getItem(position)
        val prevItem = if(position > 0)  getItem(position - 1) else null
        if (currentItem != null) {

                Glide.with(holder.itemView)
                    .load(currentItem.url)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView)

            // Change "seen" in realm db to true
            if(prevItem != null) {
                realm.beginTransaction()
                val meme: Meme? = realm.where<Meme>().equalTo("url", prevItem.url).findFirst()
                if (meme != null) {
                    meme.seen = true
                }
                realm.commitTransaction()
            }

        }
    }

    interface MemeOnClickListener{
        fun onShareClick(url: String)
        fun onDownloadClick(title: String, url: String)
    }

    companion object {
        val MEME_COMPARATOR = object : DiffUtil.ItemCallback<Meme>() {
            override fun areItemsTheSame(oldItem: Meme, newItem: Meme) = oldItem.url == newItem.url



            override fun areContentsTheSame(oldItem: Meme, newItem: Meme) = oldItem.url == newItem.url
        }
    }



}

//class MemeAdapter (private val list: ArrayList<Meme>) : RecyclerView.Adapter<MemeAdapter.MemeViewHolder>() {
//
//    class MemeViewHolder (val view:View) : RecyclerView.ViewHolder(view){
//        val imageView: ImageView = view.findViewById(R.id.imageView)
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
//        return MemeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
//        Glide.with(holder.view)
//            .load(list[position].url)
//            .into(holder.imageView)
//    }
//
//    override fun getItemCount() = list.size
//}