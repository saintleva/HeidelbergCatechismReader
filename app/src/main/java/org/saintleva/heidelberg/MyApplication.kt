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

package org.saintleva.heidelberg

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import org.saintleva.heidelberg.data.repository.Repository
import org.saintleva.heidelberg.di.appModule
import org.saintleva.heidelberg.di.dataModule


class MyApplication : Application() {

    val repository: Repository by inject(Repository::class.java)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(dataModule, appModule))
        }

        repository.loadPositionFromPrefs()
    }

    override fun onLowMemory() {
        repository.savePositionToPrefs()
        super.onLowMemory()
    }

    override fun onTerminate() {
        repository.savePositionToPrefs()
        super.onTerminate()
    }
}