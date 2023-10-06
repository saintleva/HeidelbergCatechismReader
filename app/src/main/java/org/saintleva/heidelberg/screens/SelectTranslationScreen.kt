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

package org.saintleva.heidelberg.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.data.TranslationListState
import org.saintleva.heidelberg.data.TranslationMetadata
import org.saintleva.heidelberg.viewmodels.SelectTranslationViewModel


@Composable
fun TranslationItem(metadata: TranslationMetadata, isCurrent: Boolean, onTranslationChange: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isCurrent)
                        MaterialTheme.colorScheme.secondary
                    else
                        MaterialTheme.colorScheme.surface
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clickable { onTranslationChange() },
            ) {
            Text(
                text = metadata.name,
                style = MaterialTheme.typography.labelSmall
            )
            if (!metadata.isEnglish()) {
                Text(
                    text = metadata.englishName,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                text = metadata.language,
                style = MaterialTheme.typography.labelSmall
            )
            if (metadata.isOriginal) {
                Row {
                    Icon(Icons.Default.Check, contentDescription = "Original")
                    Text(stringResource(R.string.original))
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

    val errorAlerted = remember { mutableStateOf(false) }

    when (val state = viewModel.allTranslations.value) {
        TranslationListState.None -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewModel.loadTranslationList()
        }

        is TranslationListState.Error -> {
            if (errorAlerted.value) {
                NoListBox()
            } else {
                DataAlert(
                    exception = state.error,
                    onClose = { errorAlerted.value = true }
                )
            }
        }

        is TranslationListState.Loaded -> {
            val names = state.all
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                for (key in names.keys) {
                    item {
                        TranslationItem(
                            metadata = names[key]!!,
                            isCurrent = viewModel.isCurrent(key),
                            onTranslationChange = {
                                viewModel.changeCurrentTranslationId(key)
                                navigateToReadingScreen()
                            }
                        )
                    }
                }
            }
        }
    }
}