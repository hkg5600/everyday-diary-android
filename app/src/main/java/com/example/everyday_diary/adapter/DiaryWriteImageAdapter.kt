package com.example.everyday_diary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everyday_diary.R
import com.example.everyday_diary.databinding.WriteImageItemBinding
import com.example.everyday_diary.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.write_image_item.view.*

class DiaryWriteImageAdapter :
    RecyclerView.Adapter<DiaryWriteImageAdapter.DiaryWriteImageHolder>() {

    private val imageList = ArrayList<GalleryImageAdapter.Image>()
    val showGalleryImage: SingleLiveEvent<Any> = SingleLiveEvent()

    fun setImageList(imageList: ArrayList<GalleryImageAdapter.Image>) {
        this.imageList.clear()
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiaryWriteImageHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.write_image_item, parent, false
        )
    )

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: DiaryWriteImageHolder, position: Int) {

        holder.imageVIew.setOnClickListener {
            showGalleryImage.call()
        }

        holder.bind(imageList[position])
    }


    inner class DiaryWriteImageHolder(private val binding: WriteImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageVIew: ImageView = binding.imageView

        fun bind(item: GalleryImageAdapter.Image) {
            binding.item = item
            itemView.run {
                Glide.with(context).load(item.uri).into(image_view)
            }
        }
    }
}