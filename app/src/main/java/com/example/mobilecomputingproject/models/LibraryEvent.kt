package com.example.mobilecomputingproject.models

import java.sql.Timestamp

data class LibraryEvent(
    var eventDescription: String? = null,
    var eventTitle: String? = null,
    var libraryID: String? = null,
    var targetedAudience: String? = null,
    var timestamp: com.google.firebase.Timestamp? = null
)
