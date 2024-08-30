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

package org.saintleva.heidelberg.data

import androidx.compose.ui.text.style.TextIndent
import org.saintleva.heidelberg.HeidelbergException
import org.saintleva.heidelberg.data.models.Catechism


class EmptySubstringException : HeidelbergException()

data class SearchConditions(
    val text: String,
    val searchInQuestions: Boolean = true,
    val searchInAnswers: Boolean = true,
    val matchCase: Boolean = false
)

fun findSubstring(substring: String, text: CharSequence, matchCase: Boolean): List<IntRange> {
    if (substring.isEmpty())
        throw EmptySubstringException()
    val result = mutableListOf<IntRange>()
    var start = 0
    while (true) {
        val index = text.indexOf(substring, start, !matchCase)
        if (index != -1) {
            result.add(IntRange(index, index + substring.length - 1))
            start = index + substring.length
        } else
            return result
    }
}

class FoundInRecord(
    val inQuestion: List<IntRange>,
    val inAnswer: List<IntRange>
) {
    fun notFound(): Boolean {
        return inQuestion.isEmpty() and inAnswer.isEmpty()
    }
}

typealias Found = Map<Int, FoundInRecord>

fun findInCatechism(catechism: Catechism, conditions: SearchConditions, indent: TextIndent): Found {
    val result = mutableMapOf<Int, FoundInRecord>()
    for (i in 0 until catechism.questionCount) {
        val foundInRecord = FoundInRecord(
            if (conditions.searchInQuestions)
                findSubstring(
                    conditions.text,
                    multiParagraphText(catechism.record(i).question, indent),
                    conditions.matchCase
                )
            else
                emptyList<IntRange>(),
            if (conditions.searchInAnswers)
                findSubstring(
                    conditions.text,
                    multiParagraphText(catechism.record(i).answer, indent),
                    conditions.matchCase
                )
            else
                emptyList<IntRange>()
        )
        if (!foundInRecord.notFound())
            result[i] = foundInRecord
    }
    return result
}