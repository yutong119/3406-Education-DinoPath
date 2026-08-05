package com.example.dinopath.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.dinopath.ui.theme.DinoPathTheme
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.example.dinopath.domain.model.QuizQuestion
import androidx.compose.runtime.collectAsState
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import com.example.dinopath.ui.theme.LocalReduceMotion
import com.example.dinopath.ui.knowledge.KnowledgeCheckUiState
import com.example.dinopath.ui.knowledge.KnowledgeCheckViewModel

@Composable
fun KnowledgeCheckScreen(
    viewModel: KnowledgeCheckViewModel,
    onBack: () -> Unit,
    onBackToLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    KnowledgeCheckContent(
        uiState = uiState,
        onAnswerSelected = viewModel::selectAnswer,
        onSubmitAnswer = viewModel::submitAnswer,
        onNextQuestion = viewModel::moveToNextQuestion,
        onRetrySave = viewModel::moveToNextQuestion, // Trigger save again
        onBack = onBack,
        onBackToLobby = onBackToLobby,
        modifier = modifier,
    )
}

@Composable
private fun KnowledgeCheckContent(
    uiState: KnowledgeCheckUiState,
    onAnswerSelected: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onRetrySave: () -> Unit,
    onBack: () -> Unit,
    onBackToLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isComplete) {
        QuizResultScreen(
            score = uiState.score,
            totalQuestions = uiState.totalQuestions,
            earnedStars = uiState.earnedStars,
            savedAccuracy = uiState.savedAccuracy,
            questions = uiState.questions,
            answers = uiState.submittedAnswers,
            onReturnToExhibition = onBack,
            onBackToLobby = onBackToLobby,
            modifier = modifier,
        )
        return
    }

    if (uiState.isSaving || uiState.saveError != null) {
        QuizSavingScreen(
            isSaving = uiState.isSaving,
            error = uiState.saveError,
            onRetry = onRetrySave,
            modifier = modifier,
        )
        return
    }

    val question =
        uiState.currentQuestion ?: return

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
                questionNumber = uiState.questionNumber,
                totalQuestions = uiState.totalQuestions,
                onBack = onBack,
            )
        }

        item {
            QuestionCard(
                question = question,
                selectedAnswer = uiState.selectedAnswer,
                hasSubmitted = uiState.hasSubmitted,
                onAnswerSelected = onAnswerSelected,
            )
        }

        if (!uiState.hasSubmitted) {
            item {
                Button(
                    onClick = onSubmitAnswer,
                    enabled = uiState.selectedAnswer != null,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("SUBMIT ANSWER")
                }
            }
        } else {
            item {
                val reduceMotion = LocalReduceMotion.current
                AnimatedVisibility(
                    visible = true,
                    enter = if (reduceMotion) EnterTransition.None 
                            else expandVertically() + fadeIn(),
                ) {
                    AnswerFeedbackCard(
                        question = question,
                        selectedAnswer =
                            uiState.selectedAnswer.orEmpty(),
                    )
                }
            }

            item {
                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (uiState.isLastQuestion) {
                            "VIEW RESULT"
                        } else {
                            "NEXT QUESTION"
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
                    modifier = Modifier.semantics {
                        heading()
                    },
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
private fun QuizSavingScreen(
    isSaving: Boolean,
    error: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (isSaving) {
            CircularProgressIndicator()
            Text(
                text = "Saving your progress...",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp),
            )
        } else if (error != null) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            Text(
                text = "Failed to save results",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp),
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("RETRY")
            }
        }
    }
}

@Composable
private fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    earnedStars: Int,
    savedAccuracy: Int,
    questions: List<QuizQuestion>,
    answers: Map<Int, String>,
    onReturnToExhibition: () -> Unit,
    onBackToLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showReview by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        item {
            Text(
                text = "Exhibition Complete!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        item {
            val reduceMotion = LocalReduceMotion.current
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { index ->
                    var visible by remember { mutableStateOf(reduceMotion) }
                    LaunchedEffect(Unit) {
                        if (!reduceMotion) {
                            delay(index * 200L)
                            visible = true
                        }
                    }
                    val isEarned = index < earnedStars
                    
                    if (visible) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (isEarned) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outlineVariant
                            },
                            modifier = Modifier.size(48.dp)
                        )
                    } else {
                        Box(modifier = Modifier.size(48.dp))
                    }
                }
            }
        }

        item {
            Text(
                text = "Score: $score / $totalQuestions",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item {
            Text(
                text = "Accuracy: $savedAccuracy%",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            Button(
                onClick = onBackToLobby,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Text("BACK TO LOBBY")
            }
        }

        item {
            OutlinedButton(
                onClick = { showReview = !showReview },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (showReview) "HIDE REVIEW" else "REVIEW ANSWERS")
            }
        }

        if (showReview) {
            items(questions) { question ->
                ReviewQuestionItem(
                    question = question,
                    userAnswer = answers[question.id].orEmpty(),
                )
            }
        }

        item {
            OutlinedButton(
                onClick = onReturnToExhibition,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Text("RETURN TO EXHIBITION")
            }
        }
    }
}

@Composable
private fun ReviewQuestionItem(
    question: QuizQuestion,
    userAnswer: String,
    modifier: Modifier = Modifier,
) {
    val isCorrect = userAnswer == question.correctAnswer

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                    text = if (isCorrect) "Correct" else "Incorrect",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )
            }

            Text(
                text = "Your answer: $userAnswer",
                style = MaterialTheme.typography.bodyMedium,
            )

            if (!isCorrect) {
                Text(
                    text = "Correct answer: ${question.correctAnswer}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Text(
                text = question.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
        KnowledgeCheckContent(
            uiState = KnowledgeCheckUiState(
                questions = listOf(
                    QuizQuestion(
                        id = 1,
                        question =
                            "Which dinosaur lived during the Jurassic Period?",
                        options = listOf(
                            "Tyrannosaurus rex",
                            "Brachiosaurus",
                            "Triceratops",
                            "Velociraptor",
                        ),
                        correctAnswer = "Brachiosaurus",
                        explanation =
                            "Brachiosaurus lived during the Jurassic Period.",
                    ),
                ),
            ),
            onAnswerSelected = {},
            onSubmitAnswer = {},
            onNextQuestion = {},
            onRetrySave = {},
            onBack = {},
            onBackToLobby = {},
        )
    }
}

