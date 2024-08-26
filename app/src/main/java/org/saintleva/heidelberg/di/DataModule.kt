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

package org.saintleva.heidelberg.di

import org.koin.dsl.module
import org.saintleva.heidelberg.data.loader.AssetsCatechismLoader
import org.saintleva.heidelberg.data.loader.CatechismLoader
import org.saintleva.heidelberg.data.manager.AssetsTranslationManager
import org.saintleva.heidelberg.data.manager.CombinedTranslationManager
import org.saintleva.heidelberg.data.repository.Repository
import org.saintleva.heidelberg.data.repository.RepositoryImpl


val dataModule = module {

    single<CatechismLoader> { AssetsCatechismLoader(get()) }
    single<CombinedTranslationManager> { AssetsTranslationManager(get()) }
    single<Repository> { RepositoryImpl(get())}
}