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

package org.saintleva.heidelberg.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.saintleva.heidelberg.Repository
import org.saintleva.heidelberg.data.SearchConditions


sealed class Route(val path: String) {

    object Reading: Route("reading") {
        const val questionPosition = "questionPosition"
    }

    abstract class SelectSomething(path: String): Route(path) {
        val selectedQuestion = "selectedQuestion"
    }

    object SelectQuestion: SelectSomething("selectQuestion")
    object SelectSunday: SelectSomething("selectSunday")

    object Found: Route("found") {
        const val text = "text"
        const val searchInQuestions = "searchInQuestions"
        const val searchInAnswers = "searchInAnswers"
        const val matchCase = "matchCase"
    }

    object AboutTranslation: Route("aboutTranslation")
    object AboutCatechism: Route("aboutCatechism")
    object AboutApplication: Route("aboutApplication")

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/$arg")
            }
        }
    }

    fun formatArgs(vararg args: String) : String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/{$arg}")
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun AppNavGraph(navController: NavHostController) {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun NavigableUpScreen(content: @Composable () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("MY TITLE") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            content()
        }
    }

    NavHost(navController = navController, startDestination = Route.Reading.withArgs(-1)) {

        composable(
            route = Route.Reading.formatArgs(Route.Reading.questionPosition),
            arguments = listOf(navArgument(Route.Reading.questionPosition) { type = NavType.IntType })
        ) { backStackEntry ->
            ReadingScreen(
                object : NavigateToScreens {
                    override fun selectTranslation() {
                        navController.navigate(
                            Route.Reading.withArgs(Repository.scrollPosition.firstVisibleItemIndex)
                        )
                    }

                    override fun selectQuestion(selectedQuestion: Int) {
                        navController.navigate(Route.SelectQuestion.withArgs(selectedQuestion))
                    }

                    override fun selectSunday(selectedQuestion: Int) {
                        navController.navigate(Route.SelectSunday.withArgs(selectedQuestion))
                    }

                    override fun found(conditions: SearchConditions) {
                        navController.navigate(
                            Route.Found.withArgs(
                                conditions.text,
                                conditions.searchInQuestions,
                                conditions.searchInAnswers,
                                conditions.matchCase
                            )
                        )
                    }

                    override fun aboutTranslation() {
                        navController.navigate(Route.AboutTranslation.path)
                    }

                    override fun aboutCatechism() {
                        navController.navigate(Route.AboutCatechism.path)
                    }

                    override fun aboutApplication() {
                        navController.navigate(Route.AboutApplication.path)
                    }
                },
                backStackEntry.arguments!!.getInt(Route.Reading.questionPosition)
            )
        }

        composable(
            route = Route.SelectQuestion.formatArgs(Route.SelectQuestion.selectedQuestion),
            arguments = listOf(navArgument(Route.SelectQuestion.selectedQuestion) { type = NavType.IntType })
        ) { backStackEntry ->
            NavigableUpScreen {
                val selected = backStackEntry.arguments!!.getInt(Route.SelectQuestion.selectedQuestion)
                SelectQuestionScreen(
                    navigateToReadingScreen = { selectedQuestion ->
                        navController.navigate(Route.Reading.withArgs(selectedQuestion))
                    },
                    selected
                )
            }
        }

        composable(
            route = Route.SelectSunday.formatArgs(Route.SelectSunday.selectedQuestion),
            arguments = listOf(navArgument(Route.SelectSunday.selectedQuestion) { type = NavType.IntType })
        ) { backStackEntry ->
            NavigableUpScreen {
                val selected = backStackEntry.arguments!!.getInt(Route.SelectSunday.selectedQuestion)
                SelectQuestionScreen(
                    navigateToReadingScreen = { selectedQuestion ->
                        navController.navigate(Route.Reading.withArgs(selectedQuestion))
                    },
                    selected
                )
            }
        }

        composable(
            route = Route.Found.formatArgs(
                Route.Found.text,
                Route.Found.searchInQuestions,
                Route.Found.searchInAnswers,
                Route.Found.matchCase
            ),
            arguments = listOf(
                navArgument(Route.Found.text) { type = NavType.StringType },
                navArgument(Route.Found.searchInQuestions) { type = NavType.BoolType },
                navArgument(Route.Found.searchInAnswers) { type = NavType.BoolType },
                navArgument(Route.Found.matchCase) { type = NavType.BoolType },
            )
        ) { backStackEntry ->
            NavigableUpScreen {
                FoundScreen(
                    SearchConditions(
                        backStackEntry.arguments!!.getString(Route.Found.text)!!,
                        backStackEntry.arguments!!.getBoolean(Route.Found.searchInQuestions),
                        backStackEntry.arguments!!.getBoolean(Route.Found.searchInAnswers),
                        backStackEntry.arguments!!.getBoolean(Route.Found.matchCase)
                    )
                )
            }
        }

        composable(route = Route.AboutTranslation.path) {
            NavigableUpScreen {
                AboutTranslationScreen()
            }
        }

        composable(route = Route.AboutCatechism.path) {
            NavigableUpScreen {
                AboutCatechismScreen()
            }
        }

        composable(route = Route.AboutApplication.path) {
            NavigableUpScreen {
                AboutApplicationScreen()
            }
        }
    }
}