package com.example.mobilecomputingproject.models

/**
 * Model for Past Devices
 */
class PastDevices {
    var configuration: String
    var laptopType: String
    var loanType: String
    var library: String
    var borrowedDate: String
    var returnedDate: String

    // Initialize the variables
    constructor(
        configuration: String,
        laptopType: String,
        loanType: String,
        library: String,
        borrowedDate: String,
        returnedDate: String
    ) {
        this.configuration = configuration
        this.laptopType = laptopType
        this.loanType = loanType
        this.library = library
        this.borrowedDate = borrowedDate
        this.returnedDate = returnedDate
    }
}