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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.android.inject
import org.saintleva.heidelberg.ui.screens.AppNavGraph
import org.saintleva.heidelberg.ui.theme.HeidelbergCatechismReaderTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by inject()

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.repository.loadPositionFromPrefs()

        setContent {
            val navController = rememberNavController()
            HeidelbergCatechismReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(navController)
                }
            }
        }
    }

    override fun onPause() {
        viewModel.repository.savePositionToPrefs()
        super.onPause()
    }

    override fun onStop() {
        viewModel.repository.savePositionToPrefs()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.repository.savePositionToPrefs()
        super.onSaveInstanceState(outState)
    }
}