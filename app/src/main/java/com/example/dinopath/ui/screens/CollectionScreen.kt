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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dinopath.domain.model.DinosaurSpecimen
import com.example.dinopath.ui.components.EmptyStateCard
import com.example.dinopath.ui.components.ErrorStateCard
import com.example.dinopath.ui.components.LoadingStateCard
import com.example.dinopath.ui.collection.CollectionUiState
import com.example.dinopath.ui.collection.CollectionViewModel
import com.example.dinopath.ui.components.MuseumCard
import com.example.dinopath.ui.components.MuseumDangerButton
import com.example.dinopath.ui.components.MuseumImageShape
import com.example.dinopath.ui.components.MuseumPageTitle
import com.example.dinopath.ui.theme.DinoPathTheme
import androidx.compose.ui.layout.ContentScale
import com.example.dinopath.ui.components.LocalDinosaurImage

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectionContent(
        uiState = uiState,
        onRemoveSpecimen = viewModel::removeFavourite,
        modifier = modifier,
    )
}

@Composable
private fun CollectionContent(
    uiState: CollectionUiState,
    onRemoveSpecimen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CollectionHeader(
            count = uiState.favourites.size,
        )

        if (uiState.isLoading) {
            LoadingStateCard(
                message = "Loading museum collection…",
                modifier = Modifier.padding(20.dp),
            )
        } else if (uiState.errorMessage != null) {
            ErrorStateCard(
                message = uiState.errorMessage,
                modifier = Modifier.padding(20.dp),
            )
        } else if (uiState.favourites.isEmpty()) {
            EmptyStateCard(
                title = "No specimens collected yet",
                message = "Explore the museum and add dinosaurs to your personal collection.",
                icon = Icons.Outlined.Pets,
                modifier = Modifier.padding(20.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 8.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    items = uiState.favourites,
                    key = { it.id },
                ) { specimen ->
                    FavouriteSpecimenCard(
                        specimen = specimen,
                        isRemoving = uiState.removingSpecimenId == specimen.id,
                        onRemove = { onRemoveSpecimen(specimen.id) },
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun CollectionHeader(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        MuseumPageTitle(
            title = "Museum Collection",
            subtitle = if (count == 1) "1 specimen collected" else "$count specimens collected"
        )
    }
}

@Composable
private fun FavouriteSpecimenCard(
    specimen: DinosaurSpecimen,
    isRemoving: Boolean,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MuseumCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Top,
            ) {
                LocalDinosaurImage(
                    specimenId = specimen.id,
                    specimenName = specimen.name,
                    contentDescription =
                        "${specimen.name} collection thumbnail",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(MuseumImageShape),
                    contentScale = ContentScale.Crop,
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = specimen.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = specimen.period,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            enabled = false
                        )
                    }
                }
            }

            Text(
                text = specimen.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            MuseumDangerButton(
                text = if (isRemoving) "REMOVING…" else "REMOVE FROM COLLECTION",
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isRemoving
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionPreview() {
    DinoPathTheme {
        CollectionContent(
            uiState = CollectionUiState(
                favourites = listOf(
                    DinosaurSpecimen(
                        id = "1",
                        name = "Stegosaurus",
                        period = "Late Jurassic",
                        diet = "Herbivore",
                        description = "Known for its back plates and four-spiked tail.",
                    ),
                    DinosaurSpecimen(
                        id = "2",
                        name = "Brachiosaurus",
                        period = "Late Jurassic",
                        diet = "Herbivore",
                        description = "A tall sauropod with longer front legs.",
                    ),
                ),
                isLoading = false,
            ),
            onRemoveSpecimen = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionEmptyPreview() {
    DinoPathTheme {
        CollectionContent(
            uiState = CollectionUiState(
                favourites = emptyList(),
                isLoading = false,
            ),
            onRemoveSpecimen = {},
        )
    }
}
