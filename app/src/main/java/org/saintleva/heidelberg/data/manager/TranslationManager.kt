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

package org.saintleva.heidelberg.data.manager

import android.content.Context
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow
import org.saintleva.heidelberg.DataException
import org.saintleva.heidelberg.data.models.AllTranslations
import org.saintleva.heidelberg.data.models.TranslationMetadata
import java.util.SortedMap


sealed class TranslationListState {
    object None: TranslationListState()
    data class Loaded(val all: AllTranslations): TranslationListState()
    data class Error(val error: DataException): TranslationListState()
}

interface TranslationManager {
    val allTranslations: MutableStateFlow<TranslationListState>
    suspend fun load(context: Context)
}

class ExtendedMetadata(
    val id: String,
    val data: TranslationMetadata
)

typealias CombinedTranslations = SortedMap<String, MutableSet<ExtendedMetadata>>

sealed class CombinedTranslationListState {
    object None: CombinedTranslationListState()
    data class Loaded(val combined: CombinedTranslations): CombinedTranslationListState()
    data class Error(val error: DataException): CombinedTranslationListState()
}

interface CombinedTranslationManager: TranslationManager {
    val combinedTranslations: MutableStateFlow<CombinedTranslationListState>
    fun combineTranslations()
}