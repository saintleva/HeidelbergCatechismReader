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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.Repository
import org.saintleva.heidelberg.viewmodels.ReadingViewModel
import org.saintleva.heidelberg.viewmodels.ReadingViewModelFactory


@Composable
fun ReadingScreean(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { SelectTranslationScreen(navController) },
        topBar = {
            TopAppBar {
                IconButton(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                ) {
                    Icon(Icons.Filled.Face, contentDescription = "Navigation Drawer")
                }
            }
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green)
        ) {
//            if (Repository.currentTranslationId.value == null)
//                Text("TRANSLATION NOT SELECTED")
//            else {
//                Text(Repository.currentTranslationId.value!!)
//                Text(Repository.currentTraslationName!!)
//            }
//            Text(Repository.transtation.value)
            val vm = viewModel<ReadingViewModel>(
                factory = ReadingViewModelFactory(LocalContext.current))
            LazyColumn {
                for (record in vm.translation.records)
                    item {
                        Column {
                            Text(record.question)
                            Text(record.answer)
                        }
                    }
            }
        }
    }
}