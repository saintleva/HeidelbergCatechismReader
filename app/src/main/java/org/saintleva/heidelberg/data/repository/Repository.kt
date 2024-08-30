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

package org.saintleva.heidelberg.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import org.saintleva.heidelberg.DataException
import org.saintleva.heidelberg.ScrollPosition
import org.saintleva.heidelberg.TranslationIdIsEmptyStringException
import org.saintleva.heidelberg.data.Found
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.data.models.Catechism


sealed class CatechismState {
    object Never: CatechismState()
    object SelectedToLoad: CatechismState()
    data class Loaded(val catechism: Catechism): CatechismState()
    data class Error(val error: DataException): CatechismState()
}

sealed class TranslationId {
    object None: TranslationId()
    data class Id(val value: String): TranslationId() {
        init {
            if (value.isEmpty()) {
                throw TranslationIdIsEmptyStringException()
            }
        }
    }
}

interface Repository {

    val currentTranslationId: MutableStateFlow<TranslationId>

    val catechismState: MutableStateFlow<CatechismState>

    var scrollPosition: ScrollPosition

    fun savePositionToPrefs()
    fun loadPositionFromPrefs()

    var previousConditions: SearchConditions?

    val found: MutableStateFlow<Found?>
}