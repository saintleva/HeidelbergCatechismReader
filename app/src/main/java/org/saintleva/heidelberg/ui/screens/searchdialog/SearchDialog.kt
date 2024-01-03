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

package org.saintleva.heidelberg.ui.screens.searchdialog

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import org.saintleva.heidelberg.R


@ExperimentalMaterial3Api
@Composable
//TODO: Use "innerPadding" parameter of remove it
fun SearchDialog(viewModel: SearchDialogViewModel, innerPadding: PaddingValues) {
    AlertDialog(
        modifier = Modifier.padding(horizontal = 20.dp),
        onDismissRequest = viewModel::onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = {
            Column {
                Text(
                    text = stringResource(R.string.search),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 12.dp),
                    fontSize = 22.sp
                )
                OutlinedTextField(
                    value = viewModel.text.value,
                    onValueChange = { viewModel.onTextChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp),
                    label = { Text(stringResource(R.string.enter_search_text)) },
                    isError = viewModel.text.value.isEmpty()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.searchInQuestions.value,
                        onCheckedChange = { viewModel.onSearchInQuestionsChange(it) }
                    )
                    Text(stringResource(R.string.search_in_questions))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.searchInAnswers.value,
                        onCheckedChange = { viewModel.onSearchInAnswerChange(it) }
                    )
                    Text(stringResource(R.string.search_in_answers))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.matchCase.value,
                        onCheckedChange = { viewModel.onMatchCaseChange(it) }
                    )
                    Text(stringResource(R.string.match_case))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = viewModel::onOK,
                enabled = viewModel.maySearch()
            ) {
                Text(text = stringResource(R.string.ok), fontSize = 22.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = viewModel::onCancel) {
                Text(text = stringResource(R.string.cancel), fontSize = 22.sp)
            }
        }
    )
}