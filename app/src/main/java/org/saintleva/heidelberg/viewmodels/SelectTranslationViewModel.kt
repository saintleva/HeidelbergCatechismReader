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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.saintleva.heidelberg.CatechismState
import org.saintleva.heidelberg.DataException
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.MainActivity
import org.saintleva.heidelberg.Repository
import org.saintleva.heidelberg.TranslationId
import org.saintleva.heidelberg.data.AllTranslations
import org.saintleva.heidelberg.data.TranslationListState


class SelectTranslationViewModel(localContext: Context) : ViewModel() {

    val context = localContext.applicationContext

    private val _currentTranslationId: MutableState<TranslationId> = Repository.currentTranslationId
    val currentTranslationId: State<TranslationId> = _currentTranslationId

//    private var _error = mutableStateOf<DataException?>(null)
//    val error: State<DataException?> = _error

    private val _allTranslations = Repository.manager.allTranslations
    val allTranslations: State<TranslationListState> = _allTranslations

    fun loadTranslationList() {
        viewModelScope.launch {
            try {
                Repository.manager.load(context)
            } catch (e: FileLoadingException) {
                _allTranslations.value = TranslationListState.Error(e)
            }
        }
    }

    fun isCurrent(id: String): Boolean =
        when (val translationId = currentTranslationId.value) {
            is TranslationId.None -> false
            is TranslationId.Id -> translationId.value == id
        }

    fun changeCurrentTranslationId(newId: String) {
        Repository.currentTranslationId.value = TranslationId.Id(newId)
        Repository.catechismState.value = CatechismState.SelectedToLoad
    }
}