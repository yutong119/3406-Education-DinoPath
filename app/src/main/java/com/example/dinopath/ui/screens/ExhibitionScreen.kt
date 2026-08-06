package com.example.dinopath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Button
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.dinopath.ui.theme.DinoPathTheme
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.dinopath.R

@Composable
fun ExhibitionScreen(
    onStartKnowledgeCheck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedGallery by rememberSaveable {
        mutableStateOf(ExhibitionGallery.CLIMATE)
    }

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
            ExhibitionHeader(
                progress = 0.60f,
            )
        }
        item {
            GallerySelector(
                selectedGallery = selectedGallery,
                onGallerySelected = { gallery ->
                    selectedGallery = gallery
                },
            )
        }
        item {
            GalleryLearningCard(
                gallery = selectedGallery,
            )
        }
        item {
            DinosaurHighlightsSection()
        }

        item {
            Button(
                onClick = onStartKnowledgeCheck,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "START KNOWLEDGE CHECK",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun ExhibitionHeader(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Jurassic Period",
                    modifier = Modifier.semantics {
                        heading()
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "201–145 million years ago",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Icon(
                imageVector = Icons.Outlined.Museum,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Exhibition 3 of 7",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

private enum class ExhibitionGallery(
    val label: String,
) {
    CLIMATE("Gallery 1 · Climate"),
    DINOSAURS("Gallery 2 · Dinosaurs"),
    LIFE("Gallery 3 · Life"),
}

@Composable
private fun GallerySelector(
    selectedGallery: ExhibitionGallery,
    onGallerySelected: (ExhibitionGallery) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExhibitionGallery.entries.forEach { gallery ->
            FilterChip(
                selected = gallery == selectedGallery,
                onClick = {
                    onGallerySelected(gallery)
                },
                label = {
                    Text(gallery.label)
                },
            )
        }
    }
}

@Composable
private fun GalleryLearningCard(
    gallery: ExhibitionGallery,
    modifier: Modifier = Modifier,

) {
    val content = gallery.content()

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
                text = content.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = content.introduction,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = "KEY FACTS",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            content.facts.forEach { fact ->
                Text(
                    text = "•  $fact",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Text(
                text = "THINK ABOUT IT",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
            )

            Text(
                text = content.reflectionQuestion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

        }
    }
}

@Composable
private fun DinosaurHighlightsSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "DINOSAUR HIGHLIGHTS",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                count = dinosaurHighlights.size,
                key = { index ->
                    dinosaurHighlights[index].name
                },
            ) { index ->
                DinosaurHighlightCard(
                    dinosaur = dinosaurHighlights[index],
                )

            }

        }
    }
}

@Composable
private fun DinosaurHighlightCard(
    dinosaur: DinosaurHighlight,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.width(240.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp),
        ) {
            Image(
                painter = painterResource(
                    id = dinosaur.imageRes,
                ),
                contentDescription =
                    "${dinosaur.name} dinosaur illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(
                        RoundedCornerShape(14.dp),
                    ),
                contentScale = ContentScale.Crop,
            )

            Text(
                text = dinosaur.name,
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = dinosaur.diet,
                style =
                    MaterialTheme.typography.labelLarge,
                color =
                    MaterialTheme.colorScheme.primary,
            )

            Text(
                text = dinosaur.description,
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant,
            )
        }
    }
}

private data class GalleryContent(
    val title: String,
    val introduction: String,
    val facts: List<String>,
    val reflectionQuestion: String,
)

private fun ExhibitionGallery.content(): GalleryContent {
    return when (this) {
        ExhibitionGallery.CLIMATE -> GalleryContent(
            title = "Climate and Environment",
            introduction =
                "The Jurassic Period was generally warm and humid. " +
                        "Large forests spread across many parts of Earth.",
            facts = listOf(
                "There were no permanent polar ice caps.",
                "Pangaea gradually began to separate.",
                "Warm forests supported large herbivorous dinosaurs.",
            ),
            reflectionQuestion =
                "How might a warm climate have helped very large dinosaurs survive?",
        )

        ExhibitionGallery.DINOSAURS -> GalleryContent(
            title = "Jurassic Dinosaurs",
            introduction =
                "The Jurassic Period was home to enormous sauropods, " +
                        "armoured herbivores and powerful predators.",
            facts = listOf(
                "Brachiosaurus used its long neck to reach vegetation.",
                "Stegosaurus had large plates and a spiked tail.",
                "Allosaurus was one of the major Jurassic predators.",
            ),
            reflectionQuestion =
                "Why might different body shapes have helped dinosaurs share one habitat?",
        )

        ExhibitionGallery.LIFE -> GalleryContent(
            title = "Life Beyond Dinosaurs",
            introduction =
                "Jurassic ecosystems included marine reptiles, early mammals, " +
                        "insects, plants and the earliest known birds.",
            facts = listOf(
                "Small mammals lived alongside dinosaurs.",
                "Marine reptiles occupied Jurassic oceans.",
                "Archaeopteryx had both dinosaur and bird features.",
            ),
            reflectionQuestion =
                "What evidence connects some dinosaurs with modern birds?",
        )
    }
}

private data class DinosaurHighlight(
    val name: String,
    val diet: String,
    val description: String,
    @DrawableRes
    val imageRes: Int,
)

private val dinosaurHighlights = listOf(
    DinosaurHighlight(
        name = "Stegosaurus",
        diet = "Herbivore",
        description =
            "Known for its back plates and " +
                    "four-spiked tail.",
        imageRes = R.drawable.stegosaurus,
    ),
    DinosaurHighlight(
        name = "Brachiosaurus",
        diet = "Herbivore",
        description =
            "A tall sauropod with longer " +
                    "front legs.",
        imageRes = R.drawable.brachiosaurus,
    ),
    DinosaurHighlight(
        name = "Allosaurus",
        diet = "Carnivore",
        description =
            "A large predator of the " +
                    "Late Jurassic.",
        imageRes = R.drawable.allosaurus,
    ),
)

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun ExhibitionScreenPreview() {
    DinoPathTheme {
        ExhibitionScreen(
            onStartKnowledgeCheck = {},
        )
    }
}