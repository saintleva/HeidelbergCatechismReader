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
import androidx.compose.runtime.mutableStateOf
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import okio.buffer
import okio.source
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.FileType
import org.saintleva.heidelberg.NoLanguageSpecifiedException
import org.saintleva.heidelberg.data.models.AllTranslations
import org.saintleva.heidelberg.data.models.TranslationMetadata


object AssetsTranslationManager : StandardCombinedTranslationManager {

    override var combinedTranslations =
        mutableStateOf<CombinedTranslationListState>(CombinedTranslationListState.None)

    override var allTranslations = mutableStateOf<TranslationListState>(TranslationListState.None)

    //@OptIn(kotlin.ExperimentalStdlibApi::class)
    override suspend fun load(context: Context) {
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
        val inputStream = assetManager.open("list")
        try {
            val bufferedSource = inputStream.source().buffer()
            delay(1500)
            allTranslations.value =
                TranslationListState.Loaded(adapter.fromJson(bufferedSource)!!) //TODO: remove "!!"
        } catch (e: java.io.IOException) {
            throw FileLoadingException(FileType.LIST, e)
        } catch (e: NoLanguageSpecifiedException) {
            throw FileLoadingException(FileType.LIST, e)
        }
    }
}