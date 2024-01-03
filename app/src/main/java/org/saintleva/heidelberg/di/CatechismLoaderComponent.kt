package org.saintleva.heidelberg.di

import org.saintleva.heidelberg.data.loader.AssetsCatechismLoader
import org.saintleva.heidelberg.ui.screens.reading.ReadingViewModel


object CatechismLoaderComponent {

    fun inject(readingViewModel: ReadingViewModel) {
        readingViewModel.loader = AssetsCatechismLoader
    }
}