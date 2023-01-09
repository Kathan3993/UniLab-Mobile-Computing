package com.example.mobilecomputingproject.models

/**
 * Model for Current Devices
 */
class CurrentDevices {
    var configuration: String
    var laptopType: String
    var loanType: String
    var library: String
    var borrowedDate: String
    var returnDate: String

    // Initialize the variables
    constructor(
        configuration: String,
        laptopType: String,
        loanType: String,
        library: String,
        borrowedDate: String,
        returnDate: String
    ) {
        this.configuration = configuration
        this.laptopType = laptopType
        this.loanType = loanType
        this.library = library
        this.borrowedDate = borrowedDate
        this.returnDate = returnDate
    }
}