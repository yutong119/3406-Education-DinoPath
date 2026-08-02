package com.example.dinopath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button

@Composable
fun KnowledgeCheckScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedAnswer by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var hasSubmitted by rememberSaveable {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 16.dp,
            end = 20.dp,
            bottom = 40.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            KnowledgeCheckHeader(
                onBack = onBack,
            )
        }

        item {
            QuestionCard(
                selectedAnswer = selectedAnswer,
                hasSubmitted = hasSubmitted,
                onAnswerSelected = { answer ->
                    if (!hasSubmitted) {
                        selectedAnswer = answer
                    }
                },
            )
        }

        item {
            Button(
                onClick = {
                    hasSubmitted = true
                },
                enabled = selectedAnswer != null && !hasSubmitted,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("SUBMIT ANSWER")
            }
        }

        if (hasSubmitted) {
            item {
                AnswerFeedbackCard(
                    selectedAnswer = selectedAnswer.orEmpty(),
                )
            }
        }
    }
}

@Composable
private fun KnowledgeCheckHeader(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Return to exhibition",
                )
            }

            Column {
                Text(
                    text = "Knowledge Check",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "Jurassic Period",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Text(
            text = "Question 1 of 3",
            style = MaterialTheme.typography.labelLarge,
        )

        LinearProgressIndicator(
            progress = { 1f / 3f },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun QuestionCard(
    selectedAnswer: String?,
    hasSubmitted: Boolean,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(
        "Tyrannosaurus rex",
        "Brachiosaurus",
        "Triceratops",
        "Velociraptor",
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Which dinosaur lived during the Jurassic Period?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = !hasSubmitted,
                        ) {
                            onAnswerSelected(option)
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedAnswer == option,
                        enabled = !hasSubmitted,
                        onClick = {
                            onAnswerSelected(option)
                        },
                    )

                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun AnswerFeedbackCard(
    selectedAnswer: String,
    modifier: Modifier = Modifier,
) {
    val correctAnswer = "Brachiosaurus"
    val isCorrect = selectedAnswer == correctAnswer

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = if (isCorrect) {
                        Icons.Outlined.CheckCircle
                    } else {
                        Icons.Outlined.ErrorOutline
                    },
                    contentDescription = null,
                    tint = if (isCorrect) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )

                Text(
                    text = if (isCorrect) {
                        "Correct"
                    } else {
                        "Not quite"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }

            if (!isCorrect) {
                Text(
                    text = "Correct answer: Brachiosaurus",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Text(
                text =
                    "Brachiosaurus lived during the Late Jurassic Period. " +
                            "Tyrannosaurus rex, Triceratops and Velociraptor " +
                            "appeared later during the Cretaceous Period.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}