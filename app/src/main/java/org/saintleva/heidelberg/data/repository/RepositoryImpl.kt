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

package org.saintleva.heidelberg.data.repository

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import org.saintleva.heidelberg.Position
import org.saintleva.heidelberg.ScrollPosition
import org.saintleva.heidelberg.data.Found
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.loadFromPrefs
import org.saintleva.heidelberg.saveToPrefs


object RepositoryImpl : Repository {

    override val currentTranslationId = MutableStateFlow<TranslationId>(TranslationId.None)
//        get() {
//            Log.d("lifecycle", "repository.currentTranslationId == ${currentTranslationId.value}")
//            return currentTranslationId
//        }

    override val catechismState = MutableStateFlow<CatechismState>(CatechismState.Never)
//        get() {
//            when (currentTranslationId.value) {
//                TranslationId.None -> MutableStateFlow<CatechismState>(CatechismState.Never)
//                is TranslationId.Id -> MutableStateFlow<CatechismState>(CatechismState.Loaded())
//            }
//
//        }

    override var scrollPosition = ScrollPosition.DEFAULT
 
    override fun savePositionToPrefs(context: Context) {
        saveToPrefs(
            Position(currentTranslationId.value, scrollPosition),
            context
        )
    }

    override fun loadPositionFromPrefs(context: Context) {
        val position = loadFromPrefs(context)
        if (position.translation is TranslationId.Id) {
            Log.d("lifecycle", "translation == ${position.translation.value}")
        } else {
            Log.d("lifecycle", "translation is None")
        }
        currentTranslationId.value = position.translation
        scrollPosition = position.scroll
    }

    override var previousConditions: SearchConditions? = null

    override val found = MutableStateFlow<Found?>(null)
}