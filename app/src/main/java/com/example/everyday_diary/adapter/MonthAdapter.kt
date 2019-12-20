package com.example.everyday_diary.adapter

import android.graphics.Color
import android.os.Process
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everyday_diary.R
import com.example.everyday_diary.databinding.MonthItemBinding
import com.example.everyday_diary.network.model.CardImage
import com.example.everyday_diary.utils.BASE_URL
import kotlinx.android.synthetic.main.diary_list_item.view.*
import kotlinx.android.synthetic.main.month_item.view.*

class MonthAdapter : RecyclerView.Adapter<MonthAdapter.MonthHolder>() {

    val monthList = ArrayList<Month>()
    var selectedItem = SparseBooleanArray(0)

    data class Month(
        val month: Int,
        val monthName: String,
        var progress: String,
        var currentProgress: Int,
        val total: Int,
        val color: String,
        var image: CardImage?
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

        if (onMenuClickListener != null) {
            holder.buttonMenu.setOnClickListener { v ->
                onMenuClickListener?.onClick(v, position, holder)
            }
        }

        if (selectedItem.get(position, false))
            holder.cardImage.visibility = View.VISIBLE
         else
            holder.cardImage.visibility = View.GONE

        holder.layout.setBackgroundColor(Color.parseColor(monthList[position].color))
        holder.progress.max = monthList[position].total
        holder.progress.progress = monthList[position].currentProgress
        holder.bind(monthList[position])
    }

    inner class MonthHolder(private val binding: MonthItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val layout: ConstraintLayout = binding.holderLayout
        val progress: ProgressBar = binding.progressDate
        val buttonMenu: ImageButton = binding.buttonMenu
        val cardImage: ImageView = binding.imageViewCard

        fun bind(item: Month) {
            itemView.run {
                item.image?.let {
                    cardImage.visibility = View.VISIBLE
                    Glide.with(context).load(BASE_URL + it.image).into(image_view_card)
                }
            }
            binding.item = item
        }
    }

    var onItemClickListener: OnItemClickListener? = null
    var onMenuClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(
            view: View,
            position: Int,
            holder: MonthHolder
        )
    }
}