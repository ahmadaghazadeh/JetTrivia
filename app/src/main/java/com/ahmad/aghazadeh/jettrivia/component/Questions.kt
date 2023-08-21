package com.ahmad.aghazadeh.jettrivia.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmad.aghazadeh.jettrivia.model.QuestionItem
import com.ahmad.aghazadeh.jettrivia.screens.QuestionViewModel
import com.ahmad.aghazadeh.jettrivia.util.AppColors

@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questions=viewModel.data.value.data?.toMutableList()

    val questionIndex= remember{
        mutableStateOf(0)
    }
    if (viewModel.data.value.loading == true) {
       CircularProgressIndicator()
    }
    else{
        val question=try {
            questions?.get(questionIndex.value)
        }catch (ex:Exception){
            null
        }
        if (questions!=null){
            QuestionDisplay(question=questions[questionIndex.value],
                questionIndex= questionIndex,
                viewModel=viewModel ){
                questionIndex.value=it+1
            }
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel,
    onNextQuestion:(Int)->Unit={},
                    ) {

    val answerState = remember(question){
        mutableStateOf<Int?>(null)
    }

    val choicesState = remember(question){
        question.choices.toMutableList()
    }

    val correctAnswerState = remember(question){
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question){
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }


    Surface(modifier= Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(4.dp),
        color= AppColors.deepPurple900){
        Column(modifier= Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {
            if (questionIndex.value >= 3) {
                Progress(score = questionIndex.value)
            }
            QuestionTracker(questionIndex.value,viewModel.getTotalQuestionCount())
            DottedLine(pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f))
            QuestionTitle(question.question)
            Choices(choicesState, answerState, updateAnswer, correctAnswerState)
            Button(onClick = {
                onNextQuestion(questionIndex.value)
            },
                modifier = Modifier
                    .padding(3.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                shape = RoundedCornerShape(34.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = AppColors.lightBlue500
                )
            ){
                Text(text="Next",modifier=Modifier.padding(4.dp),
                    color= AppColors.white,
                    fontSize =17.sp)
            }

        }
    }
}

@Composable
private fun Choices(
    choicesState: MutableList<String>,
    answerState: MutableState<Int?>,
    updateAnswer: (Int) -> Unit,
    correctAnswerState: MutableState<Boolean?>
) {

    Column(modifier = Modifier.fillMaxHeight(0.55f)) {
        choicesState.forEachIndexed { index, answerText ->
            Row(
                modifier = Modifier
                    .padding(3.dp)
                    .fillMaxWidth()
                    .height(45.dp)
                    .border(
                        width = 4.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                AppColors.indigo800,
                                AppColors.indigo800
                            )
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (answerState.value == index),
                    onClick = {
                        updateAnswer(index)
                    },
                    modifier = Modifier.padding(start = 16.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = if (correctAnswerState.value == true && index == answerState.value) {
                            Color.Green.copy(alpha = 0.2f)
                        } else {
                            Color.Red.copy(alpha = 0.2f)
                        }
                    )
                )
                val annotatedString = buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
                        withStyle(
                            style = SpanStyle(
                                color = if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green.copy(0.9f)
                                } else if(correctAnswerState.value == false && index == answerState.value) {
                                    Color.Red
                                }else{
                                    Color.White
                                },
                                fontSize = 14.sp
                            )
                        ) {

                            append(answerText)
                        }
                    }
                }
                Text(text = annotatedString, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun QuestionTitle(question: String) {
    Text(text = question,modifier = Modifier
        .fillMaxHeight(0.3f)
        .padding(8.dp),color = AppColors.gray100)
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

@Preview
@Composable
fun Progress(score: Int = 50) {
    val progress = remember(score) {
        mutableStateOf(score * 0.05f)
    }

    Row(modifier= Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    AppColors.gray900,
                    AppColors.gray900
                )
            ),
            shape = RoundedCornerShape(25.dp)
        )
        .clip(
            RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 50,
                bottomEndPercent = 50,
                bottomStartPercent = 50
            )
        )
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {
        Button(onClick = { },contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progress.value)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AppColors.pink800,
                            AppColors.purple500
                        )
                    ),
                ), enabled = false, elevation = null,
            colors = buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )) {
                Text(text =(score*10).toString(),
                modifier=Modifier.clip(shape= RoundedCornerShape(23.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.87f)
                    .padding(6.dp),
                    color = AppColors.white,
                    textAlign = TextAlign.Center)
        }

    }
}


