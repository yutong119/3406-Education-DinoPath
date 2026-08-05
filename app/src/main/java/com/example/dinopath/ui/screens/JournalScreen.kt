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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dinopath.domain.model.QuizHistory
import com.example.dinopath.ui.components.EmptyStateCard
import com.example.dinopath.ui.components.LoadingStateCard
import com.example.dinopath.ui.journal.JournalUiState
import com.example.dinopath.ui.journal.JournalViewModel
import java.text.DateFormat
import java.util.Date
import com.example.dinopath.ui.journal.EvolutionMapSection

@Composable
fun JournalScreen(
    viewModel: JournalViewModel,
    onReviewMistakes: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    JournalContent(
        uiState = uiState,
        onReviewMistakes = onReviewMistakes,
        modifier = modifier,
    )
}


@Composable
private fun JournalContent(
    uiState: JournalUiState,
    onReviewMistakes: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 24.dp,
            end = 20.dp,
            bottom = 40.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Text(
                text = "Explorer Journal",
                modifier = Modifier.semantics {
                    heading()
                },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        if (uiState.isLoading) {
            item {
                LoadingStateCard(
                    message = "Loading explorer statistics…",
                )
            }
        } else {
            item {
                StatisticsGrid(
                    uiState = uiState,
                )
            }

            item {
                Button(
                    onClick = onReviewMistakes,
                    enabled = uiState.mistakeCount > 0,
                    modifier = Modifier.fillMaxWidth(),
                    ) {
                    Text(
                        text =
                            "REVIEW MISTAKES " +
                                    "(${uiState.mistakeCount})",
                        fontWeight = FontWeight.Bold,
                        )
                }
            }

            item {
                EvolutionMapSection(
                    chapters = uiState.chapters,
                )
            }

            item {
                RecentActivitySection(
                    activities = uiState.recentActivity,
                )
            }
        }
    }
}

@Composable
private fun StatisticsGrid(
    uiState: JournalUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            JournalStatCard(
                label = "Chapters",
                value =
                    "${uiState.completedChapters}/${uiState.totalChapters}",
                icon = Icons.Outlined.CheckCircle,
                modifier = Modifier.weight(1f),
            )

            JournalStatCard(
                label = "Accuracy",
                value = "${uiState.averageAccuracy}%",
                icon = Icons.Outlined.Percent,
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            JournalStatCard(
                label = "Total Stars",
                value = uiState.totalStars.toString(),
                icon = Icons.Outlined.Star,
                modifier = Modifier.weight(1f),
            )

            JournalStatCard(
                label = "Mistakes",
                value = uiState.mistakeCount.toString(),
                icon = Icons.Outlined.Quiz,
                modifier = Modifier.weight(1f),
            )
        }
    }
}



@Composable
private fun JournalStatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}


@Composable
private fun RecentActivitySection(
    activities: List<QuizHistory>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "RECENT ACTIVITY",
            modifier = Modifier.semantics {
                heading()
            },
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        if (activities.isEmpty()) {
            EmptyStateCard(
                title = "No recent activity",
                message = "Complete a knowledge check to begin your journal.",
                icon = Icons.Outlined.History,
            )
        } else {
            activities.forEach { activity ->
                RecentActivityCard(
                    activity = activity,
                )
            }
        }
    }
}

@Composable
private fun RecentActivityCard(
    activity: QuizHistory,
    modifier: Modifier = Modifier,
) {
    val chapterName = when (activity.chapterId) {
        1 -> "Meet the Dinosaurs"
        2 -> "Triassic Period"
        3 -> "Jurassic Period"
        4 -> "Cretaceous Period"
        5 -> "Habitats and Diets"
        6 -> "Mass Extinction"
        7 -> "Dinosaurs and Modern Birds"
        else -> "Unknown Chapter"
    }

    val formattedDate =
        DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM,
            DateFormat.SHORT,
        ).format(Date(activity.completedAt))

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = chapterName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text =
                        "${activity.score}/${activity.totalQuestions}" +
                                " · ${activity.accuracy}% · " +
                                "${activity.stars} stars",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}


