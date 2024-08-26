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

package org.saintleva.heidelberg.data.loader

import android.content.Context
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.FileType
import org.saintleva.heidelberg.data.loadStructureFromXml
import org.saintleva.heidelberg.data.loadTranslationFromXml
import org.saintleva.heidelberg.data.models.Catechism
import org.saintleva.heidelberg.data.models.Structure
import org.saintleva.heidelberg.data.models.Translation


class AssetsCatechismLoader(private val context: Context) : CatechismLoader {

    override var structure: Structure? = null

    override val loaded = mutableMapOf<String, Translation>()

    override suspend fun load(id: String): Catechism {
        val assetManager = context.assets
        if (id !in loaded) {
            try {
                val inputStream = assetManager.open("translations/$id.translation")
                loaded[id] = loadTranslationFromXml(inputStream)
            } catch (e: java.io.IOException) {
                throw FileLoadingException(FileType.TRANSLATION, e)
            }
        }
        if (structure == null) {
            try {
                val inputStream = assetManager.open("structure")
                structure = loadStructureFromXml(inputStream)
            } catch (e: java.io.IOException) {
                throw FileLoadingException(FileType.STRUCTURE, e)
            }
        }
        return Catechism(structure!!, loaded[id]!!)
    }
}