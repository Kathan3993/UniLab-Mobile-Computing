package com.example.mobilecomputingproject.models

/**
 * Model for Borrow Request
 */
class BorrowRequest {
    var userId: String
    var configuration: String
    var laptopType: String
    var loanType: String
    var library: String
    var status: String
    var dateOfRequest: String

    // Initialize the variables
    constructor(
        userId: String,
        configuration: String,
        laptopType: String,
        loanType: String,
        library: String,
        status: String,
        dateOfRequest: String
    ) {
        this.configuration = configuration
        this.laptopType = laptopType
        this.loanType = loanType
        this.library = library
        this.status = status
        this.dateOfRequest = dateOfRequest
        this.userId = userId
    }
}