/*
 * Copyright (C) Anton Liaukevich 2021-2022 <leva.dev@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.saintleva.heidelberg.ui.screens.selecttranslation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.data.manager.CombinedTranslationListState
import org.saintleva.heidelberg.data.manager.TranslationListState
import org.saintleva.heidelberg.data.models.TranslationMetadata
import org.saintleva.heidelberg.ui.screens.common.DataAlert


@Composable
fun TranslationItem(metadata: TranslationMetadata, isCurrent: Boolean, onTranslationChange: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
//        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isCurrent)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clickable { onTranslationChange() },
            ) {
            if (metadata.name != null) {
                Text(
                    text = metadata.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (metadata.englishName != null) {
                if (metadata.name != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    text = metadata.englishName,
                    style = if (metadata.name == null)
                        MaterialTheme.typography.bodyMedium
                    else
                        MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
                )
            }
            if (metadata.isOriginal) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = "Original")
                    Text(
                        text = stringResource(R.string.original),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun NoListBox() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.no_translation_list_loaded),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun SelectTranslationScreen(navigateToReadingScreen: () -> Unit, innerPadding: PaddingValues) {
    val viewModel = viewModel<SelectTranslationViewModel>()
    val combinedTranslations = viewModel.combinedTranslations.collectAsStateWithLifecycle()

    val errorAlerted = remember { mutableStateOf(false) }

    when (val state = combinedTranslations.value) {
        CombinedTranslationListState.None -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewModel.loadTranslationList()
        }

        is CombinedTranslationListState.Error -> {
            if (errorAlerted.value) {
                NoListBox()
            } else {
                DataAlert(
                    exception = state.error,
                    onClose = { errorAlerted.value = true }
                )
            }
        }

        is CombinedTranslationListState.Loaded -> {
            val combined = state.combined
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                for (lang in combined.keys) {
                    val names = combined[lang]!!
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = lang, style = MaterialTheme.typography.titleLarge)
                        }
                    }
                    for (name in names.toSortedSet(compareBy { it.data.nameToUse })) {
                        item {
                            TranslationItem(
                                metadata = name.data,
                                isCurrent = viewModel.isCurrent(name.id),
                                onTranslationChange = {
                                    viewModel.changeCurrentTranslationId(name.id)
                                    navigateToReadingScreen()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}