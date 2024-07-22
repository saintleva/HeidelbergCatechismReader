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

import android.app.Application
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.ScrollPosition
import org.saintleva.heidelberg.data.loader.CatechismLoader
import org.saintleva.heidelberg.data.repository.CatechismState
import org.saintleva.heidelberg.data.repository.TranslationId
import org.saintleva.heidelberg.di.CatechismLoaderComponent
import org.saintleva.heidelberg.ui.screens.common.CatechismViewModel


class ReadingViewModel(application: Application) : CatechismViewModel(application) {

    lateinit var loader: CatechismLoader

    private var _scrollPosition = MutableStateFlow(ScrollPosition.DEFAULT)
    val scrollPosition: StateFlow<ScrollPosition> = _scrollPosition

    init {
        CatechismLoaderComponent.inject(this)
        _scrollPosition.value = repository.scrollPosition
    }

    private fun loadCatechism() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _catechismState.value = CatechismState.Loaded(
                    loader.load(
                        (repository.currentTranslationId.value as TranslationId.Id).value,
                        context = getApplication()
                    )
                )
            } catch (e: FileLoadingException) {
                _catechismState.value = CatechismState.Error(e)
            }
        }
    }

    fun tryToLoadSavedCatechism() {
        if (repository.currentTranslationId.value is TranslationId.Id
            && _catechismState.value == CatechismState.Never) {
            selectToLoad()
        }
    }

    fun isCatechismLoaded() = _catechismState.value is CatechismState.Loaded

    fun selectToLoad() {
        _catechismState.value = CatechismState.SelectedToLoad
        loadCatechism()
    }

    fun saveScrollPosition(state: LazyListState) {
        repository.scrollPosition = ScrollPosition(state.firstVisibleItemIndex,
            state.firstVisibleItemScrollOffset)
    }
}