package com.example.dinopath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dinopath.domain.model.MistakeSummary
import com.example.dinopath.ui.components.EmptyStateCard
import com.example.dinopath.ui.components.ErrorStateCard
import com.example.dinopath.ui.components.LoadingStateCard
import com.example.dinopath.ui.mistakes.MistakeReviewUiState
import com.example.dinopath.ui.mistakes.MistakeReviewViewModel

@Composable
fun MistakeReviewScreen(
    viewModel: MistakeReviewViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MistakeReviewContent(
        uiState = uiState,
        onBack = onBack,
        onMarkMastered = viewModel::markAsMastered,
        modifier = modifier,
    )
}

@Composable
private fun MistakeReviewContent(
    uiState: MistakeReviewUiState,
    onBack: () -> Unit,
    onMarkMastered: (
        chapterId: Int,
        questionId: Int,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 16.dp,
            end = 20.dp,
            bottom = 40.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            MistakeReviewHeader(
                onBack = onBack,
            )
        }

        if (uiState.isLoading) {
            item {
                LoadingStateCard(
                    message = "Loading mistakes…",
                )
            }
        } else {
            uiState.errorMessage?.let { errorMessage ->
                item {
                    ErrorStateCard(
                        message = errorMessage,
                    )
                }
            }

            if (uiState.mistakes.isEmpty()) {
                item {
                    EmptyStateCard(
                        title = "No mistakes to review",
                        message = "Great work! Complete more knowledge checks to keep building your journal.",
                        icon = Icons.Outlined.CheckCircle,
                    )
                }
            } else {
                items(
                    items = uiState.mistakes,
                    key = { mistake ->
                        "${mistake.chapterId}-${mistake.questionId}"
                    },
                ) { mistake ->
                    val mistakeKey =
                        "${mistake.chapterId}-${mistake.questionId}"

                    MistakeCard(
                        mistake = mistake,
                        isProcessing =
                            uiState.processingKey == mistakeKey,
                        onMarkMastered = {
                            onMarkMastered(
                                mistake.chapterId,
                                mistake.questionId,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MistakeReviewHeader(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.semantics {
                contentDescription =
                    "Back to Explorer Journal"
            },
        ) {
            Icon(
                imageVector =
                    Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = null,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "Review Mistakes",
                modifier = Modifier.semantics {
                    heading()
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = "Learn from previous answers",
                style = MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MistakeCard(
    mistake: MistakeSummary,
    isProcessing: Boolean,
    onMarkMastered: () -> Unit,
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Quiz,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text =
                        "Chapter ${mistake.chapterId} · " +
                                "Question ${mistake.questionId}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = mistake.question,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            AnswerSection(
                label = "YOUR ANSWER",
                answer = mistake.selectedAnswer,
                isCorrectAnswer = false,
            )

            AnswerSection(
                label = "CORRECT ANSWER",
                answer = mistake.correctAnswer,
                isCorrectAnswer = true,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "EXPLANATION",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = mistake.explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Button(
                onClick = onMarkMastered,
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 10.dp),
                        strokeWidth = 2.dp,
                    )

                    Text("UPDATING…")
                } else {
                    Icon(
                        imageVector =
                            Icons.Outlined.CheckCircle,
                        contentDescription = null,
                    )

                    Text(
                        text = "MARK AS MASTERED",
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun AnswerSection(
    label: String,
    answer: String,
    isCorrectAnswer: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = if (isCorrectAnswer) {
                    Icons.Outlined.CheckCircle
                } else {
                    Icons.Outlined.ErrorOutline
                },
                contentDescription = null,
                tint = if (isCorrectAnswer) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrectAnswer) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )

                Text(
                    text = answer.ifBlank {
                        "No answer recorded"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
