/*
 * Copyright (C) Anton Liaukevich 2022-2024 <leva.dev@gmail.com>
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

package org.saintleva.heidelberg.ui.screens.select

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.ui.screens.common.LoadedCatechismViewModel


@Composable
fun SelectQuestionScreen(navigateToReadingScreen: (Int) -> Unit, innerPadding: PaddingValues,
                         selected: Int) {
    val viewModel = koinViewModel<LoadedCatechismViewModel>()
    val newSelected = rememberSaveable { mutableStateOf(selected) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier = Modifier.padding(innerPadding)
    ) {
        for (i in 0 until viewModel.catechism.questionCount) {
            item {
                Card(
                    modifier = Modifier
                        .padding(1.dp)
                        .clickable {
                            newSelected.value = i
                            navigateToReadingScreen(newSelected.value)
                        },
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor =
                        if (i == newSelected.value)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.primaryContainer
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${i + 1}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "(${stringResource(R.string.sunday)}" +
                                    " ${viewModel.catechism.sundayOfQuestion(i) + 1})",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}