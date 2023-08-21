package com.ahmad.aghazadeh.jettrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmad.aghazadeh.jettrivia.data.DataOrException
import com.ahmad.aghazadeh.jettrivia.model.QuestionItem
import com.ahmad.aghazadeh.jettrivia.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: QuestionRepository): ViewModel() {
    val data: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, Exception>> =
    mutableStateOf(DataOrException(null,true,Exception("")))

    init {
        getAllQuestions()
    }
    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllQuestions()
            if(data.value.data.toString().isNullOrEmpty()){
                data.value.loading = false
            }
        }
    }

}