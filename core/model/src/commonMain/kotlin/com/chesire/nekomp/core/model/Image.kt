package com.chesire.nekomp.core.model

data class Image(
    val tiny: String,
    val small: String,
    val medium: String,
    val large: String,
    val original: String
) {

    // I'm so sorry if you are reading this block
    val lowest get() = tiny.ifBlank { small.ifBlank { medium.ifBlank { large.ifBlank { original } } } }
    val low get() = small.ifBlank { medium.ifBlank { tiny.ifBlank { large.ifBlank { original } } } }
    val middle get() = medium.ifBlank { large.ifBlank { small.ifBlank { original.ifBlank { tiny } } } }
    val high get() = large.ifBlank { medium.ifBlank { original.ifBlank { small.ifBlank { tiny } } } }
    val highest get() = original.ifBlank { large.ifBlank { medium.ifBlank { small.ifBlank { tiny } } } }

    companion object {

        val empty: Image get() = Image("", "", "", "", "")
    }
}
