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

package org.saintleva.heidelberg

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import org.saintleva.heidelberg.data.*


sealed class CatechismState {
    object Never: CatechismState()
    object SelectedToLoad: CatechismState()
    data class Loaded(val catechism: Catechism): CatechismState()
    data class Error(val error: DataException): CatechismState()
}

//val CatechismState.sureLoaded: Catechism
//    get() {
//        if (this is CatechismState.Loaded) {
//            return catechism
//        } else {
//            throw
//        }
//    }

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

object Repository {

    val loader = Loader
    val manager = Manager

//    lateinit var translationListLoadingJob: Job

    val currentTranslationId = mutableStateOf<TranslationId>(TranslationId.None)

    val catechismState = mutableStateOf<CatechismState>(CatechismState.Never)

    var scrollPosition = ScrollPosition.DEFAULT

    fun savePositionToPrefs(context: Context) {
        saveToPrefs(
            Position(currentTranslationId.value, scrollPosition),
            context
        )
    }

    fun loadPositionFromPrefs(context: Context) {
        val position = loadFromPrefs(context)
        currentTranslationId.value = position.translation
        scrollPosition = position.scroll
    }

    var previousConditions: SearchConditions? = null

    val found = mutableStateOf<Found?>(null)
}