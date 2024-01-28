package com.example.inklink.models

class User {
    var id: Int = 0
    var firstName: String? = null
    var lastName: String? = null
    var username: String? = null
    var email: String? = null
    var password: String? = null
    var about: String? = null
    var accountStatus: String? = null
    var registrationDate: String? = null

    constructor()

    constructor(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        about: String
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.username = firstName
        this.email = email
        this.password = password
        this.about = about
    }

    constructor(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String,
        about: String
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.email = email
        this.password = password
        this.about = about
    }

    constructor(
        id: Int,
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String,
        about: String
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.email = email
        this.password = password
        this.about = about
    }

    override fun toString(): String {
        // Vim macro time.
        return """
            User: {
                id: ${this.id},
                firstName: ${this.firstName},
                lastName: ${this.lastName},
                username: ${this.username},
                email: ${this.email},
                password: ${this.password},
                about: ${this.about},
            }
        """.trimIndent()
    }
}
