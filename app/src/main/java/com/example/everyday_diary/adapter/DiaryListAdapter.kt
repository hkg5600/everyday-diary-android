package com.example.everyday_diary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.everyday_diary.R
import com.example.everyday_diary.databinding.DiaryListItemBinding
import com.example.everyday_diary.network.model.Diary

class DiaryListAdapter : RecyclerView.Adapter<DiaryListAdapter.DiaryListHolder>() {

    val diaryList = ArrayList<Diary>()

    fun setDiaryList(diaryList: ArrayList<Diary>) {
        this.diaryList.clear()
        this.diaryList.addAll(diaryList)
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

        fun bind(item: Diary) {
            binding.item = item
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: DiaryListAdapter.DiaryListHolder
        )
    }
}