package com.easytap.app.domain.model

/** Allow-listed actions. The AI can only pick from this set; it never supplies code. */
enum class GuidanceAction { OPEN_APP, OPEN_SETTINGS_PAGE, TAP, TOGGLE, SCROLL, TYPE_BY_USER, INFORM }

data class GuidanceStep(
    val task: String,
    val step: Int,
    val totalSteps: Int,
    val action: GuidanceAction,
    val target: String,
    val instruction: String,
    val riskLevel: RiskLevel,
    val confidence: Float,
)
