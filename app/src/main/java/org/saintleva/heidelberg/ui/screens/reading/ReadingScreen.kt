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

package org.saintleva.heidelberg.ui.screens.reading

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.R.string.about_translation
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.data.multiParagraphText
import org.saintleva.heidelberg.data.repository.CatechismState
import org.saintleva.heidelberg.ui.screens.common.DataAlert
import org.saintleva.heidelberg.ui.screens.common.RecordItem
import org.saintleva.heidelberg.ui.screens.common.TextTransformer
import org.saintleva.heidelberg.ui.screens.common.appBarModifier
import org.saintleva.heidelberg.ui.screens.searchdialog.SearchDialog
import org.saintleva.heidelberg.ui.screens.searchdialog.SearchDialogEvent
import org.saintleva.heidelberg.ui.screens.searchdialog.SearchDialogViewModel


 @Composable
fun ElementSpin(element: String, previousEnabled: Boolean, onPrevious: () -> Unit,
                nextEnabled: Boolean, onNext: () -> Unit, onSelect: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onPrevious, enabled = previousEnabled) {
            Icon(painterResource(R.drawable.caret_back), contentDescription = "Go to previous")
        }
        Text(text = element, modifier = Modifier.clickable { onSelect() }, fontSize = 13.sp)
        IconButton(onClick = onNext, enabled = nextEnabled) {
            Icon(painterResource(R.drawable.caret_forward), contentDescription = "Go to next")
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
                               navigateToScreens: NavigateToScreens) {
    val catechismState = viewModel.catechismState.collectAsStateWithLifecycle()

    if (catechismState.value !is CatechismState.Loaded) return

    val scope = rememberCoroutineScope()

    fun lastVisibleItemIndex(): Int {
        val info = lazyListState.layoutInfo.visibleItemsInfo
        if (info.isEmpty()) {
            return -1
        }
        return info[info.lastIndex].index
    }

    val catechism = (catechismState.value as CatechismState.Loaded).catechism
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
fun ReadingArea(viewModel: ReadingViewModel, innerPadding: PaddingValues,
                lazyListState: LazyListState) {

    //TODO: Do I need to use rememberSaveable() there?
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset,
                                      source: NestedScrollSource): Offset {
                viewModel.saveScrollPosition(lazyListState)
                return Offset.Zero
            }
        }
    }

    val errorAlerted = rememberSaveable { mutableStateOf(false) }

    val catechismState = viewModel.catechismState.collectAsStateWithLifecycle()

    viewModel.tryToLoadSavedCatechism()

    when (val state = catechismState.value) {
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
            viewModel.selectToLoad()
        }

        is CatechismState.Loaded -> {
            val catechism = state.catechism
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .nestedScroll(nestedScrollConnection),
                state = lazyListState
            ) {
                for (i in 0 until catechism.questionCount) {
                    val start = catechism.start(i)
                    item {
                        if (start.part != null) {
                            Text(
                                text = "${catechism.blockNames.part} ${romanNumber(start.part!!)}. " +
                                        catechism.partName(start.part!!),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 4.dp),
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                        if (start.sunday != null) {
                            Text(
                                text = "${catechism.blockNames.sunday} ${start.sunday!! + 1}",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 4.dp),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        RecordItem(catechism, i, ReadingTextTransformer)
                        if (i < catechism.questionCount - 1) {
                            Spacer(modifier = Modifier.padding(all = 4.dp))
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                thickness = 1.dp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(navigateToScreens: NavigateToScreens, questionPosition: Int) {

    val viewModel = koinViewModel<ReadingViewModel>()

    val searchDialogViewModel = koinViewModel<SearchDialogViewModel>()

    when (val event = searchDialogViewModel.searchDialogEvent.value) {
        is SearchDialogEvent.ApplySearchConditionsEvent -> {
            navigateToScreens.found(event.conditions)
            searchDialogViewModel.consumeEvent()
        }
        else -> {}
    }

    val aboutMenuExpanded = rememberSaveable { mutableStateOf(false) }

    val scrollPosition = viewModel.scrollPosition.collectAsStateWithLifecycle()
    val lazyListState =
        if (questionPosition == -1) {
            Log.d("anthony", "ReadingScreen(), questionPosition == $questionPosition")
            rememberLazyListState(
                scrollPosition.value.firstVisibleItemIndex,
                scrollPosition.value.firstVisibleItemScrollOffset
            )
        } else {
            Log.d("anthony", "ReadingScreen(), questionPosition == $questionPosition")
            rememberLazyListState(questionPosition)
        }

    @Composable
    fun filledBottomAppBar()  {
        BottomAppBar(
            modifier = appBarModifier,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            CatechismNavigationButtons(viewModel, lazyListState, navigateToScreens)
        }
    }

    @Composable
    fun emptyBottomAppBar() {}

    val configuration = LocalConfiguration.current
    val isWidthLarge = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE ||
            configuration.screenWidthDp > 840

    val catechismState = viewModel.catechismState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                modifier = appBarModifier,
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                navigationIcon = {
                    IconButton(
                        onClick = navigateToScreens::selectTranslation
                    ) {
                        Icon(
                            painterResource(R.drawable.language),
                            contentDescription = "Select translation",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { searchDialogViewModel.show() },
                        enabled = catechismState.value is CatechismState.Loaded
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    if (isWidthLarge) {
                        CatechismNavigationButtons(viewModel, lazyListState, navigateToScreens)
                    }
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
                                onClick = {
                                    navigateToScreens.aboutTranslation()
                                    aboutMenuExpanded.value = false },
                                enabled = viewModel.isCatechismLoaded()
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.about_catechism)) },
                                onClick = {
                                    navigateToScreens.aboutCatechism()
                                    aboutMenuExpanded.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.about_application)) },
                                onClick = {
                                    navigateToScreens.aboutApplication()
                                    aboutMenuExpanded.value = false
                                }
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
    ) { innerPadding ->
        if (searchDialogViewModel.showDialog.value) {
            SearchDialog(searchDialogViewModel)
        }
        ReadingArea(viewModel, innerPadding, lazyListState)
    }
}