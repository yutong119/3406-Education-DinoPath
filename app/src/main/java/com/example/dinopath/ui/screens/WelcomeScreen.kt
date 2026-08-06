package com.example.dinopath.ui.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dinopath.R
import com.example.dinopath.ui.components.MuseumPrimaryButton
import com.example.dinopath.ui.theme.LocalReduceMotion

private val WelcomeTextColour =
    Color(0xFFFFF4D9)

@Composable
fun WelcomeScreen(
    onEnterMuseum: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val reduceMotion = LocalReduceMotion.current

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "Welcome button animation",
        )

    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (reduceMotion) {
            1f
        } else {
            1.03f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1_500,
                easing = LinearOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Enter button scale",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription =
                    "DinoPath museum entrance"
            },
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.welcome_dino_bg,
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        /*
         * The upper area remains lightly shaded so the dinosaur
         * remains visible. The lower area becomes darker so the
         * title and ENTER button remain readable.
         */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.16f),
                            Color.Black.copy(alpha = 0.38f),
                            Color.Black.copy(alpha = 0.82f),
                        ),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(
                    horizontal = 28.dp,
                    vertical = 36.dp,
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = "DinoPath",
                style =
                    MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = WelcomeTextColour,
                textAlign = TextAlign.Center,
            )

            Spacer(
                modifier = Modifier.height(12.dp),
            )

            Text(
                text =
                    "Interactive Prehistoric Learning Journey",
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                textAlign = TextAlign.Center,
            )

            Spacer(
                modifier = Modifier.height(12.dp),
            )

            Text(
                text =
                    "Travel through millions of years " +
                            "of Earth's history.",
                style =
                    MaterialTheme.typography.bodyLarge,
                color =
                    Color.White.copy(alpha = 0.88f),
                textAlign = TextAlign.Center,
            )

            Spacer(
                modifier = Modifier.height(36.dp),
            )

            MuseumPrimaryButton(
                text = "ENTER THE MUSEUM",
                onClick = onEnterMuseum,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(buttonScale)
                    .semantics {
                        contentDescription =
                            "Enter the DinoPath museum"
                    },
            )

            Spacer(
                modifier = Modifier.height(22.dp),
            )

            Text(
                text = "Explore  •  Learn  •  Discover",
                style =
                    MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color =
                    WelcomeTextColour.copy(alpha = 0.92f),
            )
        }
    }
}