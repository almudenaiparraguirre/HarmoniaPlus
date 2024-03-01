package com.mariana.harmonia.models.entity

import java.time.Month

class User(
    var email: String? = null,
    var name: String? = null,
    var experiencia: Int? = 0,
    var nivelActual: Int? = 1,
    var vidas: Int? = 5,
    var precisiones: MutableMap<Int, Int> = mutableMapOf(),
    var imagen: String? = null,
    var mesRegistro: Month,
    var anioRegistro: Int
)