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

import org.jdom2.Element
import org.jdom2.input.DOMBuilder
import org.saintleva.heidelberg.DataFormatException
import org.saintleva.heidelberg.FileType
import java.io.InputStream
import java.lang.StringBuilder
import javax.xml.parsers.DocumentBuilderFactory


fun trimTextInXml(text: CharSequence): String {
    val result = StringBuilder()
    val lines = text.lines()
    for (i in 1 until lines.size - 1) {
        result.append(lines[i].trimStart())
        if (i < lines.size - 2)
            result.append("\n")
    }
    return result.toString()
}

fun getRootElement(stream: InputStream): Element {
    //TODO: Migrate to org.jdom2.input.SAXBuilder()
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val document = DOMBuilder().build(documentBuilder.parse(stream))
    return document.rootElement
}

fun loadTranslationFromXml(stream: InputStream): Translation {
    try {
        val rootNode = getRootElement(stream)
        val result = Translation(trimTextInXml(rootNode.getChild("description").text))
        for (partNode in rootNode.getChild("partNames").getChildren("part"))
            result.partNames.add(partNode.text)
        for (recordNode in rootNode.getChild("data").getChildren("record"))
            result.records.add(
                Record(
                    trimTextInXml(recordNode.getChild("question").text),
                    trimTextInXml(recordNode.getChild("answer").text)
                )
            )
        return result
    }
    catch (e: Exception) {
        throw DataFormatException(FileType.TRANSLATION, e)
    }
}