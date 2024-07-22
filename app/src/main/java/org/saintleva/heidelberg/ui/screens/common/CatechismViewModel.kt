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

package org.saintleva.heidelberg.ui.screens.common

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import org.saintleva.heidelberg.data.repository.CatechismState


abstract class CatechismViewModel(application: Application) : RepositoryViewModel(application) {

    protected val _catechismState = repository.catechismState
    val catechismState: MutableStateFlow<CatechismState> = _catechismState
}