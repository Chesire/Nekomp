package com.chesire.nekomp.core.ext

import com.chesire.nekomp.core.model.Image
import com.chesire.nekomp.core.preferences.ImageQuality

/**
 * Gets the best matching [Image] value based on the preferred [imageQuality].
 */
fun Image.toBestImage(imageQuality: ImageQuality): String {
    return when (imageQuality) {
        ImageQuality.Lowest -> lowest
        ImageQuality.Low -> low
        ImageQuality.Medium -> middle
        ImageQuality.High -> high
        ImageQuality.Highest -> highest
    }
}
