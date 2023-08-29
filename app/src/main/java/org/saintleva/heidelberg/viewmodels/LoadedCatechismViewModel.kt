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

import org.saintleva.heidelberg.CatechismNotLoadedException
import org.saintleva.heidelberg.CatechismState
import org.saintleva.heidelberg.data.Catechism

open class LoadedCatechismViewModel : CatechismViewModel() {

    val catechism: Catechism
        get() {
            if (catechismState.value is CatechismState.Loaded) {
                return (catechismState.value as CatechismState.Loaded).catechism
            } else {
                throw CatechismNotLoadedException()
            }
        }

}