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

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import org.saintleva.heidelberg.Repository
import org.saintleva.heidelberg.data.Manager
import org.saintleva.heidelberg.data.TranslationMetadata
import org.saintleva.heidelberg.viewmodels.ReadingViewModelFactory
import org.saintleva.heidelberg.viewmodels.SelectTranslationViewModel
import org.saintleva.heidelberg.viewmodels.SelectTranslationViewModelFactory


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
            Text(
                text = metadata.englishName,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = metadata.language,
                style = MaterialTheme.typography.labelSmall
            )
            Row {
                Checkbox(checked = metadata.isOriginal, onCheckedChange = null)
                Text("original")
            }
        }
    }
}

@Composable
fun SelectTranslationScreen(navigateToReading: () -> Unit) {
    val vm = viewModel<SelectTranslationViewModel>(
        factory = SelectTranslationViewModelFactory(LocalContext.current)
    )

    val names = vm.allTranslations
    Log.d("compose", "names have been loaded")
    Log.d("compose", "names count is ${names.size}")
    LazyColumn {
        for (key in names.keys) {
            item {
                TranslationItem(
                    metadata = names[key]!!,
                    isCurrent = vm.isCurrent(key),
                    onTranslationChange = {
                        vm.changeCurrentTranslationId(key)
                        navigateToReading()
                    }
                )
            }
        }
    }
    Log.d("compose", "SelectTranslationScreen composed")
}