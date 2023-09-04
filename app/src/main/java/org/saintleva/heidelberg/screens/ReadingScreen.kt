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

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.*
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.R.string.about_translation
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.ui.multiParagraphText
import org.saintleva.heidelberg.viewmodels.*


@Composable
fun ElementSpin(element: String, previousEnabled: Boolean, onPrevious: () -> Unit,
                nextEnabled: Boolean, onNext: () -> Unit, onSelect: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onPrevious, enabled = previousEnabled) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Go to previous")
        }
        Text(text = element, modifier = Modifier.clickable { onSelect() })
        IconButton(onClick = onNext, enabled = nextEnabled) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Go to next")
        }
    }
}

fun romanNumber(number: Int): String {
    val list = listOf("I", "II", "III")
    return list[number]
}

object ReadingTextTransformer : TextTransformer {

    override fun transformQuestion(source: String, index: Int): AnnotatedString {
        return AnnotatedString(source)
    }
    override fun transformAnswer(source: String, index: Int): AnnotatedString {
        return multiParagraphText(source, TextIndent(firstLine = 12.sp))
    }
}

@Composable
fun CatechismNavigationButtons(viewModel: ReadingViewModel, lazyListState: LazyListState,
                               scope: CoroutineScope, navigateToScreens: NavigateToScreens) {

    if (viewModel.catechismState.value !is CatechismState.Loaded) return

    fun lastVisibleItemIndex(): Int {
        val info = lazyListState.layoutInfo.visibleItemsInfo
        if (info.isEmpty())
            return -1
        return info[info.lastIndex].index
    }

    val catechism = (viewModel.catechismState.value as CatechismState.Loaded).catechism
    val position = lazyListState.firstVisibleItemIndex

    ElementSpin(
        element = "${stringResource(R.string.question)} ${position + 1}",
        previousEnabled = position > 0,
        onPrevious = {
            scope.launch {
                lazyListState.scrollToItem(position - 1)
                viewModel.saveScrollPosition(lazyListState)
            }
        },
        nextEnabled = lastVisibleItemIndex() < catechism.questionCount - 1,
        onNext = {
            scope.launch {
                lazyListState.scrollToItem(position + 1)
                viewModel.saveScrollPosition(lazyListState)
            }
        },
        onSelect = {
            scope.launch {
                navigateToScreens.selectQuestion(position)
            }
        }
    )
    Log.d("compose", "lastVisibleItemIndex() == ${lastVisibleItemIndex()}")
    val sunday = catechism.sundayOfQuestion(position)
    ElementSpin(
        element = "${stringResource(R.string.sunday)} ${sunday + 1}",
        previousEnabled = sunday > 0,
        onPrevious = {
            scope.launch {
                lazyListState.scrollToItem(catechism.sundayStart(sunday - 1))
                viewModel.saveScrollPosition(lazyListState)
            }
        },
        nextEnabled = lastVisibleItemIndex() < 0 ||
                catechism.sundayOfQuestion(lastVisibleItemIndex()) <
                catechism.sundayCount - 1,
        onNext = {
            scope.launch {
                lazyListState.scrollToItem(catechism.sundayStart(sunday + 1))
                viewModel.saveScrollPosition(lazyListState)
            }
        },
        onSelect = {
            scope.launch {
                navigateToScreens.selectSunday(position)
            }
        }
    )
}

