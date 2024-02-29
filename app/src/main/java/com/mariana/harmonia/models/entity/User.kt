package com.mariana.harmonia.models.entity

class User(
    var email: String? = null,
    var name: String? = null,
    var experiencia: Int? = 0,
    var nivelActual: Int? = 1,
    var vidas: Int? = 5,
    var precisiones: MutableMap<Int, Int> = mutableMapOf()
)