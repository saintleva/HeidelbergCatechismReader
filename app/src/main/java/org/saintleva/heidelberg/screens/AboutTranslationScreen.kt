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

package org.saintleva.heidelberg.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.viewmodels.LoadedCatechismViewModel


@Composable
fun AboutTranslationScreen(innerPadding: PaddingValues) {
    val viewModel = viewModel<LoadedCatechismViewModel>()
    if (viewModel.catechism != null) {
        Text(
            text = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
//            text = viewModel.catechism.description,
            modifier = Modifier.padding(innerPadding)
        )
    } else {
        Text(
            stringResource(R.string.no_translation_selected),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}