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

import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.JDOMFactory
import org.jdom2.input.DOMBuilder
import org.jdom2.input.SAXBuilder
import org.saintleva.heidelberg.TranslationFormatException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import javax.xml.parsers.DocumentBuilderFactory


//abstract class XmlTranslationException(message: String) : Exception(message)
//class RecodsAreNumberedIncorrectlyException :
//    XmlTranslationException("Records are numbered incorrectly")
//class SingleChildNotFoundException :
//    XmlTranslationException("None or several child with given name found")

fun trimTextInXml(text: CharSequence): String {
    val result = StringBuilder()
    val lines = text.lines()
    for (i in 1 until lines.size - 1) {
        result.append(lines[i].trimStart())
        if (i != lines.size - 2)
            result.append(" ")
    }
    return result.toString()
}

fun getRootElement(stream: InputStream): Element {
    //TODO: Migrate to org.jdom2.input.SAXBuilder()
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val document = DOMBuilder().build(documentBuilder.parse(stream))
    return document.rootElement
}

fun loadCatechismFromXml(stream: InputStream): Catechism {
    try {
        val rootNode = getRootElement(stream)
        val result = Catechism(trimTextInXml(rootNode.getChild("description").text))
        for (recordNode in rootNode.getChild("data").getChildren("record"))
            result.records.add(Record(
                trimTextInXml(recordNode.getChild("question").text),
                trimTextInXml(recordNode.getChild("answer").text)
            ))
        return result
    }
    catch (e: org.xml.sax.SAXParseException) {
        throw TranslationFormatException()
    }
}