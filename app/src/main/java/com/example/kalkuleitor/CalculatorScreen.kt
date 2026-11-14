package com.example.kalkuleitor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalkuleitor.CalculatorState.Companion.operators
import com.example.kalkuleitor.ui.theme.KalkuleitorTheme

// Раскладка кнопок — это визуальное решение, поэтому остаётся в UI-файле.
private val buttonRows = listOf(
    listOf("sin", "cos", "tan", "C"),
    listOf("√", "x²", "π", "⌫"),
    listOf("7", "8", "9", "÷"),
    listOf("4", "5", "6", "×"),
    listOf("1", "2", "3", "−"),
    listOf("(", ")", ".", "+"),
    listOf("%", "0", "="),
)

/**
 * Визуальная часть калькулятора.
 * Ничего не считает сам — только рисует дисплей и кнопки,
 * а нажатия отдаёт в [CalculatorState.press].
 */
@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    val state = rememberSaveable(saver = CalculatorState.Saver) { CalculatorState() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Дисплей
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = state.smallText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = state.bigText,
                fontSize = bigFontSize(state.bigText),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(12.dp))

        // Клавиатура: между кнопками добавлены отступы, сами кнопки — скруглённые.
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            buttonRows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    row.forEach { label ->
                        CalcButton(
                            label = label,
                            modifier = Modifier.weight(if (label == "=") 2f else 1f),
                            onClick = { state.press(label) }
                        )
                    }
                }
            }
        }
    }
}

/** Размер шрифта крупного дисплея подбирается под длину текста. */
private fun bigFontSize(text: String): TextUnit = when {
    text.length <= 9 -> 54.sp
    text.length <= 13 -> 44.sp
    text.length <= 17 -> 34.sp
    else -> 26.sp
}

/** Кнопка калькулятора с цветом и размером шрифта, зависящими от назначения. */
@Composable
private fun CalcButton(
    label: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val isDigit = label.length == 1 && label[0].isDigit()

    val container: androidx.compose.ui.graphics.Color
    val content: androidx.compose.ui.graphics.Color
    val fontSize: TextUnit

    when {
        label == "=" -> {
            container = scheme.primary
            content = scheme.onPrimary
            fontSize = 28.sp
        }
        isDigit || label == "." -> {
            container = scheme.surfaceVariant
            content = scheme.onSurfaceVariant
            fontSize = 24.sp
        }
        label in operators -> {
            container = scheme.primaryContainer
            content = scheme.onPrimaryContainer
            fontSize = 26.sp
        }
        label == "C" || label == "⌫" -> {
            container = scheme.errorContainer
            content = scheme.onErrorContainer
            fontSize = 18.sp
        }
        else -> {
            container = scheme.secondaryContainer
            content = scheme.onSecondaryContainer
            fontSize = 16.sp
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = label,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

/** Превью экрана калькулятора для Android Studio. */
@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun CalculatorPreview() {
    KalkuleitorTheme {
        CalculatorScreen()
    }
}
