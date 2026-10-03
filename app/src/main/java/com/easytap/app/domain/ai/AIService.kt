package com.easytap.app.domain.ai

import com.easytap.app.core.AppResult
import com.easytap.app.domain.model.GuidanceStep

/** Minimal context only: the goal, language, and an already-sanitised screen summary. */
data class GuidanceRequest(
    val userGoal: String,
    val languageTag: String,
    val sanitizedScreenSummary: String?,
    val stepsCompleted: Int,
)

/** Provider-agnostic. Cloud, on-device and different LLMs are separate implementations. */
interface AIService {
    suspend fun nextStep(request: GuidanceRequest): AppResult<GuidanceStep>
}
