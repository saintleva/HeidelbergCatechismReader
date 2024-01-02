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

package org.saintleva.heidelberg.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import org.saintleva.heidelberg.data.repository.Repository
import org.saintleva.heidelberg.data.SearchConditions

sealed class SearchDialogEvent {
    object None: SearchDialogEvent()
    data class ApplySearchConditionsEvent(val conditions: SearchConditions): SearchDialogEvent()
}

class SearchDialogViewModel() : ViewModel() {

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    private val _text = mutableStateOf(Repository.previousConditions?.text ?: "")
    val text: State<String> = _text

    private val _searchInQuestions = mutableStateOf(Repository.previousConditions?.searchInQuestions ?: true)
    val searchInQuestions: State<Boolean> = _searchInQuestions

    private val _searchInAnswers = mutableStateOf(Repository.previousConditions?.searchInAnswers ?: true)
    val searchInAnswers: State<Boolean> = _searchInAnswers

    private val _matchCase = mutableStateOf(Repository.previousConditions?.matchCase ?: true)
    val matchCase: State<Boolean> = _matchCase

    private val _searchDialogEvent = mutableStateOf<SearchDialogEvent>(SearchDialogEvent.None)
    val searchDialogEvent: State<SearchDialogEvent> = _searchDialogEvent

    fun maySearch(): Boolean {
        return text.value.isNotEmpty() && (searchInQuestions.value || searchInAnswers.value)
    }

    fun consumeEvent() {
        _searchDialogEvent.value = SearchDialogEvent.None
    }

    fun show() {
        _showDialog.value = true
    }

    fun onOK() {
        _showDialog.value = false
        _searchDialogEvent.value = SearchDialogEvent.ApplySearchConditionsEvent(
            SearchConditions(text.value, searchInQuestions.value, searchInAnswers.value, matchCase.value))
    }

    fun onCancel() {
        _showDialog.value = false
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }

    fun onSearchInQuestionsChange(newValue: Boolean) {
        _searchInQuestions.value = newValue
    }

    fun onSearchInAnswerChange(newValue: Boolean) {
        _searchInAnswers.value = newValue
    }

    fun onMatchCaseChange(newValue: Boolean) {
        _matchCase.value = newValue
    }
}