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

package org.saintleva.heidelberg.ui.screens.selecttranslation

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.FileType
import org.saintleva.heidelberg.data.manager.CombinedTranslationListState
import org.saintleva.heidelberg.data.manager.CombinedTranslationManager
import org.saintleva.heidelberg.data.manager.TranslationListState
import org.saintleva.heidelberg.data.repository.CatechismState
import org.saintleva.heidelberg.data.repository.TranslationId
import org.saintleva.heidelberg.di.CombinedTranslationManagerComponent
import org.saintleva.heidelberg.ui.screens.common.RepositoryViewModel


class SelectTranslationViewModel(application: Application) : RepositoryViewModel(application) {

    lateinit var manager: CombinedTranslationManager

    init {
        CombinedTranslationManagerComponent.inject(this)
    }

    private val _currentTranslationId: MutableState<TranslationId> = repository.currentTranslationId
    val currentTranslationId: State<TranslationId> = _currentTranslationId

    private val _combinedTranslations = manager.combinedTranslations
    val combinedTranslations: State<CombinedTranslationListState> = _combinedTranslations

    fun loadTranslationList() {
        viewModelScope.launch {
            try {
                if (manager.allTranslations.value == TranslationListState.None) {
                    manager.load(getApplication())
                    manager.combineTranslations()
                }
            } catch (e: FileLoadingException) {
                _combinedTranslations.value =
                    CombinedTranslationListState.Error(FileLoadingException(FileType.LIST, e))
            }
        }
    }

    fun isCurrent(id: String): Boolean =
        when (val translationId = currentTranslationId.value) {
            is TranslationId.None -> false
            is TranslationId.Id -> translationId.value == id
        }

    fun changeCurrentTranslationId(newId: String) {
        repository.currentTranslationId.value = TranslationId.Id(newId)
        repository.catechismState.value = CatechismState.SelectedToLoad
    }
}