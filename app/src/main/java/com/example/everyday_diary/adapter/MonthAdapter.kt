package com.example.everyday_diary.adapter

import android.graphics.Color
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.everyday_diary.R
import com.example.everyday_diary.databinding.MonthItemBinding

class MonthAdapter : RecyclerView.Adapter<MonthAdapter.MonthHolder>() {

    val monthList = ArrayList<Month>()

    data class Month(
        val month: Int,
        val monthName: String,
        var progress: String,
        var currentProgress: Int,
        val total: Int,
        val color: String
    )

    fun setMonthList(monthList: ArrayList<Month>) {
        this.monthList.clear()
        this.monthList.addAll(monthList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MonthHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.month_item, parent, false
        )
    )

    override fun getItemCount() = monthList.size

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {

        if (onItemClickListener != null) {
            holder.layout.setOnClickListener { v ->
                onItemClickListener?.onClick(v, position, holder)
            }
        }

        holder.layout.setBackgroundColor(Color.parseColor(monthList[position].color))
        holder.progress.max = monthList[position].total
        holder.progress.progress = monthList[position].currentProgress
        holder.bind(monthList[position])
    }

    inner class MonthHolder(private val binding: MonthItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val layout: ConstraintLayout = binding.holderLayout
        val progress: ProgressBar = binding.progressDate

        fun bind(item: Month) {
            binding.item = item
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: MonthHolder
        )
    }
}