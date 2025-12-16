package com.example.numbergame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NumberGuessGame()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberGuessGame() {
    var targetNumber by remember { mutableStateOf(Random.nextInt(1, 101)) }
    var userInput by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("1부터 100 사이의 숫자를 맞춰보세요!") }
    var attempts by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var minRange by remember { mutableStateOf(1) }
    var maxRange by remember { mutableStateOf(100) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("숫자 맞추기 게임") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎯",
                fontSize = 72.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "범위: $minRange ~ $maxRange",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = if (gameOver) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "시도 횟수: $attempts",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!gameOver) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = { Text("숫자 입력") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val guess = userInput.toIntOrNull()
                        if (guess == null) {
                            message = "올바른 숫자를 입력하세요!"
                        } else if (guess < 1 || guess > 100) {
                            message = "1부터 100 사이의 숫자를 입력하세요!"
                        } else {
                            attempts++
                            when {
                                guess < targetNumber -> {
                                    message = "UP! 더 큰 숫자입니다"
                                    minRange = maxOf(minRange, guess + 1)
                                }
                                guess > targetNumber -> {
                                    message = "DOWN! 더 작은 숫자입니다"
                                    maxRange = minOf(maxRange, guess - 1)
                                }
                                else -> {
                                    message = "🎉 정답입니다! $attempts 번 만에 맞추셨습니다!"
                                    gameOver = true
                                }
                            }
                            userInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("확인")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    targetNumber = Random.nextInt(1, 101)
                    userInput = ""
                    message = "1부터 100 사이의 숫자를 맞춰보세요!"
                    attempts = 0
                    gameOver = false
                    minRange = 1
                    maxRange = 100
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(if (gameOver) "새 게임" else "다시 시작")
            }
        }
    }
}