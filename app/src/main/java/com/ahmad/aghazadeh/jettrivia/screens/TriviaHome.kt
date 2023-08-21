package com.ahmad.aghazadeh.jettrivia.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmad.aghazadeh.jettrivia.component.Questions

@Composable
fun TriviaHome(viewModel: QuestionViewModel = viewModel()) {
    Questions(viewModel)
}