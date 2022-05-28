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

import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory


class XmlTranslationException(message: String) : Exception(message)

fun loadCatechismFromXml(stream: InputStream): String {
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val document = documentBuilder.parse(stream)
    val root = document.documentElement
    val result = Catechism()
    for (i in 0 until root.childNodes.length) {
        val recordNode = root.childNodes.item(i)
        if (recordNode.attributes.getNamedItem("number").nodeValue.toInt() != i + 1)
            throw XmlTranslationException("Records have not been numerated correctly")
        val questionNode = recordNode.childNodes.item(0)
        if (questionNode.nodeName != "question")
            throw XmlTranslationException("There is must be 'question' node")
        val answerNode = recordNode.childNodes.item(1)
        if (answerNode.nodeName != "answer")
            throw XmlTranslationException("There is must be 'question' node")
        result.records.add(Record(questionNode.nodeValue, answerNode.nodeValue))
    }
}