package com.example.dinopath.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.dinopath.ui.theme.DinoPathTheme
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.dinopath.domain.model.QuizQuestion


private val jurassicQuestions = listOf(
    QuizQuestion(
        id = 1,
        question = "Which dinosaur lived during the Jurassic Period?",
        options = listOf(
            "Tyrannosaurus rex",
            "Brachiosaurus",
            "Triceratops",
            "Velociraptor",
        ),
        correctAnswer = "Brachiosaurus",
        explanation =
            "Brachiosaurus lived during the Late Jurassic. " +
                    "The other three appeared during the Cretaceous.",
    ),
    QuizQuestion(
        id = 2,
        question = "What was the general Jurassic climate like?",
        options = listOf(
            "Mostly warm and humid",
            "Permanently frozen",
            "Completely dry worldwide",
            "Identical to today's climate",
        ),
        correctAnswer = "Mostly warm and humid",
        explanation =
            "The Jurassic climate was generally warm and humid, " +
                    "supporting extensive forests and large herbivores.",
    ),
    QuizQuestion(
        id = 3,
        question = "Which animal shows features linking dinosaurs and birds?",
        options = listOf(
            "Archaeopteryx",
            "Triceratops",
            "Diplodocus",
            "Mosasaurus",
        ),
        correctAnswer = "Archaeopteryx",
        explanation =
            "Archaeopteryx had feathers and wings as well as " +
                    "several dinosaur-like skeletal features.",
    ),
)

@Composable
fun KnowledgeCheckScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentQuestionIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    var selectedAnswer by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var hasSubmitted by rememberSaveable {
        mutableStateOf(false)
    }

    var score by rememberSaveable {
        mutableIntStateOf(0)
    }

    var isComplete by rememberSaveable {
        mutableStateOf(false)
    }

    if (isComplete) {
        QuizResultScreen(
            score = score,
            totalQuestions = jurassicQuestions.size,
            onReturnToExhibition = onBack,
            modifier = modifier,
        )
        return
    }

    val question = jurassicQuestions[currentQuestionIndex]

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
                questionNumber = currentQuestionIndex + 1,
                totalQuestions = jurassicQuestions.size,
                onBack = onBack,
            )
        }

        item {
            QuestionCard(
                question = question,
                selectedAnswer = selectedAnswer,
                hasSubmitted = hasSubmitted,
                onAnswerSelected = { answer ->
                    if (!hasSubmitted) {
                        selectedAnswer = answer
                    }
                },
            )
        }

        if (!hasSubmitted) {
            item {
                Button(
                    onClick = {
                        hasSubmitted = true

                        if (selectedAnswer == question.correctAnswer) {
                            score += 1
                        }
                    },
                    enabled = selectedAnswer != null,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("SUBMIT ANSWER")
                }
            }
        } else {
            item {
                AnswerFeedbackCard(
                    question = question,
                    selectedAnswer = selectedAnswer.orEmpty(),
                )
            }

            item {
                Button(
                    onClick = {
                        if (
                            currentQuestionIndex <
                            jurassicQuestions.lastIndex
                        ) {
                            currentQuestionIndex += 1
                            selectedAnswer = null
                            hasSubmitted = false
                        } else {
                            isComplete = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (
                            currentQuestionIndex <
                            jurassicQuestions.lastIndex
                        ) {
                            "NEXT QUESTION"
                        } else {
                            "VIEW RESULT"
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun KnowledgeCheckHeader(
    questionNumber: Int,
    totalQuestions: Int,
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
                modifier = Modifier.semantics {
                    contentDescription =
                        "Back to Jurassic Period exhibition"
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = null,
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
            text = "Question $questionNumber of $totalQuestions",
            style = MaterialTheme.typography.labelLarge,
        )

        LinearProgressIndicator(
            progress = {
                questionNumber.toFloat() / totalQuestions.toFloat()
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun QuestionCard(
    question: QuizQuestion,
    selectedAnswer: String?,
    hasSubmitted: Boolean,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                text = question.question,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            question.options.forEach { option ->
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
    question: QuizQuestion,
    selectedAnswer: String,
    modifier: Modifier = Modifier,
) {
    val isCorrect =
        selectedAnswer == question.correctAnswer

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
                    text =
                        "Correct answer: ${question.correctAnswer}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Text(
                text = question.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    onReturnToExhibition: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val stars = when (score) {
        totalQuestions -> 3
        totalQuestions - 1 -> 2
        else -> 1
    }

    val accuracy =
        (score.toFloat() / totalQuestions.toFloat() * 100).toInt()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "Exhibition Complete!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "★".repeat(stars) + "☆".repeat(3 - stars),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "Score: $score / $totalQuestions",
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = "Accuracy: $accuracy%",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Button(
            onClick = onReturnToExhibition,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        ) {
            Text("RETURN TO EXHIBITION")
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun KnowledgeCheckScreenPreview() {
    DinoPathTheme {
        KnowledgeCheckScreen(
            onBack = {},
        )
    }
}