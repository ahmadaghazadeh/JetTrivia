package com.ahmad.aghazadeh.jettrivia.repository

import android.util.Log
import com.ahmad.aghazadeh.jettrivia.data.DataOrException
import com.ahmad.aghazadeh.jettrivia.model.QuestionItem
import com.ahmad.aghazadeh.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi){
    private val dataOrException =
        DataOrException<ArrayList<QuestionItem>,
                Boolean,
                Exception>()
    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>,Boolean,Exception>{
        try{
            dataOrException.loading = true
            dataOrException.data=api.getAllQuestions()
            if(dataOrException.data.toString().isNotEmpty()){
                dataOrException.loading = false
                dataOrException.data = dataOrException.data
            }
        }catch(e: Exception){
            dataOrException.exception = e
            Log.d("Exception","{e.message}")
        }
        return dataOrException
    }
}