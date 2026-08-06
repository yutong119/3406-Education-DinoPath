package com.example.dinopath.ui.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.ChapterStatus
import com.example.dinopath.ui.components.MuseumCard
import com.example.dinopath.ui.components.MuseumSectionHeader

@Composable
fun EvolutionMapSection(
    chapters: List<ChapterProgress>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MuseumSectionHeader(
            title = "Evolution Map",
            subtitle = "Your journey through prehistoric time"
        )

        MuseumCard(
            modifier = Modifier.fillMaxWidth(),
            emphasized = true,
        ) {
            if (chapters.isEmpty()) {
                Text(
                    text = "No learning progress is available.",
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    chapters.forEachIndexed { index, chapter ->
                        EvolutionNode(
                            chapter = chapter,
                            showConnector = index < chapters.lastIndex,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EvolutionNode(
    chapter: ChapterProgress,
    showConnector: Boolean,
    modifier: Modifier = Modifier,
) {
    val statusText = when (chapter.status) {
        ChapterStatus.COMPLETED -> "Completed"
        ChapterStatus.IN_PROGRESS -> "In progress"
        ChapterStatus.LOCKED -> "Locked"
    }

    val icon = when (chapter.status) {
        ChapterStatus.COMPLETED -> Icons.Outlined.Check
        ChapterStatus.IN_PROGRESS ->
            Icons.Outlined.RadioButtonChecked
        ChapterStatus.LOCKED -> Icons.Outlined.Lock
    }

    val nodeColor = when (chapter.status) {
        ChapterStatus.COMPLETED ->
            MaterialTheme.colorScheme.secondary

        ChapterStatus.IN_PROGRESS ->
            MaterialTheme.colorScheme.primary

        ChapterStatus.LOCKED ->
            MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(
                if (chapter.status == ChapterStatus.LOCKED) {
                    0.65f
                } else {
                    1f
                },
            )
            .semantics {
                contentDescription =
                    "${chapter.title}, $statusText, " +
                            "${chapter.stars} stars"
            },
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = nodeColor,
            )

            if (showConnector) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .width(2.dp)
                        .height(48.dp)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant,
                        ),
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = chapter.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = statusText,
                style = MaterialTheme.typography.labelMedium,
                color = nodeColor,
            )

            if (chapter.status == ChapterStatus.COMPLETED) {
                Text(
                    text =
                        "★".repeat(chapter.stars.coerceIn(0, 3)) +
                                "☆".repeat(
                                    3 - chapter.stars.coerceIn(0, 3),
                                ),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}