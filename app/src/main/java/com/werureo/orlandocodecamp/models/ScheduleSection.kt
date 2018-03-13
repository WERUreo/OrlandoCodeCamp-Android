package com.werureo.orlandocodecamp.models


data class ScheduleSection(
        val timeslot: String,
        val sessions: List<Session>
)