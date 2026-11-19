package com.taskflow.app.ui.theme

import androidx.compose.ui.graphics.Color

// Light theme (Modern Indigo Accent)
val LightPrimary = Color(0xFF4F46E5)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFEEF2FF)
val LightOnPrimaryContainer = Color(0xFF312E81)
val LightSecondary = Color(0xFF6366F1)
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightOnSurface = Color(0xFF0F172A)
val LightOnSurfaceVariant = Color(0xFF64748B)
val LightOutline = Color(0xFF94A3B8)
val LightOutlineVariant = Color(0xFFE2E8F0)
val LightError = Color(0xFFEF4444)
val LightErrorContainer = Color(0xFFFEE2E2)

// Dark theme (Deep Slate & Indigo)
val DarkPrimary = Color(0xFF818CF8)
val DarkOnPrimary = Color(0xFF1E1B4B)
val DarkPrimaryContainer = Color(0xFF312E81)
val DarkOnPrimaryContainer = Color(0xFFE0E7FF)
val DarkSecondary = Color(0xFFA5B4FC)
val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkSurfaceVariant = Color(0xFF334155)
val DarkOnSurface = Color(0xFFF8FAFC)
val DarkOnSurfaceVariant = Color(0xFF94A3B8)
val DarkOutline = Color(0xFF64748B)
val DarkOutlineVariant = Color(0xFF334155)
val DarkError = Color(0xFFFCA5A5)
val DarkErrorContainer = Color(0xFF7F1D1D)

// Priority badge colors (background + text)
data class BadgeColors(val background: Color, val text: Color)

val HighLight = BadgeColors(Color(0xFFFEE2E2), Color(0xFFDC2626))
val HighDark = BadgeColors(Color(0xFF451A1A), Color(0xFFFCA5A5))
val MediumLight = BadgeColors(Color(0xFFFEF3C7), Color(0xFFD97706))
val MediumDark = BadgeColors(Color(0xFF453010), Color(0xFDFCD34D))
val LowLight = BadgeColors(Color(0xFFDCFCE7), Color(0xFF16A34A))
val LowDark = BadgeColors(Color(0xFF143820), Color(0xFF86EFAC))

fun priorityBadgeColors(priority: String, isDark: Boolean): BadgeColors = when (priority) {
    "High" -> if (isDark) HighDark else HighLight
    "Medium" -> if (isDark) MediumDark else MediumLight
    else -> if (isDark) LowDark else LowLight
}
