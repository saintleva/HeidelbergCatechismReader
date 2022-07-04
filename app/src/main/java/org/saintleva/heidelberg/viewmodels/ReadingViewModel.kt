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

package org.saintleva.heidelberg.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.saintleva.heidelberg.DataException
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.FileType
import org.saintleva.heidelberg.Repository
import org.saintleva.heidelberg.data.*


class ReadingViewModel(localContext: Context) : CatechismViewModel() {

    val context = localContext.applicationContext

    private var _error = mutableStateOf<DataException?>(null)
    val error: State<DataException?>
        get() = _error

//    val structure: MutableState<Structure?>
//        get() = Repository.structure
//    val translation: MutableState<Translation?>
//        get() = Repository.translation

    init {
        try {
            try {
                catechism.value = Catechism(
                    loadStructureFromXml(Repository.loadStructure(context)),
                    loadTranslationFromXml(Repository.loadTranslation(context))
                )
            }
            //TODO: Catch exceptions from both FileType
            catch (e: java.io.IOException) {
                throw FileLoadingException(FileType.TRANSLATION, e)
            }
        }
        catch (e: DataException) {
            _error.value = e
        }
    }

}