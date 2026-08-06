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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import coil.compose.SubcomposeAsyncImage
import com.example.dinopath.ui.theme.LocalReduceMotion
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.ChapterStatus
import com.example.dinopath.domain.model.SpecimenDetails
import com.example.dinopath.ui.components.ErrorStateCard
import com.example.dinopath.ui.components.LoadingStateCard
import com.example.dinopath.ui.home.HomeUiState
import com.example.dinopath.ui.home.HomeViewModel
import com.example.dinopath.ui.theme.DinoPathTheme
import androidx.compose.foundation.Image
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.dinopath.R
import com.example.dinopath.ui.components.MuseumCard
import com.example.dinopath.ui.components.MuseumIconContainer
import com.example.dinopath.ui.components.MuseumPageTitle
import com.example.dinopath.ui.components.MuseumPrimaryButton
import com.example.dinopath.ui.components.MuseumSectionHeader
import com.example.dinopath.ui.components.MuseumStarBadge
import com.example.dinopath.ui.theme.MuseumOverlay

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
        onRetrySpecimen = {
            viewModel.loadFeaturedSpecimen(forceRefresh = true)
        },
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onContinueExpedition: () -> Unit,
    onToggleFavourite: () -> Unit,
    onRetrySpecimen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 16.dp,
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
                specimen = uiState.specimenDetails,
                isLoading = uiState.isSpecimenLoading,
                error = uiState.specimenError,
                isFavourite = uiState.isFeaturedFavourite,
                isUpdating = uiState.isUpdatingFavourite,
                favouriteError = uiState.favouriteError,
                onToggleFavourite = onToggleFavourite,
                onRetry = onRetrySpecimen,
            )
        }

        if (uiState.isLoading) {
            item {
                LoadingStateCard(
                    message = "Loading your learning journey…",
                )
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MuseumPageTitle(
            title = "DinoPath",
            subtitle = "Your Prehistoric Journey",
            modifier = Modifier.weight(1f)
        )

        val reduceMotion = LocalReduceMotion.current
        if (reduceMotion) {
            MuseumStarBadge(
                stars = totalStars,
                modifier = Modifier.semantics {
                    contentDescription = "$totalStars total stars"
                }
            )
        } else {
            AnimatedContent(
                targetState = totalStars,
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                },
                label = "TotalStars"
            ) { targetStars ->
                MuseumStarBadge(
                    stars = targetStars,
                    modifier = Modifier.semantics {
                        contentDescription = "$targetStars total stars"
                    }
                )
            }
        }
    }
}

@Composable
private fun DailyExpeditionCard(
    onContinueExpedition: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MuseumCard(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.brachiosaurus),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MuseumOverlay
                            ),
                            startY = 100f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
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
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = "8 min • 2 of 3 activities",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { 0.67f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.White.copy(alpha = 0.2f),
                )

                Spacer(modifier = Modifier.height(16.dp))

                MuseumPrimaryButton(
                    text = "CONTINUE EXPEDITION",
                    onClick = onContinueExpedition,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.AutoMirrored.Outlined.ArrowForward
                )
            }
        }
    }
}

@Composable
private fun FeaturedSpecimenCard(
    specimen: SpecimenDetails?,
    isLoading: Boolean,
    error: String?,
    isFavourite: Boolean,
    isUpdating: Boolean,
    favouriteError: String?,
    onToggleFavourite: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MuseumCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically,
            ) {
                Text(
                    text = "FEATURED SPECIMEN",
                    style =
                        MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary,
                )

                IconButton(
                    onClick = onToggleFavourite,
                    enabled = !isUpdating,
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Icon(
                            imageVector =
                                if (isFavourite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined
                                        .FavoriteBorder
                                },
                            contentDescription =
                                if (isFavourite) {
                                    "Remove Stegosaurus " +
                                            "from collection"
                                } else {
                                    "Add Stegosaurus " +
                                            "to collection"
                                },
                            tint =
                                MaterialTheme.colorScheme
                                    .primary,
                        )
                    }
                }
            }

            when {
                isLoading -> {
                    LocalStegosaurusImage(
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp),
                        verticalAlignment =
                            Alignment.CenterVertically,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                        )

                        Text(
                            text =
                                "Loading live museum notes…",
                            style =
                                MaterialTheme.typography
                                    .bodyMedium,
                            color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant,
                        )
                    }
                }

                error != null -> {
                    LocalStegosaurusImage(
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    )

                    Text(
                        text = "Stegosaurus",
                        style =
                            MaterialTheme.typography
                                .headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color =
                            MaterialTheme.colorScheme
                                .onSurface,
                    )

                    Text(
                        text =
                            "One of the most recognisable " +
                                    "dinosaurs, known for its " +
                                    "large back plates and " +
                                    "spiked tail.",
                        style =
                            MaterialTheme.typography
                                .bodyMedium,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant,
                    )

                    Text(
                        text = "Offline guide · Live content unavailable",
                        style =
                            MaterialTheme.typography
                                .labelMedium,
                        color =
                            MaterialTheme.colorScheme
                                .secondary,
                    )

                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "RETRY LIVE CONTENT",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                specimen != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                color =
                                    MaterialTheme.colorScheme
                                        .surfaceVariant,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        SubcomposeAsyncImage(
                            model = specimen.imageUrl,
                            contentDescription =
                                "${specimen.displayTitle} " +
                                        "specimen image",
                            modifier =
                                Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            loading = {
                                LocalStegosaurusImage(
                                    modifier =
                                        Modifier.fillMaxSize(),
                                )
                            },
                            error = {
                                LocalStegosaurusImage(
                                    modifier =
                                        Modifier.fillMaxSize(),
                                )
                            },
                        )
                    }

                    Text(
                        text = specimen.displayTitle,
                        style =
                            MaterialTheme.typography
                                .headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color =
                            MaterialTheme.colorScheme
                                .onSurface,
                    )

                    Text(
                        text = specimen.summary,
                        style =
                            MaterialTheme.typography
                                .bodyMedium,
                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant,
                    )

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically,
                        horizontalArrangement =
                            Arrangement.spacedBy(4.dp),
                    ) {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = "Wikipedia",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            enabled = false
                        )

                        if (specimen.isFromCache) {
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = "Offline cache",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                enabled = false
                            )
                        }
                    }
                }
            }

            if (favouriteError != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector =
                            Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint =
                            MaterialTheme.colorScheme.error,
                    )

                    Text(
                        text = favouriteError,
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Composable
private fun LocalStegosaurusImage(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(
            id = R.drawable.stegosaurus,
        ),
        contentDescription =
            "Stegosaurus museum illustration",
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        contentScale = ContentScale.Crop,
    )
}


@Composable
private fun LearningJourneySection(
    chapters: List<ChapterProgress>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MuseumSectionHeader(
            title = "Learning Journey",
            subtitle = "Travel through the age of dinosaurs",
            icon = Icons.Outlined.Pets
        )

        MuseumCard(
            modifier = Modifier.fillMaxWidth(),
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
                        vertical = 20.dp,
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
                specimenDetails = SpecimenDetails(
                    queryTitle = "Stegosaurus",
                    displayTitle = "Stegosaurus",
                    summary = "One of the most recognisable dinosaurs, known for its large back plates and spiked tail.",
                    imageUrl = null,
                    isFromCache = false,
                ),
                isSpecimenLoading = false,
            ),
            onContinueExpedition = {},
            onToggleFavourite = {},
            onRetrySpecimen = {},
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