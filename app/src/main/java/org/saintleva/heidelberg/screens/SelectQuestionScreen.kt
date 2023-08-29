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
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.viewmodels.LoadedCatechismViewModel


@Composable
fun SelectQuestionScreen(navigateToReadingScreen: (Int) -> Unit, selected: Int) {
    val vm = viewModel<LoadedCatechismViewModel>()
    val newSelected = remember { mutableStateOf(selected) }

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 70.dp)) {
        for (i in 0 until vm.catechism.questionCount) {
            item {
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .clickable {
                            newSelected.value = i
                            navigateToReadingScreen(newSelected.value)
                        },
                    colors = CardDefaults.cardColors(containerColor =
                        if (i == newSelected.value)
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
                            text = "(${stringResource(R.string.sunday)}" +
                                    " ${vm.catechism.sundayOfQuestion(i) + 1})",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}