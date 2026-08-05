package com.example.dinopath.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dinopath.ui.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {
    val preferences by
    viewModel.preferences
        .collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 24.dp,
            end = 20.dp,
            bottom = 40.dp,
        ),
        verticalArrangement =
            Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = "Settings",
                modifier = Modifier.semantics {
                    heading()
                },
                style =
                    MaterialTheme.typography
                        .headlineLarge,
                fontWeight = FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme.primary,
            )
        }

        item {
            Text(
                text = "APPEARANCE & ACCESSIBILITY",
                style =
                    MaterialTheme.typography
                        .labelLarge,
                fontWeight = FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme.primary,
            )
        }

        item {
            SettingSwitchCard(
                title = "Dark Mode",
                description =
                    "Use the darker museum theme.",
                checked = preferences.darkMode,
                onCheckedChange =
                    viewModel::setDarkMode,
            )
        }

        item {
            SettingSwitchCard(
                title = "Large Text",
                description =
                    "Increase important text sizes.",
                checked = preferences.largeText,
                onCheckedChange =
                    viewModel::setLargeText,
            )
        }

        item {
            SettingSwitchCard(
                title = "High Contrast",
                description =
                    "Increase contrast between " +
                            "content and backgrounds.",
                checked =
                    preferences.highContrast,
                onCheckedChange =
                    viewModel::setHighContrast,
            )
        }

        item {
            SettingSwitchCard(
                title = "Reduce Motion",
                description =
                    "Disable non-essential animations.",
                checked =
                    preferences.reduceMotion,
                onCheckedChange =
                    viewModel::setReduceMotion,
            )
        }
    }
}

@Composable
private fun SettingSwitchCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement =
                Arrangement.spacedBy(16.dp),
            verticalAlignment =
                Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.SemiBold,
                )

                Text(
                    text = description,
                    style =
                        MaterialTheme.typography
                            .bodyMedium,
                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant,
                )
            }

            Switch(
                checked = checked,
                onCheckedChange =
                    onCheckedChange,
            )
        }
    }
}