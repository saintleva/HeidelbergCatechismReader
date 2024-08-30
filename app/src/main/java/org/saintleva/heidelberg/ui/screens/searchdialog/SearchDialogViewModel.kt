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

package org.saintleva.heidelberg.ui.screens.searchdialog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import org.saintleva.heidelberg.data.SearchConditions
import org.saintleva.heidelberg.data.repository.Repository
import org.saintleva.heidelberg.ui.screens.common.RepositoryViewModel


sealed class SearchDialogEvent {
    object None: SearchDialogEvent()
    data class ApplySearchConditionsEvent(val conditions: SearchConditions): SearchDialogEvent()
}

class SearchDialogViewModel(repository: Repository) : RepositoryViewModel(repository) {

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    private val _text = mutableStateOf(repository.previousConditions?.text ?: "")
    val text: State<String> = _text

    private val _searchInQuestions = mutableStateOf(repository.previousConditions?.searchInQuestions ?: true)
    val searchInQuestions: State<Boolean> = _searchInQuestions

    private val _searchInAnswers = mutableStateOf(repository.previousConditions?.searchInAnswers ?: true)
    val searchInAnswers: State<Boolean> = _searchInAnswers

    private val _matchCase = mutableStateOf(repository.previousConditions?.matchCase ?: true)
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
            SearchConditions(
                text.value,
                searchInQuestions.value,
                searchInAnswers.value,
                matchCase.value
            )
        )
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