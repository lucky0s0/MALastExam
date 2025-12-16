package com.example.rockpapperscissors

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
                RockPaperScissorsGame()
            }
        }
    }
}

enum class Choice(val emoji: String, val displayName: String) {
    ROCK("✊", "바위"),
    PAPER("✋", "보"),
    SCISSORS("✌️", "가위")
}

enum class Result {
    WIN, LOSE, DRAW
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RockPaperScissorsGame() {
    var playerChoice by remember { mutableStateOf<Choice?>(null) }
    var computerChoice by remember { mutableStateOf<Choice?>(null) }
    var result by remember { mutableStateOf<Result?>(null) }
    var wins by remember { mutableStateOf(0) }
    var losses by remember { mutableStateOf(0) }
    var draws by remember { mutableStateOf(0) }

    fun play(choice: Choice) {
        playerChoice = choice
        computerChoice = Choice.values()[Random.nextInt(3)]

        result = when {
            playerChoice == computerChoice -> {
                draws++
                Result.DRAW
            }
            (playerChoice == Choice.ROCK && computerChoice == Choice.SCISSORS) ||
                    (playerChoice == Choice.PAPER && computerChoice == Choice.ROCK) ||
                    (playerChoice == Choice.SCISSORS && computerChoice == Choice.PAPER) -> {
                wins++
                Result.WIN
            }
            else -> {
                losses++
                Result.LOSE
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("가위바위보 게임") },
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
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // 전적
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("전적", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("승", fontWeight = FontWeight.Bold)
                            Text("$wins", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("무", fontWeight = FontWeight.Bold)
                            Text("$draws", fontSize = 24.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("패", fontWeight = FontWeight.Bold)
                            Text("$losses", fontSize = 24.sp, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            // 결과 표시
            if (playerChoice != null && computerChoice != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("나", fontWeight = FontWeight.Bold)
                            Text(playerChoice!!.emoji, fontSize = 72.sp)
                            Text(playerChoice!!.displayName)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("VS", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("컴퓨터", fontWeight = FontWeight.Bold)
                            Text(computerChoice!!.emoji, fontSize = 72.sp)
                            Text(computerChoice!!.displayName)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = when (result) {
                            Result.WIN -> "🎉 승리!"
                            Result.LOSE -> "😢 패배!"
                            Result.DRAW -> "🤝 무승부!"
                            null -> ""
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (result) {
                            Result.WIN -> MaterialTheme.colorScheme.primary
                            Result.LOSE -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            } else {
                Text(
                    text = "가위, 바위, 보 중 하나를 선택하세요!",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // 선택 버튼들
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Choice.values().forEach { choice ->
                    Button(
                        onClick = { play(choice) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    ) {
                        Text(
                            "${choice.emoji} ${choice.displayName}",
                            fontSize = 24.sp
                        )
                    }
                }

                Button(
                    onClick = {
                        wins = 0
                        losses = 0
                        draws = 0
                        playerChoice = null
                        computerChoice = null
                        result = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("전적 초기화")
                }
            }
        }
    }
}