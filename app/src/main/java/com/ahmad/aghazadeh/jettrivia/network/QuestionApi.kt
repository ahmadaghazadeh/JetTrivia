package com.ahmad.aghazadeh.jettrivia.network

import com.ahmad.aghazadeh.jettrivia.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET("world.json")
    suspend fun getAllQuestions(): Question

}