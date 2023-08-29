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

package org.saintleva.heidelberg.data

import android.content.Context
import android.util.Log
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import okio.buffer
import okio.source
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.FileType
import org.saintleva.heidelberg.TranslationIdIsEmptyStringException

object Manager {

    var allTranslations: AllTranslations = emptyMap()

    //TODO: make it suspend fun
    suspend fun load(context: Context) {
        val moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
//        val adapter = moshi.adapter<AllTranslations>(Map::class.java)
        val adapter = moshi.adapter<AllTranslations>(
            Types.newParameterizedType(
                MutableMap::class.java,
                String::class.java,
                TranslationMetadata::class.java
            )
        )

        val assetManager = context.assets
        val inputStream = assetManager.open("list")
        try {
            val bufferedSource = inputStream.source().buffer()
            delay(3000)
            allTranslations = adapter.fromJson(bufferedSource)!! //TODO: remove "!!"
            for (id in allTranslations.keys) {
                Log.d("compose", "id == $id")
                Log.d("compose", "allTranslations[$id].name == ${allTranslations[id]?.name}")
                if (id.isEmpty()) {
                    throw TranslationIdIsEmptyStringException()
                }
            }
        } catch (e: java.io.IOException) {
            throw FileLoadingException(FileType.LIST, e)
        }
    }
}