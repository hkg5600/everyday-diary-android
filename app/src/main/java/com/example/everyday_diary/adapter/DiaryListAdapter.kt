package com.example.everyday_diary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everyday_diary.R
import com.example.everyday_diary.databinding.DiaryListItemBinding
import com.example.everyday_diary.utils.BASE_URL
import com.example.everyday_diary.utils.DateTimeConverter
import kotlinx.android.synthetic.main.diary_list_item.view.*

class DiaryListAdapter : RecyclerView.Adapter<DiaryListAdapter.DiaryListHolder>() {

    data class Diary(
        val id: Int,
        val image: String,
        val title: String,
        val text: String,
        val day: String,
        val dayOfWeek: String
    )

    val diaryList = ArrayList<Diary>()

    fun setDiaryList(diaryList: ArrayList<com.example.everyday_diary.network.model.Diary>) {
        this.diaryList.clear()
        this.diaryList.addAll(with(diaryList) {
            ArrayList(this.map {
                Diary(it.id, it.images[0].image, it.title, it.text, DateTimeConverter.dateTimeToDay(it.created_at), DateTimeConverter.getWeekOfDate(it.created_at))
            })
        })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiaryListHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.diary_list_item, parent, false
        )
    )

    override fun getItemCount() = diaryList.size

    override fun onBindViewHolder(holder: DiaryListHolder, position: Int) {

        holder.bind(diaryList[position])
    }

    inner class DiaryListHolder(private val binding: DiaryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageHolder: ConstraintLayout = binding.imageHolder
        val textHolder: ConstraintLayout = binding.diaryTextHolder

        fun bind(item: Diary) {
            itemView.run {
                if (item.image.isNotEmpty()) {
                    textHolder.visibility = View.GONE
                    imageHolder.visibility = View.VISIBLE
                    Glide.with(context).load(BASE_URL + item.image).into(image_view)
                }
            }
            binding.item = item
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: DiaryListHolder
        )
    }
}