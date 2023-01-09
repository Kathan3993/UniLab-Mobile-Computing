package com.example.mobilecomputingproject.models

import java.util.*

/**
 * Model for Users
 */

class User {
    var firstname: String
    var lastname: String
    var email: String
    var timestamp: Date

    // Initialize the variables
    constructor(firstname: String, lastname: String, email: String, timestamp: Date) {
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.timestamp = Date()

    }


}