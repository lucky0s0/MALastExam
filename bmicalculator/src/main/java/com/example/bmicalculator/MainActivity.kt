package com.example.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BMICalculator()
            }
        }
    }
}

data class BMIResult(
    val bmi: Double,
    val category: String,
    val color: Color,
    val description: String
)

fun calculateBMI(height: Double, weight: Double): BMIResult {
    val bmi = weight / (height / 100).pow(2)

    val (category, color, description) = when {
        bmi < 18.5 -> Triple("저체중", Color(0xFF2196F3), "영양 상태를 개선하세요")
        bmi < 23.0 -> Triple("정상", Color(0xFF4CAF50), "건강한 체중입니다")
        bmi < 25.0 -> Triple("과체중", Color(0xFFFFC107), "체중 관리가 필요합니다")
        bmi < 30.0 -> Triple("비만", Color(0xFFFF9800), "적극적인 체중 감량이 필요합니다")
        else -> Triple("고도비만", Color(0xFFF44336), "전문가 상담을 권장합니다")
    }

    return BMIResult(bmi, category, color, description)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculator() {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<BMIResult?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BMI 계산기") },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "⚖️",
                fontSize = 72.sp
            )

            Text(
                text = "체질량지수 계산",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 키 입력
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("키 (cm)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 몸무게 입력
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("몸무게 (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 계산 버튼
            Button(
                onClick = {
                    val h = height.toDoubleOrNull()
                    val w = weight.toDoubleOrNull()

                    if (h != null && w != null && h > 0 && w > 0) {
                        bmiResult = calculateBMI(h, w)
                    } else {
                        bmiResult = null
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("BMI 계산하기", fontSize = 18.sp)
            }

            // 결과 표시
            if (bmiResult != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = bmiResult!!.color.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "BMI 지수",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = String.format("%.1f", bmiResult!!.bmi),
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = bmiResult!!.color
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = bmiResult!!.category,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = bmiResult!!.color
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = bmiResult!!.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BMI 범위 안내
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "BMI 기준 (WHO 아시아-태평양)",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BMIRangeItem("저체중", "18.5 미만", Color(0xFF2196F3))
                        BMIRangeItem("정상", "18.5 ~ 22.9", Color(0xFF4CAF50))
                        BMIRangeItem("과체중", "23.0 ~ 24.9", Color(0xFFFFC107))
                        BMIRangeItem("비만", "25.0 ~ 29.9", Color(0xFFFF9800))
                        BMIRangeItem("고도비만", "30.0 이상", Color(0xFFF44336))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 초기화 버튼
            OutlinedButton(
                onClick = {
                    height = ""
                    weight = ""
                    bmiResult = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("초기화")
            }
        }
    }
}

@Composable
fun BMIRangeItem(category: String, range: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(16.dp),
                color = color,
                shape = MaterialTheme.shapes.small
            ) {}
            Spacer(modifier = Modifier.width(8.dp))
            Text(category)
        }
        Text(range, fontWeight = FontWeight.Bold)
    }
}