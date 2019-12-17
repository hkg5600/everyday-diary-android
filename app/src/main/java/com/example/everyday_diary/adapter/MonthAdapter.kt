package com.example.everyday_diary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.everyday_diary.R
import com.example.everyday_diary.databinding.MonthItemBinding

class MonthAdapter : RecyclerView.Adapter<MonthAdapter.MonthHolder>() {

    private var monthList = ArrayList<Month>()

    data class Month(
        val month: Int,
        val monthName: String,
        val progress: String,
        val currentProgress: Int,
        val total: Int,
        val color: String
    )

    fun setMonthList(monthList: ArrayList<Month>) {
        this.monthList = monthList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MonthHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.month_item, parent, false
        )
    )

    override fun getItemCount() = monthList.size

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {
        holder.layout.setBackgroundColor(Color.parseColor(monthList[position].color))
        holder.bind(monthList[position])
    }

    inner class MonthHolder(private val binding: MonthItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val layout : ConstraintLayout = binding.holderLayout

        fun bind(item: Month) {
            binding.item = item
        }
    }
}