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
import android.util.Log
import org.saintleva.heidelberg.data.repository.TranslationId


class ScrollPosition(
    val firstVisibleItemIndex: Int,
    val firstVisibleItemScrollOffset: Int
) {
    companion object {
        val DEFAULT = ScrollPosition(0, 0)
    }
}

class Position(
    val translation: TranslationId,
    val scroll: ScrollPosition
)

const val STORAGE_STATE = "state"
const val TRANSLATION_ID = "translationId"
const val SCROLL_FIRST_VISIBLE_ITEM_INDEX = "scrollFirstVisibleItemIndex"
const val SCROLL_FIRST_VISIBLE_ITEM_SCROLL_OFFSET = "scrollFirstVisibleItemScrollOffset"

fun saveToPrefs(position: Position, context: Context) {
    val editor = context.getSharedPreferences(STORAGE_STATE, Context.MODE_PRIVATE).edit().apply {
        putString(
            TRANSLATION_ID,
            when (position.translation) {
                TranslationId.None -> ""
                is TranslationId.Id -> position.translation.value
            }
        )
        putInt(SCROLL_FIRST_VISIBLE_ITEM_INDEX, position.scroll.firstVisibleItemIndex)
        putInt(SCROLL_FIRST_VISIBLE_ITEM_SCROLL_OFFSET, position.scroll.firstVisibleItemScrollOffset)
        apply()
    }
}

fun loadFromPrefs(context: Context): Position {
    val prefs = context.getSharedPreferences(STORAGE_STATE, Context.MODE_PRIVATE)
    val translationString = prefs.getString(TRANSLATION_ID, "")
    Log.d("lifecycle", "translationString == $translationString")
    return Position(
        if (translationString.isNullOrEmpty())
            TranslationId.None
        else
            TranslationId.Id(translationString!!),
        ScrollPosition(
            prefs.getInt(SCROLL_FIRST_VISIBLE_ITEM_INDEX, 0),
            prefs.getInt(SCROLL_FIRST_VISIBLE_ITEM_SCROLL_OFFSET, 0)
        )
    )
}

