package com.example.everyday_diary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everyday_diary.R
import com.example.everyday_diary.databinding.DiaryDetailImageItemBinding
import com.example.everyday_diary.network.model.Image
import com.example.everyday_diary.utils.BASE_URL
import kotlinx.android.synthetic.main.diary_detail_image_item.view.*

class DiaryDetailImageAdapter : RecyclerView.Adapter<DiaryDetailImageAdapter.DiaryDetailImageHolder>() {

    private val imageList = ArrayList<Image>()

    fun setImageList(imageList: ArrayList<Image>) {
        this.imageList.clear()
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiaryDetailImageHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.diary_detail_image_item, parent, false
        )
    )

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: DiaryDetailImageHolder, position: Int) {
        holder.bind(imageList[position])
    }


    inner class DiaryDetailImageHolder(private val binding: DiaryDetailImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageVIew: ImageView = binding.imageView
        
        fun bind(item: Image) {
            binding.item = item
            itemView.run {
                Glide.with(context).load(BASE_URL + item.image).into(image_view)
            }
        }
    }
}