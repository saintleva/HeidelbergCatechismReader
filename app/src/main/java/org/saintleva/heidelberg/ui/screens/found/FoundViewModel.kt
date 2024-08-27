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

package org.saintleva.heidelberg.ui.screens.found

import android.app.Application
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.saintleva.heidelberg.data.Found
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.data.findInCatechism
import org.saintleva.heidelberg.data.repository.Repository
import org.saintleva.heidelberg.ui.screens.common.LoadedCatechismViewModel


class FoundViewModel(repository: Repository) : LoadedCatechismViewModel(repository) {

    private var _previousConditions: SearchConditions?
        get() = repository.previousConditions
        set(value) {
            repository.previousConditions = value
        }

    private val _found: MutableStateFlow<Found?> = repository.found
    val found: StateFlow<Found?> = _found

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