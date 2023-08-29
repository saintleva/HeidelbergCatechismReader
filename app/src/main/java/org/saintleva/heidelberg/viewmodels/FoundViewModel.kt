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

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.Repository
import org.saintleva.heidelberg.data.Found
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.data.findInCatechism


class FoundViewModel : LoadedCatechismViewModel() {

    private var _previousConditions: SearchConditions?
        get() = Repository.previousConditions
        set(value) {
            Repository.previousConditions = value
        }

    private val _found: MutableState<Found?>
        get() = Repository.found

    val found: State<Found?>
        get() = _found

    fun find(conditions: SearchConditions) {
        if (conditions != _previousConditions) {
            _found.value = null
            _previousConditions = conditions
            viewModelScope.launch {
                _found.value =
                    findInCatechism(catechism, conditions, TextIndent(firstLine = 12.sp))
            }
        }
    }
}