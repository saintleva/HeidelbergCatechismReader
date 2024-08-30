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

package org.saintleva.heidelberg.ui.screens.found

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.saintleva.heidelberg.R
import org.saintleva.heidelberg.data.Found
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.data.multiParagraphText
import org.saintleva.heidelberg.ui.screens.common.RecordItem
import org.saintleva.heidelberg.ui.screens.common.TextTransformer


class FoundTransformer(
    val found: Found,
    private val backgroundColor: Color
) : TextTransformer {

    private fun transformSomething(source: String, foundItem: List<IntRange>): AnnotatedString {
        return buildAnnotatedString {
            append(multiParagraphText(source, TextIndent(firstLine = 12.sp)))
            for (i in foundItem.indices)
                addStyle(
                    style = SpanStyle(background = backgroundColor),
                    start = foundItem[i].start,
                    end = foundItem[i].endInclusive + 1
                )
        }

    }

    override fun transformQuestion(source: String, index: Int): AnnotatedString {
        return transformSomething(source, found[index]!!.inQuestion)
    }

    override fun transformAnswer(source: String, index: Int): AnnotatedString {
        return transformSomething(source, found[index]!!.inAnswer)
    }
}

@Composable
fun FoundScreen(conditions: SearchConditions, innerPadding: PaddingValues) {

    val viewModel = koinViewModel<FoundViewModel>()
    viewModel.find(conditions)
    val found = viewModel.found.collectAsStateWithLifecycle()

    if (found.value == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        val found = found.value!!
        if (found.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val id =
                    if (conditions.searchInQuestions)
                        if (conditions.searchInAnswers)
                            R.string.not_found_in_questions_and_answers
                        else
                            R.string.not_found_in_questions
                    else
                        R.string.not_found_in_answers

                Text(
                    stringResource(id).format(conditions.text),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                for (index in found.keys) {
                    item {
                        RecordItem(
                            viewModel.catechism,
                            index,
                            FoundTransformer(found, MaterialTheme.colorScheme.primary)
                        )
                        if (index < found.keys.maxOrNull()!!) {
                            Spacer(modifier = Modifier.padding(all = 4.dp))
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}