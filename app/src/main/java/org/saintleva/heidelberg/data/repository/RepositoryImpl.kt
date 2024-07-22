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

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.saintleva.heidelberg.Position
import org.saintleva.heidelberg.ScrollPosition
import org.saintleva.heidelberg.data.Found
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.loadFromPrefs
import org.saintleva.heidelberg.saveToPrefs


object RepositoryImpl : Repository {

    override val currentTranslationId = MutableStateFlow<TranslationId>(TranslationId.None)

    override val catechismState = MutableStateFlow<CatechismState>(CatechismState.Never)

    override var scrollPosition = ScrollPosition.DEFAULT
 
    override fun savePositionToPrefs(context: Context) {
        saveToPrefs(
            Position(currentTranslationId.value, scrollPosition),
            context
        )
    }

    override fun loadPositionFromPrefs(context: Context) {
        val position = loadFromPrefs(context)
        currentTranslationId.value = position.translation
        scrollPosition = position.scroll
    }

    override var previousConditions: SearchConditions? = null

    override val found = MutableStateFlow<Found?>(null)
}