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
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.AssistChip
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.dinopath.ui.theme.DinoPathTheme

@Composable
fun HomeScreen(
    onContinueExpedition: () -> Unit,
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
                totalStars = 0,
            )
        }

        item {
            DailyExpeditionCard(
                onContinueExpedition = onContinueExpedition,
            )
        }

        item {
            FeaturedSpecimenCard()
        }

        item {
            LearningJourneySection()
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
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = "Your Prehistoric Journey",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.semantics {
                    heading()
                },
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
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun FeaturedSpecimenCard(
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

                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Add Stegosaurus to collection",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Pets,
                    contentDescription = "Stegosaurus image placeholder",
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = "Stegosaurus",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = "One of the most recognisable dinosaurs, known for its large back plates and spiked tail.",
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
private fun LearningJourneySection(
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
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
            ) {
                journeyChapters.forEachIndexed { index, chapter ->
                    JourneyChapterRow(
                        chapter = chapter,
                        showConnector = index < journeyChapters.lastIndex,
                    )
                }
            }
        }
    }
}

@Composable
private fun JourneyChapterRow(
    chapter: JourneyChapter,
    showConnector: Boolean,
    modifier: Modifier = Modifier,
) {
    val icon = when (chapter.status) {
        ChapterStatus.COMPLETED -> Icons.Outlined.Check
        ChapterStatus.CURRENT -> Icons.Outlined.RadioButtonChecked
        ChapterStatus.LOCKED -> Icons.Outlined.Lock
    }

    val statusText = when (chapter.status) {
        ChapterStatus.COMPLETED -> "Completed"
        ChapterStatus.CURRENT -> "Current expedition"
        ChapterStatus.LOCKED -> "Locked"
    }

    val statusColor = when (chapter.status) {
        ChapterStatus.COMPLETED -> MaterialTheme.colorScheme.secondary
        ChapterStatus.CURRENT -> MaterialTheme.colorScheme.primary
        ChapterStatus.LOCKED -> MaterialTheme.colorScheme.onSurfaceVariant
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
                            color = MaterialTheme.colorScheme.outlineVariant,
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
                color = if (chapter.status == ChapterStatus.LOCKED) {
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
                    text = "★".repeat(chapter.stars) +
                            "☆".repeat(3 - chapter.stars),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

private enum class ChapterStatus {
    COMPLETED,
    CURRENT,
    LOCKED,
}

private data class JourneyChapter(
    val title: String,
    val subtitle: String,
    val status: ChapterStatus,
    val stars: Int = 0,
)

private val journeyChapters = listOf(
    JourneyChapter(
        title = "Meet the Dinosaurs",
        subtitle = "Introduction to prehistoric life",
        status = ChapterStatus.COMPLETED,
        stars = 3,
    ),
    JourneyChapter(
        title = "Triassic Period",
        subtitle = "The first dinosaurs emerge",
        status = ChapterStatus.COMPLETED,
        stars = 2,
    ),
    JourneyChapter(
        title = "Jurassic Period",
        subtitle = "Giants dominate the Earth",
        status = ChapterStatus.CURRENT,
    ),
    JourneyChapter(
        title = "Cretaceous Period",
        subtitle = "A changing prehistoric world",
        status = ChapterStatus.LOCKED,
    ),
    JourneyChapter(
        title = "Dinosaur Habitats and Diets",
        subtitle = "How dinosaurs lived and ate",
        status = ChapterStatus.LOCKED,
    ),
    JourneyChapter(
        title = "Mass Extinction",
        subtitle = "The end of the dinosaur age",
        status = ChapterStatus.LOCKED,
    ),
    JourneyChapter(
        title = "Dinosaurs and Modern Birds",
        subtitle = "The dinosaurs that survived",
        status = ChapterStatus.LOCKED,
    ),
)

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun HomeScreenPreview() {
    DinoPathTheme {
        HomeScreen(
            onContinueExpedition = {},
        )
    }
}