package com.example.everyday_diary.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everyday_diary.R
import com.example.everyday_diary.databinding.ImageItemBinding
import com.example.everyday_diary.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.image_item.view.*

class GalleryImageAdapter : RecyclerView.Adapter<GalleryImageAdapter.ImageHolder>() {
    var overSize: SingleLiveEvent<Any> = SingleLiveEvent()
    var onChanged: SingleLiveEvent<Any> = SingleLiveEvent()
    var selectedItem = SparseBooleanArray(0)
    var selectedImageList = ArrayList<Image>()
    var isFull = false
    var isClosing = false

    data class Image(val uri: String)

    var imageList = ArrayList<Image>()

    fun setImage(imageList: ArrayList<String>) {
        this.imageList.clear()
        imageList.forEach {
            this.imageList.add(Image(it))
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.image_item,
            parent,
            false
        )
    )

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {

        holder.imgView.setOnClickListener {
            if (isClosing)
                return@setOnClickListener
            if (selectedImageList.size >= 13)
                isFull = true
            if (selectedItem.get(position, false)) {
                holder.imgView.alpha = 1F
                holder.count.visibility = View.GONE
                selectedItem.put(position, false)
                selectedImageList.remove(imageList[position])
                isFull = false
            } else {
                if (isFull) {
                    overSize.call()
                } else {
                    holder.imgView.alpha = 0.2F
                    holder.count.visibility = View.VISIBLE
                    selectedItem.put(position, true)
                    selectedImageList.add(imageList[position])
                    holder.count.text =
                        ((selectedImageList.indexOf(imageList[position]) + 1).toString())
                    isFull = false
                }
            }
            loadValue()
            onChanged.call()
        }

        if (selectedItem.get(position, false)) {
            holder.count.visibility = View.VISIBLE
            holder.imgView.alpha = 0.2F
            holder.count.text = ((selectedImageList.indexOf(imageList[position]) + 1).toString())
        } else {
            holder.imgView.alpha = 1F
            holder.count.visibility = View.GONE
        }

        holder.bind(imageList[position])
    }

    inner class ImageHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val count: TextView = binding.textViewNumber
        val imgView: ImageView = binding.imageView
        fun bind(item: Image) {
            itemView.run {
                Glide.with(context).load(item.uri).placeholder(R.drawable.image_default)
                    .override(600, 600).into(image_view)
            }
            binding.item = item
        }
    }

    private fun loadValue() {
        var position: Int
        for (index: Int in 0 until selectedItem.size()) {
            position = selectedItem.keyAt(index)
            notifyItemChanged(position)
        }
    }

    fun clearValue() {
        selectedImageList.clear()
        isFull = false
        var position: Int
        for (index: Int in 0 until selectedItem.size()) {
            position = selectedItem.keyAt(index)
            selectedItem.put(position, false)
            notifyItemChanged(position)
        }
    }

}