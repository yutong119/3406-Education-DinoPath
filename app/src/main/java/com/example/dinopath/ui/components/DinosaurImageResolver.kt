package com.example.dinopath.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.dinopath.R
import androidx.compose.ui.unit.dp

@DrawableRes
fun resolveDinosaurImage(
    specimenId: String?,
    specimenName: String,
): Int? {
    val id = specimenId
        ?.trim()
        ?.lowercase()
        .orEmpty()

    val name = specimenName
        .trim()
        .lowercase()

    return when {
        id.contains("stegosaurus") ||
                name.contains("stegosaurus") -> {
            R.drawable.stegosaurus
        }

        id.contains("brachiosaurus") ||
                name.contains("brachiosaurus") -> {
            R.drawable.brachiosaurus
        }

        id.contains("allosaurus") ||
                name.contains("allosaurus") -> {
            R.drawable.allosaurus
        }

        else -> null
    }
}

@Composable
fun LocalDinosaurImage(
    specimenId: String?,
    specimenName: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val imageResource = resolveDinosaurImage(
        specimenId = specimenId,
        specimenName = specimenName,
    )

    if (imageResource != null) {
        Image(
            painter = painterResource(
                id = imageResource,
            ),
            contentDescription = contentDescription,
            modifier = modifier.clip(
                RoundedCornerShape(12.dp),
            ),
            contentScale = contentScale,
        )
    } else {
        Box(
            modifier = modifier
                .clip(
                    RoundedCornerShape(12.dp),
                )
                .background(
                    MaterialTheme.colorScheme
                        .surfaceVariant,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Pets,
                contentDescription =
                    "$specimenName image unavailable",
                modifier =
                    Modifier.fillMaxSize(0.42f),
                tint =
                    MaterialTheme.colorScheme.primary,
            )
        }
    }
}