@Composable
fun NoTranslationBox() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.no_translation_selected),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun ReadingArea(vm: ReadingViewModel, questionPosition: Int) {

    val lazyListState =
        if (questionPosition == -1)
            rememberLazyListState(vm.scrollPosition.value.firstVisibleItemIndex,
                vm.scrollPosition.value.firstVisibleItemScrollOffset)
        else
            rememberLazyListState(questionPosition)

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset,
                                      source: NestedScrollSource): Offset {
                vm.saveScrollPosition(lazyListState)
                return Offset.Zero
            }
        }
    }

    val errorAlerted = remember { mutableStateOf(false) }

    when (val state = vm.catechismState.value) {
        CatechismState.Never -> {
            NoTranslationBox()
        }

        is CatechismState.Error -> {
            if (errorAlerted.value) {
                NoTranslationBox()
            } else {
                DataAlert(
                    exception = state.error,
                    onClose = { errorAlerted.value = true }
                )
            }
        }

        CatechismState.SelectedToLoad -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            vm.selectToLoad()
        }

        is CatechismState.Loaded -> {
            val catechism = state.catechism
            LazyColumn(
                modifier = Modifier.nestedScroll(nestedScrollConnection),
                state = lazyListState
            ) {
                for (i in 0 until catechism.questionCount) {
                    val start = catechism.start(i)
                    item {
                        if (start.part != null) {
                            Text(
                                text = "${catechism.blockNames.part} ${romanNumber(start.part!!)}. " +
                                        "${catechism.partName(start.part!!)}",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 4.dp),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        if (start.sunday != null) {
                            Text(
                                text = "${catechism.blockNames.sunday} ${start.sunday!! + 1}",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 4.dp),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        RecordItem(catechism, i, ReadingTextTransformer)
                        if (i < catechism.questionCount - 1) {
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
    }
}

interface NavigateToScreens {
    fun selectTranslation()
    fun selectQuestion(selectedQuestion: Int)
    fun selectSunday(selectedQuestion: Int)
    fun found(conditions: SearchConditions)
    fun aboutTranslation()
    fun aboutCatechism()
    fun aboutApplication()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(navigateToScreens: NavigateToScreens, questionPosition: Int) {
    val scope = rememberCoroutineScope()

    val viewModel = viewModel<ReadingViewModel>(
        factory = ReadingViewModelFactory(LocalContext.current)
    )

    val searchDialogViewModel = viewModel<SearchDialogViewModel>()

    when (val event = searchDialogViewModel.searchDialogEvent.value) {
        is SearchDialogEvent.ApplySearchConditionsEvent -> {
            navigateToScreens.found(event.conditions)
            searchDialogViewModel.consumeEvent()
        }
        else -> {}
    }

    val aboutMenuExpanded = remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    @Composable
    fun filledBottomAppBar()  {
        BottomAppBar {
            CatechismNavigationButtons(viewModel, lazyListState, scope, navigateToScreens)
        }
    }

    @Composable
    fun emptyBottomAppBar() {}

    val configuration = LocalConfiguration.current
    val isWidthLarge = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE ||
            configuration.screenWidthDp > 840

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { SelectTranslationScreen(navigateToScreens::selectTranslation) }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Simple TopAppBar",
                            maxLines = 1,
                            overflow = TextOverflow.Visible
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Navigation Drawer")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { searchDialogViewModel.show() }
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                        if (isWidthLarge) {
                            CatechismNavigationButtons(viewModel, lazyListState, scope, navigateToScreens)
                        }
//                        Spacer(modifier = Modifier.weight(1f))
                        Box {
                            IconButton(
                                onClick = { aboutMenuExpanded.value = true }
                            ) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Show about menu")
                            }
                            DropdownMenu(
                                expanded = aboutMenuExpanded.value,
                                onDismissRequest = { aboutMenuExpanded.value = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(about_translation)) },
                                    onClick = navigateToScreens::aboutTranslation
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.about_catechism)) },
                                    onClick = navigateToScreens::aboutCatechism
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.about_application)) },
                                    onClick = navigateToScreens::aboutApplication
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (!isWidthLarge) {
                    filledBottomAppBar()
                } else {
                    emptyBottomAppBar()
                }
            }
        ) {
            if (searchDialogViewModel.showDialog.value) {
                SearchDialog(searchDialogViewModel)
            }
            ReadingArea(viewModel, questionPosition)
        }
    }
}