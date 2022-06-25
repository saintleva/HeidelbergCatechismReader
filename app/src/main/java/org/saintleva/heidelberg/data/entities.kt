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
    var part: Int? = null,
    var sunday: Int? = null
)

class Structure(
    val sundayCount: Int,
    val partStarts: List<Int>,
    val sundayStarts: List<Int>
) {
    val starts = Array<Start>(sundayCount) { Start() }

    init {
        var partIndex = 0
        var sundayIndex = 0
        for (index in 0 until sundayCount)
            if (partStarts[partIndex] == index) {
                starts[index].part = partIndex
                partIndex++
                if (partIndex >= partStarts.size)
                    break
            }
        for (index in 0 until sundayCount)
            if (sundayStarts[sundayIndex] == index) {
                starts[index].sunday = sundayIndex
                sundayIndex++
                if (sundayIndex >= sundayStarts.size)
                    break
            }
    }
}