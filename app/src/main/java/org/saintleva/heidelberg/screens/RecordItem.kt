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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.data.Catechism
import org.saintleva.heidelberg.ui.multiParagraphText


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CopyableSubItem(text: AnnotatedString, style: TextStyle) {
    val clipboardManager = LocalClipboardManager.current
    Box {
        val expanded = remember { mutableStateOf(false) }
        Text(
            text = text,
            modifier = Modifier
                .combinedClickable(onLongClick = { expanded.value = true }, onClick = {})
                .padding(all = 4.dp),
            style = style
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.copy)) },
                onClick = {
                    clipboardManager.setText(text)
                    expanded.value = false
                }
            )
        }
    }
}

interface TextTransformer {
    fun transformQuestion(source: String, index: Int): AnnotatedString
    fun transformAnswer(source: String, index: Int): AnnotatedString
}

@Composable
fun RecordItem(catechism: Catechism, index: Int, transformer: TextTransformer) {
    val record = catechism.record(index)
    Column {
        Text(
            text = "${catechism.blockNames.question} ${index + 1}",
            modifier = Modifier.padding(all = 4.dp),
            style = MaterialTheme.typography.titleLarge
        )
        CopyableSubItem(
            text = transformer.transformQuestion(record.question, index),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        CopyableSubItem(
            text = transformer.transformAnswer(record.answer, index),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
