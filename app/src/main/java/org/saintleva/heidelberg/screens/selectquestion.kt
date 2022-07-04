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
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.viewmodels.SelectQuestionViewModel


@Composable
fun SelectQuestionScreen(navController: NavHostController) {
    val vm = viewModel<SelectQuestionViewModel>()
    
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 60.dp)) {
        for (i in 0 until vm.catechism.value!!.questionCount)
            item {
                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .clickable { navController.navigateUp() },
                    backgroundColor = Color.LightGray,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${i + 1}",
                            fontSize = 24.sp//,
//                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${stringResource(R.string.sunday)}" +
                                    " ${vm.catechism.value!!.sundayOfQuestion[i] + 1}",
                            fontSize = 10.sp
                        )
                    }
                }
            }
    }
}