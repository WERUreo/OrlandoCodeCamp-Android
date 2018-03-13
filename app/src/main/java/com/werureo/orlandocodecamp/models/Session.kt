package com.werureo.orlandocodecamp.models


data class Session(
        val name: String,
        val description: String,
        val level: String,
        val speaker: String,
        val speakerImageUrl: String,
        val track: Track,
        val timeslot: String
)