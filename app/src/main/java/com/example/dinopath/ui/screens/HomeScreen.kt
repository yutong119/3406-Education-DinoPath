package com.example.dinopath.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.ChapterStatus
import com.example.dinopath.ui.home.HomeUiState
import com.example.dinopath.ui.home.HomeViewModel
import com.example.dinopath.ui.theme.DinoPathTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onContinueExpedition: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onContinueExpedition = onContinueExpedition,
        onToggleFavourite = viewModel::toggleFeaturedFavourite,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onContinueExpedition: () -> Unit,
    onToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "DinoPath home screen"
            },
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 24.dp,
            end = 20.dp,
            bottom = 40.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            HomeHeader(
                totalStars = uiState.totalStars,
            )
        }

        item {
            DailyExpeditionCard(
                onContinueExpedition = onContinueExpedition,
            )
        }

        item {
            FeaturedSpecimenCard(
                isFavourite = uiState.isFeaturedFavourite,
                isUpdating = uiState.isUpdatingFavourite,
                error = uiState.favouriteError,
                onToggleFavourite = onToggleFavourite,
            )
        }

        if (uiState.isLoading) {
            item {
                HomeLoadingState()
            }
        } else {
            item {
                LearningJourneySection(
                    chapters = uiState.chapters,
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    totalStars: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "DinoPath",
                modifier = Modifier.semantics {
                    heading()
                },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = "Your Prehistoric Journey",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .semantics {
                    contentDescription = "$totalStars total stars"
                },
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = totalStars.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun DailyExpeditionCard(
    onContinueExpedition: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "TODAY'S EXPEDITION",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = "Explore the Jurassic Period",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                )

                Text(
                    text = "8 min",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = "2 of 3 activities",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            LinearProgressIndicator(
                progress = { 0.67f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Button(
                onClick = onContinueExpedition,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "CONTINUE EXPEDITION",
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun FeaturedSpecimenCard(
    isFavourite: Boolean,
    isUpdating: Boolean,
    error: String?,
    onToggleFavourite: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "FEATURED SPECIMEN",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                IconButton(
                    onClick = onToggleFavourite,
                    enabled = !isUpdating,
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Icon(
                            imageVector = if (isFavourite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = if (isFavourite) {
                                "Remove Stegosaurus from collection"
                            } else {
                                "Add Stegosaurus to collection"
                            },
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        color =
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Pets,
                    contentDescription =
                    "Stegosaurus image placeholder",
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            if (error != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error,
                    )
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Text(
                text = "Stegosaurus",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text =
                "One of the most recognisable dinosaurs, " +
                        "known for its large back plates and spiked tail.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text("Late Jurassic")
                    },
                )

                AssistChip(
                    onClick = {},
                    label = {
                        Text("Herbivore")
                    },
                )
            }
        }
    }
}

@Composable
private fun HomeLoadingState(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CircularProgressIndicator()

            Text(
                text = "Loading your learning journey…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun LearningJourneySection(
    chapters: List<ChapterProgress>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "LEARNING JOURNEY",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "Travel through the age of dinosaurs",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            if (chapters.isEmpty()) {
                Text(
                    text = "No learning chapters are available.",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ),
                ) {
                    chapters.forEachIndexed { index, chapter ->
                        JourneyChapterRow(
                            chapter = chapter,
                            showConnector =
                                index < chapters.lastIndex,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JourneyChapterRow(
    chapter: ChapterProgress,
    showConnector: Boolean,
    modifier: Modifier = Modifier,
) {
    val icon = when (chapter.status) {
        ChapterStatus.COMPLETED ->
            Icons.Outlined.Check

        ChapterStatus.IN_PROGRESS ->
            Icons.Outlined.RadioButtonChecked

        ChapterStatus.LOCKED ->
            Icons.Outlined.Lock
    }

    val statusText = when (chapter.status) {
        ChapterStatus.COMPLETED ->
            "Completed"

        ChapterStatus.IN_PROGRESS ->
            "Current expedition"

        ChapterStatus.LOCKED ->
            "Locked"
    }

    val statusColor = when (chapter.status) {
        ChapterStatus.COMPLETED ->
            MaterialTheme.colorScheme.secondary

        ChapterStatus.IN_PROGRESS ->
            MaterialTheme.colorScheme.primary

        ChapterStatus.LOCKED ->
            MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = statusText,
                tint = statusColor,
            )

            if (showConnector) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .width(2.dp)
                        .height(52.dp)
                        .background(
                            color =
                                MaterialTheme.colorScheme.outlineVariant,
                        ),
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = chapter.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (
                    chapter.status == ChapterStatus.LOCKED
                ) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )

            Text(
                text = chapter.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = statusText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = statusColor,
            )

            if (chapter.status == ChapterStatus.COMPLETED) {
                Text(
                    text =
                        "★".repeat(chapter.stars.coerceIn(0, 3)) +
                                "☆".repeat(
                                    3 - chapter.stars.coerceIn(0, 3),
                                ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun HomeScreenPreview() {
    DinoPathTheme {
        HomeContent(
            uiState = HomeUiState(
                chapters = previewChapters,
                isLoading = false,
            ),
            onContinueExpedition = {},
            onToggleFavourite = {},
        )
    }
}

private val previewChapters = listOf(
    ChapterProgress(
        chapterId = 1,
        chapterOrder = 1,
        title = "Meet the Dinosaurs",
        subtitle = "Introduction to prehistoric life",
        status = ChapterStatus.COMPLETED,
        isUnlocked = true,
        stars = 3,
        bestScore = 3,
        totalQuestions = 3,
        bestAccuracy = 100,
    ),
    ChapterProgress(
        chapterId = 2,
        chapterOrder = 2,
        title = "Triassic Period",
        subtitle = "The first dinosaurs emerge",
        status = ChapterStatus.COMPLETED,
        isUnlocked = true,
        stars = 2,
        bestScore = 2,
        totalQuestions = 3,
        bestAccuracy = 66,
    ),
    ChapterProgress(
        chapterId = 3,
        chapterOrder = 3,
        title = "Jurassic Period",
        subtitle = "Giants dominate the Earth",
        status = ChapterStatus.IN_PROGRESS,
        isUnlocked = true,
        stars = 0,
        bestScore = 0,
        totalQuestions = 0,
        bestAccuracy = 0,
    ),
    ChapterProgress(
        chapterId = 4,
        chapterOrder = 4,
        title = "Cretaceous Period",
        subtitle = "A changing prehistoric world",
        status = ChapterStatus.LOCKED,
        isUnlocked = false,
        stars = 0,
        bestScore = 0,
        totalQuestions = 0,
        bestAccuracy = 0,
    ),
)