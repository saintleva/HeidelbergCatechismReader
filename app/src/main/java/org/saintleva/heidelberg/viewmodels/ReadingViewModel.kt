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

package org.saintleva.heidelberg.viewmodels

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.*
import org.saintleva.heidelberg.data.*


class ReadingViewModel(localContext: Context) : CatechismViewModel() {

    val context = localContext.applicationContext

    private var _scrollPosition = mutableStateOf(ScrollPosition.DEFAULT)
    val scrollPosition: State<ScrollPosition> = _scrollPosition

    init {
        _scrollPosition.value = Repository.scrollPosition
    }

    private fun loadCatechism() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _catechismState.value = CatechismState.Loaded(
                    Repository.loader.load(
                        (Repository.currentTranslationId.value as TranslationId.Id).value,
                        context = context
                    )
                )
            } catch (e: FileLoadingException) {
                _catechismState.value = CatechismState.Error(e)
            }
        }
    }

    fun selectToLoad() {
        _catechismState.value = CatechismState.SelectedToLoad
        loadCatechism()
    }

    fun saveScrollPosition(state: LazyListState) {
        Repository.scrollPosition = ScrollPosition(state.firstVisibleItemIndex,
            state.firstVisibleItemScrollOffset)
    }
}