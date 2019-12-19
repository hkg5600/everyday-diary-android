package com.example.everyday_diary.network.model

data class Diary(
    val id: Int,
    val images: ArrayList<Image>,
    val title: String,
    val text: String,
    val created_at: String,
    val month: Int,
    val year: Int,
    val owner: String
)