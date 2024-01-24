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

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow


interface StandardCombinedTranslationManager : CombinedTranslationManager {

    override val combinedTranslations: MutableStateFlow<CombinedTranslationListState>

    override fun combineTranslations() {
        val result = sortedMapOf<String, MutableSet<ExtendedMetadata>>()
        val loaded = (allTranslations.value as TranslationListState.Loaded)
        val source = loaded.all
        for (id in source.keys) {
            val lang = source[id]!!.language
            val item = ExtendedMetadata(id, source[id]!!)
            if (lang in result.keys) {
                result[lang]!!.add(item)
            } else {
                result[lang] = mutableSetOf(item)
            }
        }
        combinedTranslations.value = CombinedTranslationListState.Loaded(result)
        Log.d("anthony", "StandardCombinedTranslationManager: combinedTranslations.value == ${combinedTranslations.value}")
    }
}