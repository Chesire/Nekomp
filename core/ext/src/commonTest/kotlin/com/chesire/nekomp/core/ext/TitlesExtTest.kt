package com.chesire.nekomp.core.ext

import com.chesire.nekomp.core.model.Titles
import com.chesire.nekomp.core.preferences.TitleLanguage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TitlesExtTest : FunSpec({

    listOf(
        Triple(
            TitleLanguage.Canonical,
            Titles(
                canonical = "canonical",
                english = "english",
                romaji = "romaji",
                cjk = "cjk"
            ),
            "canonical"
        ),
        Triple(
            TitleLanguage.English,
            Titles(
                canonical = "canonical",
                english = "english",
                romaji = "romaji",
                cjk = "cjk"
            ),
            "english"
        ),
        Triple(
            TitleLanguage.Romaji,
            Titles(
                canonical = "canonical",
                english = "english",
                romaji = "romaji",
                cjk = "cjk"
            ),
            "romaji"
        ),
        Triple(
            TitleLanguage.CJK,
            Titles(
                canonical = "canonical",
                english = "english",
                romaji = "romaji",
                cjk = "cjk"
            ),
            "cjk"
        )
    ).forEach { (inputLanguage, titles, expected) ->
        test("Given $inputLanguage, When toChosenLanguage, Then $expected is returned") {
            titles.toChosenLanguage(inputLanguage).shouldBe(expected)
        }
    }
})
