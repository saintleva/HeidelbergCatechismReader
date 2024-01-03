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

package org.saintleva.heidelberg.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.saintleva.heidelberg.DataException
import org.saintleva.heidelberg.DataFormatException
import org.saintleva.heidelberg.FileLoadingException
import org.saintleva.heidelberg.FileType
import org.saintleva.heidelberg.R


@Composable
fun DataAlert(exception: DataException, onClose: () -> Unit) {

    val errorMessage = when(exception) {
        is FileLoadingException -> stringResource(R.string.error_while_loading_from_file_with)
        is DataFormatException -> stringResource(R.string.invalid_format_in_the_file_with)
        else -> stringResource(R.string.another_error_concerned_with)
    } + " " +
    when(exception.fileType) {
        FileType.TRANSLATION -> stringResource(R.string.catechism_translation)
        FileType.STRUCTURE -> stringResource(R.string.catechism_structure)
        FileType.LIST -> stringResource(R.string.translation_list)
    } + ": ${exception.message}"

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(stringResource(R.string.error)) },
        text = { Text(errorMessage) },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text(text = "OK", fontSize = 22.sp)
            }
        },
        dismissButton = null
    )
}
