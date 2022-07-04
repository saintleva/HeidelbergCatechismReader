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
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.*
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.data.Record
import org.saintleva.heidelberg.ui.multiParagraphText
import org.saintleva.heidelberg.viewmodels.ReadingViewModel
import org.saintleva.heidelberg.viewmodels.ReadingViewModelFactory
import java.lang.Exception


@Composable
fun DataAlert(exception: DataException, onClose: () -> Unit) {

    val errorMessage = when(exception) {
        is FileLoadingException -> stringResource(R.string.error_while_loading_from_file_with)
        is DataFormatException -> stringResource(R.string.invalid_format_in_the_file_with)
        else -> stringResource(R.string.another_error_concerned_with)
    } + " " +
    when(exception.fileType) {
        FileType.TRANSLATION -> stringResource(R.string.catechism_translation)
        FileType.STRUCTURE -> stringResource(R.string.catechism_structure)
        FileType.LIST -> stringResource(R.string.translation_list)
    } + ": ${exception.message}"

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(stringResource(R.string.error)) },
        text = { Text(errorMessage) },
        buttons = {
            Box(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = onClose
                ) {
                    Text(text = "OK", fontSize = 22.sp)
                }
            }
        }
    )
}

@Composable
fun ElementSpin(element: String, previousEnabled: Boolean, onPrevious: () -> Unit,
                nextEnabled: Boolean, onNext: () -> Unit, onSelect: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onPrevious, enabled = previousEnabled) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Go to previous")
        }
        Text(text = element, modifier = Modifier.clickable { onSelect() })
        IconButton(onClick = onNext, enabled = nextEnabled) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Go to next")
        }
    }
}

@Composable
fun CopyableSubItem(text: AnnotatedString, style: TextStyle) {
    val clipboardManager = LocalClipboardManager.current
    Box {
        val expanded = remember { mutableStateOf(false) }
        Text(
            text = text,
            modifier = Modifier
                .combinedClickable(onLongClick = { expanded.value = true }, onClick = {})
                .padding(all = 4.dp),
            style = style
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            DropdownMenuItem(
                onClick = {
                    clipboardManager.setText(text)
                    expanded.value = false
                }
            ) {
                Text(stringResource(R.string.copy))
            }
        }
    }
}

@Composable
fun RecordItem(index: Int, record: Record) {
    Column {
        Text(
            text = "${stringResource(R.string.question)} ${index + 1}",
            modifier = Modifier.padding(all = 4.dp),
            style = MaterialTheme.typography.h6
        )
        CopyableSubItem(
            text = AnnotatedString(record.question),
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        CopyableSubItem(
            text = multiParagraphText(
                record.answer,
                TextIndent(firstLine = 12.sp)
            ),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun ReadingScreen(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val errorAlerted = remember { mutableStateOf(false) }

    val lazyListState: LazyListState = rememberLazyListState()

    fun lastVisibleItemIndex(): Int {
        val first = lazyListState.firstVisibleItemIndex
        val info = lazyListState.layoutInfo.visibleItemsInfo
        if (info.isEmpty())
            return -1
        return info[info.lastIndex].index
    }

    val vm = viewModel<ReadingViewModel>(
        factory = ReadingViewModelFactory(LocalContext.current)
    )

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
                if (vm.catechism.value != null) {
                    val position = lazyListState.firstVisibleItemIndex
                    ElementSpin(
                        element = "${stringResource(R.string.question)} ${position + 1}",
                        previousEnabled = position > 0,
                        onPrevious = {
                            scope.launch {
                                lazyListState.scrollToItem(position - 1)
                            }
                        },
                        nextEnabled = lastVisibleItemIndex() <
                                vm.catechism.value!!.questionCount - 1,
                        onNext = {
                            scope.launch {
                                lazyListState.scrollToItem(position + 1)
                            }
                        },
                        onSelect = {
                            navController.navigate("selectquestion")
                        }
                    )
                }
            }
        }
    ) {
        if (vm.error.value != null && errorAlerted.value == false)
            DataAlert(
                exception = vm.error.value!!,
                onClose = { errorAlerted.value = true }
            )
        if (vm.catechism.value != null) {
            errorAlerted.value = false
            val records = vm.catechism.value!!.translation.records
            val partNames = vm.catechism.value!!.translation.partNames

            LazyColumn(state = lazyListState) {
                var recordToItemIndex = 0
                var itemToRecordIndex = 0
                for (i in records.indices) {
                    val start = vm.catechism.value!!.structure.starts[i]
                    item {
                        if (start.part != null)
                            Text(
                                text = "${stringResource(R.string.part)} ${start.part!! + 1}. " +
                                        "${partNames[start.part!!]}",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 4.dp),
                                style = MaterialTheme.typography.h4
                            )
                        if (start.sunday != null)
                            Text(
                                text = "${stringResource(R.string.sunday)} ${start.sunday!! + 1}",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 4.dp),
                                style = MaterialTheme.typography.h5
                            )
                        RecordItem(i, records[i])
                        if (i < records.size - 1) {
                            Spacer(modifier = Modifier.padding(all = 4.dp))
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
        if (vm.catechism.value == null && (vm.error.value == null || errorAlerted.value == true)) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.no_translation_selected),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}