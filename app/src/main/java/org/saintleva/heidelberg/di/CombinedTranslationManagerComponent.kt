package org.saintleva.heidelberg.di

import org.saintleva.heidelberg.data.manager.AssetsTranslationManager
import org.saintleva.heidelberg.ui.screens.selecttranslation.SelectTranslationViewModel

object CombinedTranslationManagerComponent {

    fun inject(selectTranslationViewModel: SelectTranslationViewModel) {
        selectTranslationViewModel.manager = AssetsTranslationManager
    }
}