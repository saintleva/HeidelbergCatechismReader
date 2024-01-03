package org.saintleva.heidelberg.data.models

import org.saintleva.heidelberg.NoLanguageSpecifiedException

class TranslationMetadata(
    val name: String?,
    val englishName: String?,
    val language: String,
    val isOriginal: Boolean
) {
    init {
        if (name == null && englishName == null) {
            throw NoLanguageSpecifiedException()
        }
    }

    val nameToUse = name ?: englishName
}

typealias AllTranslations = Map<String, TranslationMetadata>