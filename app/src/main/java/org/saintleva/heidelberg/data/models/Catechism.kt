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

@file:Suppress("RemoveExplicitTypeArguments", "RemoveExplicitTypeArguments")

package org.saintleva.heidelberg.data.models

import org.saintleva.heidelberg.HeidelbergException


class BlockNames(
    val part: String,
    val sunday: String,
    val question: String
)

class Record(
    val question: String,
    val answer: String
)

class Translation(
    val description: String,
    val blockNames: BlockNames
) {
    val partNames = mutableListOf<String>()
    val records = mutableListOf<Record>()
}

class Start(
    var part: Int? = null,
    var sunday: Int? = null
)

class Structure(
    val questionCount: Int,
    private val partStarts: List<Int>,
    val sundayStarts: List<Int>
) {
    val starts = List<Start>(questionCount) { Start() }

    init {
        var partIndex = 0
        var sundayIndex = 0
        for (index in 0 until questionCount)
            if (partStarts[partIndex] == index) {
                starts[index].part = partIndex
                partIndex++
                if (partIndex >= partStarts.size)
                    break
            }
        for (index in 0 until questionCount)
            if (sundayStarts[sundayIndex] == index) {
                starts[index].sunday = sundayIndex
                sundayIndex++
                if (sundayIndex >= sundayStarts.size)
                    break
            }
    }
}

abstract class CatechismException : HeidelbergException()

class QuestionCountMismatchException: CatechismException()

abstract class BadIndexException: CatechismException()

class BadQuestionIndexException: BadIndexException()

class BadSundayIndexException: BadIndexException()

class BadPartIndexException: BadIndexException()

@Suppress("RemoveExplicitTypeArguments")
class Catechism(
    private val _structure: Structure,
    private val _translation: Translation
) {
    val description = _translation.description

    val questionCount: Int
        get() = _translation.records.size

    val sundayCount: Int
        get() = _structure.sundayStarts.size

    fun sundayStart(sundayIndex: Int): Int {
        try {
            return _structure.sundayStarts[sundayIndex]
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw BadSundayIndexException()
        }
    }

    fun sundayEnd(sundayIndex: Int): Int {
        if (sundayIndex < 0 || sundayIndex >= sundayCount)
            throw BadSundayIndexException()
        return if (sundayIndex + 1 < sundayCount)
            _structure.sundayStarts[sundayIndex + 1]
        else
            questionCount
    }

    val blockNames: BlockNames
        get() = _translation.blockNames

    fun record(questionIndex: Int): Record {
        try {
            return _translation.records[questionIndex]
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw BadQuestionIndexException()
        }
    }

    fun partName(partIndex: Int): String {
        try {
            return _translation.partNames[partIndex]
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw BadPartIndexException()
        }
    }

    fun start(questionIndex: Int): Start {
        try {
            return _structure.starts[questionIndex]
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw BadQuestionIndexException()
        }
    }

    private val _sundayOfQuestion = MutableList<Int>(questionCount) { 0 }

    fun sundayOfQuestion(questionIndex: Int): Int {
        try {
            return _sundayOfQuestion[questionIndex]
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw BadQuestionIndexException()
        }
    }

    init {
        if (_translation.records.size != _structure.questionCount)
            throw QuestionCountMismatchException()

        for (sunday in 0 until sundayCount - 1)
            for (question in _structure.sundayStarts[sunday] until _structure.sundayStarts[sunday + 1])
                _sundayOfQuestion[question] = sunday
        for (question in _structure.sundayStarts[sundayCount - 1] until questionCount)
            _sundayOfQuestion[question] = sundayCount - 1
    }
}