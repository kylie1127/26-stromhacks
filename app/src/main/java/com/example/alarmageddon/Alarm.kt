package com.example.alarmageddon

import java.util.UUID

data class Alarm(
    val id: String = UUID.randomUUID().toString(),
    val time: String,
    val repeatDays: List<String>,
    val crewCount: Int,
    val stake: String
)
