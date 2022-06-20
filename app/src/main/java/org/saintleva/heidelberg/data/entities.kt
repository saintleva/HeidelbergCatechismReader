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

package org.saintleva.heidelberg.data


class Record(
    val question: String,
    val answer: String
)

class Translation(val description: String) {
    val partNames = mutableListOf<String>()
    val records = mutableListOf<Record>()
}

class Start(
    val part: Int?,
    val sunday: Int?
)

class Structure(
    val sundayCount: Int,
    val partStarts: List<Int>,
    val sundayStarts: List<Int>
) {
    val starts = mutableListOf<Start>()

    init {
        var partIndex = 0
        var sundayIndex = 0
        for (index in 0 until sundayCount) {
            var part: Int? = null
            if (partStarts[partIndex] == index) {
                part = partIndex
                partIndex++
            }
            var sunday: Int? = null
            if (sundayStarts[sundayIndex] == index) {
                sunday = sundayIndex
                sundayIndex++
            }
            starts.add(Start(part, sunday))
        }
    }
}