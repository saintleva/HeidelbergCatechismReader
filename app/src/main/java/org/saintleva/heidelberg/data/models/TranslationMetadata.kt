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

package org.saintleva.heidelberg.data.models

import androidx.annotation.Keep
import org.saintleva.heidelberg.NoLanguageSpecifiedException

@Keep
class TranslationMetadata(
    val name: String?,
    val englishName: String?,
    val language: String,
    val isOriginal: Boolean
) {
    init {
        if (name == null && englishName == null) {
            throw NoLanguageSpecifiedException()
        }
    }

    val nameToUse = name ?: englishName
}

typealias AllTranslations = Map<String, TranslationMetadata>