package com.ahmad.aghazadeh.jettrivia.model

data class QuestionItem(
    val question: String,
    val category: String,
    val answer: String,
    val choices: List<String>
)