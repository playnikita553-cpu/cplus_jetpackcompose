package com.example.cplus_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cplus_jetpackcompose.ui.theme.Cplus_jetpackcomposeTheme

data class Questions(
    val QuestionsText: String,
    val options: List<String>,
    var correctAnswer: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cplus_jetpackcomposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigator(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf("main") }
    var finalScore by remember { mutableStateOf(0) }
    when (currentScreen) {
        "main" -> {
            MainScreen(
                onStartClick = { currentScreen = "quiz" },
                modifier = modifier
            )
        }
        "quiz" -> {
            QuizScreen(
                onQuizFinished = { score ->
                    finalScore = score
                    currentScreen = "result"
                },
                modifier = modifier
            )
        }
        "result" -> {
            ResultScreen(
                score = finalScore,
                onRestartClick = { currentScreen = "main" },
                modifier = modifier
            )
        }
    }
}

@Composable
fun MainScreen(onStartClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Тест по C++",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 18.dp)
        )
        Button(onClick = onStartClick) {
            Text(text = "Ответить на вопросы")
        }
    }
}

@Composable
fun QuizScreen(onQuizFinished: (Int) -> Unit, modifier: Modifier = Modifier) {
    val cppQuestions = listOf(
        Questions(
            "Что такое указатель в С++?",
            options = listOf("Переменная для хранения адреса памяти", "Тип данных", "Функция"),
            correctAnswer = "Переменная для хранения адреса памяти"
        ),
        Questions(
            "Какой оператор возвращает адрес переменной?",
            options = listOf("&", "", "->"),
            correctAnswer = "&"
        ),
        Questions(
            "Какой оператор разыменовывает указатель?",
            options = listOf("пустые кавычки", "&", "."),
            correctAnswer = "пустые кавычки"
        ),
        Questions(
            "Какой тип данных хранит целое число?",
            options = listOf("int", "float", "char"),
            correctAnswer = "int"
        ),
        Questions(
            "Какой тип данных хранит один символ?",
            options = listOf("char", "int", "double"),
            correctAnswer = "char"
        ),
        Questions(
            "Как правильно объявить главную функцию?",
            options = listOf("int main()", "void main()", "main()"),
            correctAnswer = "int main()"
        ),
        Questions(
            "Какой объект используется для вывода в консоль?",
            options = listOf("std::cout", "std::cin", "std::endl"),
            correctAnswer = "std::cout"
        ),
        Questions(
            "Какой объект используется для ввода с консоли?",
            options = listOf("std::cin", "std::cout", "std::cerr"),
            correctAnswer = "std::cin"
        ),
        Questions(
            "Какой цикл гарантированно выполнится хотя бы один раз?",
            options = listOf("do-while", "while", "for"),
            correctAnswer = "do-while"
        ),
        Questions(
            "Какой оператор проверяет равенство?",
            options = listOf("==", "=", "!="),
            correctAnswer = "=="
        ),
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    val question = cppQuestions[currentQuestionIndex]
    val shuffledOptions = remember(currentQuestionIndex) {
        question.options.shuffled()
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${question.QuestionsText}",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        shuffledOptions.forEach { optionText ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedAnswer == optionText),
                        onClick = { selectedAnswer = optionText }
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedAnswer == optionText),
                    onClick = { selectedAnswer = optionText }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = optionText, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedAnswer == null) return@Button
                if (selectedAnswer == question.correctAnswer) {
                    score++
                }

                selectedAnswer = null
                if (currentQuestionIndex < cppQuestions.size - 1) {
                    currentQuestionIndex++
                } else {
                    onQuizFinished(score)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = selectedAnswer != null
        ) {
            Text(text = "Подтвердить ответ")
        }
    }
}

@Composable
fun ResultScreen(score: Int, onRestartClick: () -> Unit, modifier: Modifier = Modifier) {
    val level = when (score) {
        in 0..3 -> "Новичок"
        in 4..7 -> "про"
        else -> "MR.ROBOT"
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ваш счет: $score из 10", fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(
            text = "Ваш уровень: $level",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(onClick = onRestartClick) {
            Text(text = "Пройти заново")
        }
    }
}