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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.Repository
import org.saintleva.heidelberg.viewmodels.ReadingViewModel
import org.saintleva.heidelberg.viewmodels.ReadingViewModelFactory

//import org.saintleva.heidelberg.viewmodels.ReadingViewModelFactory


@Composable
fun ReadingScreen(navController: NavHostController) {
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
//            if (Repository.currentTranslationId.value == null)
//                Text("TRANSLATION NOT SELECTED")
//            else {
//                Text(Repository.currentTranslationId.value!!)
//                Text(Repository.currentTraslationName!!)
//            }
//            Text(Repository.transtation.value)
        val vm = viewModel<ReadingViewModel>(
            factory = ReadingViewModelFactory(LocalContext.current)
        )

        if (vm.error.value != null)
            AlertDialog(
                onDismissRequest = {},
                title = { Text(stringResource(R.string.error)) },
                text = { Text(stringResource(R.string.translation_format_error)) },
                buttons = {
                    Box(modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth()) {
                        Button(
                            modifier = Modifier.align(Alignment.Center),
                            onClick = {}
                        ) {
                            Text("OK", fontSize = 22.sp)
                        }
                    }
                }

            )
        else {
            val records = vm.translation!!.records
            LazyColumn {
                for (i in 0 until records.count())
                    item {
                        Column {
                            //TODO: Use stringResource()
                            Text(
                                text = "${stringResource(R.string.question)} ${i + 1}",
                                modifier = Modifier.padding(all = 4.dp),
                                style = MaterialTheme.typography.h6
                            )
                            Text(
                                text = records[i].question,
                                modifier = Modifier.padding(all = 4.dp),
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = records[i].answer,
                                modifier = Modifier.padding(all = 4.dp),
                                style = MaterialTheme.typography.body2
                            )
                        }
                        Divider(thickness = 3.dp)
                    }
                item {
                    Text(vm.translation!!.description)
                }
            }
        }
    }
}