package org.saintleva.heidelberg.di

import org.saintleva.heidelberg.MyApplication
import org.saintleva.heidelberg.data.repository.RepositoryImpl
import org.saintleva.heidelberg.ui.screens.common.RepositoryViewModel


object RepositoryComponent {

    fun inject(repositoryViewModel: RepositoryViewModel) {
        repositoryViewModel.repository = RepositoryImpl
    }

    fun inject(myApplication: MyApplication) {
        myApplication.repository = RepositoryImpl
    }
}