package com.example.dinopath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.dinopath.ui.components.MuseumCard
import com.example.dinopath.ui.components.MuseumPrimaryButton
import com.example.dinopath.ui.components.MuseumSectionHeader
import com.example.dinopath.ui.theme.MuseumOverlay

@Composable
fun ExhibitionScreen(
    onStartKnowledgeCheck: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedGallery by rememberSaveable {
        mutableStateOf(ExhibitionGallery.CLIMATE)
    }

    Column(modifier = modifier.fillMaxSize()) {
        ExhibitionHeader(
            progress = 0.60f,
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = 40.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
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
                MuseumPrimaryButton(
                    text = "START KNOWLEDGE CHECK",
                    onClick = onStartKnowledgeCheck,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ExhibitionHeader(
    progress: Float,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
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
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Jurassic Period",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                modifier = Modifier.semantics { heading() }
            )

            Text(
                text = "201–145 million years ago",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Exhibition 3 of 7",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.White.copy(alpha = 0.2f),
            )
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 48.dp, start = 12.dp)
                .align(Alignment.TopStart),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MuseumOverlay.copy(alpha = 0.4f),
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back"
            )
        }
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

    MuseumCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                content.facts.forEach { fact ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = fact,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MuseumSectionHeader(
            title = "Dinosaur Highlights",
            icon = Icons.Outlined.Pets
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
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
    MuseumCard(
        modifier = modifier.width(260.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp),
        ) {
            Image(
                painter = painterResource(
                    id = dinosaur.imageRes,
                ),
                contentDescription =
                    "${dinosaur.name} dinosaur illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(14.dp),
                    ),
                contentScale = ContentScale.Crop,
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = dinosaur.name,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = dinosaur.diet.uppercase(),
                    style =
                        MaterialTheme.typography.labelLarge,
                    color =
                        MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = dinosaur.description,
                style =
                    MaterialTheme.typography.bodyMedium,
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
            onBack = {},
        )
    }
}