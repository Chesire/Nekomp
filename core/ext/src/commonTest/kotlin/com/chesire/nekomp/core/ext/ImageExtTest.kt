package com.chesire.nekomp.core.ext

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.preferences.models.ImageQuality
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ImageExtTest : FunSpec({

    listOf(
        Triple(
            ImageQuality.Lowest,
            Image(
                tiny = "tiny",
                small = "small",
                medium = "medium",
                large = "large",
                original = "original"
            ),
            "tiny"
        ),
        Triple(
            ImageQuality.Low,
            Image(
                tiny = "tiny",
                small = "small",
                medium = "medium",
                large = "large",
                original = "original"
            ),
            "small"
        ),
        Triple(
            ImageQuality.Medium,
            Image(
                tiny = "tiny",
                small = "small",
                medium = "medium",
                large = "large",
                original = "original"
            ),
            "medium"
        ),
        Triple(
            ImageQuality.High,
            Image(
                tiny = "tiny",
                small = "small",
                medium = "medium",
                large = "large",
                original = "original"
            ),
            "large"
        ),
        Triple(
            ImageQuality.Highest,
            Image(
                tiny = "tiny",
                small = "small",
                medium = "medium",
                large = "large",
                original = "original"
            ),
            "original"
        )
    ).forEach { (inputQuality, image, expected) ->
        test("Given $inputQuality, When toBestImage, Then $expected is output") {
            image.toBestImage(inputQuality).shouldBe(expected)
        }
    }
})
