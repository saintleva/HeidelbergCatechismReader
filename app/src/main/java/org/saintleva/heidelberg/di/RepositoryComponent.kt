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

package org.saintleva.heidelberg.di

import org.saintleva.heidelberg.MyApplication
import org.saintleva.heidelberg.data.repository.RepositoryImpl
import org.saintleva.heidelberg.ui.screens.common.RepositoryViewModel


object RepositoryComponent {

    fun inject(repositoryViewModel: RepositoryViewModel) {
        repositoryViewModel.repository = RepositoryImpl
    }

    fun inject(myApplication: MyApplication) {
        myApplication.repository = RepositoryImpl
    }
}