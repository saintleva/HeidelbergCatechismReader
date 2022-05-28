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


object Repository {

    //TODO: remove it
    //val names = hashMapOf<String, String>()
    val names = hashMapOf(
        "Вязовский2021" to "Издательство «Евангелие и Реформация»\nЕвгений Устинович\n2021 год",
        "noname1" to "noname1",
        "Мамсуров2006" to "И.В. Мамсуров\nРедакторы: В.М. Лоцманов, А.С. Джанумов\n2006 год",
        "украинский" to "Украинский перевод с https://forms.reformed.org.ua/",
        "aaa" to "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        "bbb" to "bbb bbbb bbbb bbb bbbbbbbb bbbbbbbb bbbbbb bbb bbbbb bbbb b bb bbbbbbbbb"
    )

    var currentTranslationId = mutableStateOf<String?>(null)

    val currentTraslationName: String?
        get() {
            return if (currentTranslationId.value != null)
                names[currentTranslationId.value]
            else
                null
        }

    val transtation = mutableStateOf("")

    fun loadTranstlation(context: Context) {
        val assetManager = context.assets
        transtation.value = assetManager
            .open("translations/1temp.translation")
            .bufferedReader()
            .readText()
    }
}