package com.ahmad.aghazadeh.jettrivia.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmad.aghazadeh.jettrivia.screens.QuestionViewModel
import com.ahmad.aghazadeh.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questions=viewModel.data.value.data?.toMutableList()

    if (viewModel.data.value.loading == true) {
        Log.e("TAG","Questions is lodaing")
    }
    else{
        Log.e("TAG","Questions Loading Stoped")
        questions?.forEach{
            Log.e("TAG","${it.question}")
        }
    }
    Log.d("TAG", "Questions: ${questions?.size}")
}

@Preview
@Composable
fun QuestionDisplay() {
    Surface(modifier= Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(4.dp),
        color= AppColors.deepPurple900){
        Column(modifier= Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {
            QuestionTracker()
            DottedLine(pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f))
            QuestionTitle()
        }
    }
}

@Composable
fun QuestionTitle(question: String) {
    Text(text = question,)
}

@Composable
fun DottedLine(pathEffect: PathEffect) {
    androidx.compose.foundation.Canvas(modifier= Modifier
        .fillMaxWidth()
        .height(1.dp)){
        drawLine(color = AppColors.gray400,
            start = Offset(0f,0f),
            end = Offset(size.width,0f),
            pathEffect = pathEffect)
    }
}

@Composable
fun QuestionTracker(counter: Int =10,
                    outOff: Int = 100) {
    Text(text= buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(style= SpanStyle(color= AppColors.gray800,
                fontWeight= FontWeight.Bold,
                fontSize = 27.sp)
            ){
                append("Question $counter/")
            }
            withStyle(style=SpanStyle(color= AppColors.gray400,
                fontWeight= FontWeight.Bold,
                fontSize = 14.sp)){
                append("$outOff")
            }
        }
    },modifier= Modifier.padding(12.dp))
}

