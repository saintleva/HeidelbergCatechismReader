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

package org.saintleva.heidelberg.data.manager

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import okio.buffer
import okio.source
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.FileType
import org.saintleva.heidelberg.NoLanguageSpecifiedException
import org.saintleva.heidelberg.data.models.AllTranslations
import org.saintleva.heidelberg.data.models.TranslationMetadata


class AssetsTranslationManager(private val context: Context) : StandardCombinedTranslationManager {

    override val combinedTranslations =
        MutableStateFlow<CombinedTranslationListState>(CombinedTranslationListState.None)

    override val allTranslations = MutableStateFlow<TranslationListState>(TranslationListState.None)

    override suspend fun load() {
        val moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter<AllTranslations>(
            Types.newParameterizedType(
                MutableMap::class.java,
                String::class.java,
                TranslationMetadata::class.java
            )
        )

        val assetManager = context.assets
        try {
            val inputStream = assetManager.open("list")
            val bufferedSource = inputStream.source().buffer()
            allTranslations.value =
                TranslationListState.Loaded(adapter.fromJson(bufferedSource)!!)
        } catch (e: java.io.IOException) {
            throw FileLoadingException(FileType.LIST, e)
        } catch (e: NoLanguageSpecifiedException) {
            throw FileLoadingException(FileType.LIST, e)
        }
    }
}