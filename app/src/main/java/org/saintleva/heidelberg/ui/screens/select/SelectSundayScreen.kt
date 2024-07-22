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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.ui.screens.common.LoadedCatechismViewModel


@Composable
fun SelectSundayScreen(navigateToReadingScreen: (Int) -> Unit, innerPadding: PaddingValues,
                       selectedQuestion: Int) {
    val viewModel = viewModel<LoadedCatechismViewModel>()
    val catechism = viewModel.catechism
    val newSelectedSunday =
        remember { mutableStateOf(catechism.sundayOfQuestion(selectedQuestion)) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 90.dp),
        modifier = Modifier.padding(innerPadding)
    ) {
        for (i in 0 until catechism.sundayCount) {
            item {
                Card(
                    modifier = Modifier
                        .padding(1.dp)
                        .clickable {
                            newSelectedSunday.value = i
                            navigateToReadingScreen(catechism.sundayStart(newSelectedSunday.value))
                        },
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor =
                    if (i == newSelectedSunday.value)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.tertiaryContainer
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
                            text = "(${stringResource(R.string.questions)}\n" +
                                    "${catechism.sundayStart(i) + 1}..${catechism.sundayEnd(i)})",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}