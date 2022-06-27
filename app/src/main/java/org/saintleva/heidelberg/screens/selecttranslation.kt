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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import org.saintleva.heidelberg.Repository
import org.saintleva.heidelberg.viewmodels.SelectTranslationViewModel


@Composable
fun TranslationItem(name: String, isCurrent: Boolean, onTranslationChange: () -> Unit) {
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isCurrent)
                        MaterialTheme.colors.secondary
                    else
                        MaterialTheme.colors.surface
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clickable { onTranslationChange() },
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun SelectTranslationScreen(navController: NavHostController) {
    val vm = viewModel<SelectTranslationViewModel>()
    val names = Repository.names
    LazyColumn {
        for (key in names.keys)
            item {
                TranslationItem(
                    name = names[key]!!,
                    isCurrent = vm.currentTranslationId.value == key ,
                    onTranslationChange = {
                        vm.changeCurrentTranslationId(key)
                        navController.navigate("reading")
                    }
                )
            }
    }
}