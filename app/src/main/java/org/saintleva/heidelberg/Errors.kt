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


abstract class HeidelbergException : Exception()

abstract class HeidelbergCausedException(cause: Exception) : Exception(cause)

enum class FileType { TRANSLATION, STRUCTURE, LIST }

abstract class DataException(val fileType: FileType, cause: Exception) : HeidelbergCausedException(cause)

class FileLoadingException(fileType: FileType, cause: Exception) : DataException(fileType, cause)

class DataFormatException(fileType: FileType, cause: Exception) : DataException(fileType, cause)

class TranslationIdIsEmptyStringException: HeidelbergException()

class NoLanguageSpecifiedException: HeidelbergException()

class CatechismNotLoadedException: HeidelbergException()