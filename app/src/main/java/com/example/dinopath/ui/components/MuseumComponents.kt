package com.example.dinopath.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dinopath.ui.theme.MuseumDarkBorder
import com.example.dinopath.ui.theme.MuseumGoldBorder

// Museum Visual System Shapes
val MuseumCardShape = RoundedCornerShape(20.dp)
val MuseumImageShape = RoundedCornerShape(16.dp)
val MuseumButtonShape = RoundedCornerShape(16.dp)
val MuseumIconShape = RoundedCornerShape(14.dp)

@Composable
fun MuseumCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val border = if (isSystemInDarkTheme()) {
        BorderStroke(1.dp, MuseumGoldBorder)
    } else {
        BorderStroke(1.dp, MuseumDarkBorder)
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = MuseumCardShape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            content = { content() }
        )
    } else {
        Card(
            modifier = modifier,
            shape = MuseumCardShape,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            border = border,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            content = { content() }
        )
    }
}

@Composable
fun MuseumSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subtitle: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { heading() }
            )
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun MuseumPageTitle(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Column(modifier = modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.semantics { heading() }
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MuseumPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = enabled && !loading,
        shape = MuseumButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Icon(imageVector = leadingIcon, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = trailingIcon, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun MuseumOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    loading: Boolean = false,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = enabled && !loading,
        shape = MuseumButtonShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Icon(imageVector = leadingIcon, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = trailingIcon, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun MuseumDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(54.dp),
        enabled = enabled && !loading,
        shape = MuseumButtonShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.error,
                strokeWidth = 2.dp
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Icon(imageVector = leadingIcon, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MuseumStarBadge(
    stars: Int,
    modifier: Modifier = Modifier
) {
    val border = if (isSystemInDarkTheme()) {
        BorderStroke(1.dp, MuseumGoldBorder)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    }

    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        border = border
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stars.toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun MuseumIconContainer(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(MuseumIconShape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Helper to provide Surface since it was missing in imports
@Composable
private fun Surface(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = androidx.compose.ui.graphics.RectangleShape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = androidx.compose.material3.contentColorFor(color),
    tonalElevation: androidx.compose.ui.unit.Dp = 0.dp,
    shadowElevation: androidx.compose.ui.unit.Dp = 0.dp,
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
        content = content
    )
}
