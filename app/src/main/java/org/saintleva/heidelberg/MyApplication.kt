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

package org.saintleva.heidelberg

import android.app.Application
import android.util.Log
import org.saintleva.heidelberg.data.repository.Repository
import org.saintleva.heidelberg.di.RepositoryComponent

class MyApplication : Application() {

    lateinit var repository: Repository

    init {
        RepositoryComponent.inject(this)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("lifecycle", "MyApplication.ocCreate()") //TODO: remove it
        repository.loadPositionFromPrefs(this)
    }

    override fun onLowMemory() {
        Log.d("lifecycle", "MyApplication.onLowMemory()") //TODO: remove it
        repository.savePositionToPrefs(this)
        super.onLowMemory()
    }

    override fun onTerminate() {
        Log.d("lifecycle", "MyApplication.onTerminate()") //TODO: remove it
        repository.savePositionToPrefs(this)
        super.onTerminate()
    }
}