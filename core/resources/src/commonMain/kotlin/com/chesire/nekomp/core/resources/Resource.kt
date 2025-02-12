package com.chesire.nekomp.core.resources

import nekomp.core.resources.generated.resources.Res

// This saves having to add a custom import every class that uses resources
object R {
    val string = Res.string
    val plurals = Res.plurals
    val drawable = Res.drawable
}
