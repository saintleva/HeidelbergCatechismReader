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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.viewmodels.LoadedCatechismViewModel


@Composable
fun SelectSundayScreen(navigateToReadingScreen: (Int) -> Unit, innerPadding: PaddingValues,
                       selectedQuestion: Int) {
    val viewModel = viewModel<LoadedCatechismViewModel>()
    val catechism = viewModel.catechism
    val newSelectedSunday =
        remember { mutableStateOf(catechism.sundayOfQuestion(selectedQuestion)) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 70.dp),
        modifier = Modifier.padding(innerPadding)
    ) {
        for (i in 0 until catechism.sundayCount) {
            item {
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .clickable {
                            newSelectedSunday.value = i
                            navigateToReadingScreen(catechism.sundayStart(newSelectedSunday.value))
                        },
                    colors = CardDefaults.cardColors(containerColor =
                    if (i == newSelectedSunday.value)
                        MaterialTheme.colorScheme.secondary
                    else
                        Color.LightGray
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${i + 1}",
                            fontSize = 24.sp
                        )
                        Text(
                            text = "(${stringResource(R.string.questions)}\n" +
                                    "${catechism.sundayStart(i) + 1}..${catechism.sundayEnd(i)})",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}