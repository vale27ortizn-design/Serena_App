package com.serena.ui.theme

import androidx.compose.material3.Typography as M3Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.serena.R

val NunitoSans = FontFamily(
    Font(resId = R.font.nunitosans_light, weight = FontWeight.Light),
    Font(resId = R.font.nunitosans_regular, weight = FontWeight.Normal),
    Font(resId = R.font.nunitosans_medium, weight = FontWeight.Medium),
    Font(resId = R.font.nunitosans_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.nunitosans_bold, weight = FontWeight.Bold),
    Font(resId = R.font.nunitosans_extrabold, weight = FontWeight.ExtraBold),
    Font(resId = R.font.nunitosans_black, weight = FontWeight.Black)
)

private val base = M3Typography()

val Typography = M3Typography(
    displayLarge = base.displayLarge.copy(fontFamily = NunitoSans),
    displayMedium = base.displayMedium.copy(fontFamily = NunitoSans),
    displaySmall = base.displaySmall.copy(fontFamily = NunitoSans),
    headlineLarge = base.headlineLarge.copy(fontFamily = NunitoSans),
    headlineMedium = base.headlineMedium.copy(fontFamily = NunitoSans),
    headlineSmall = base.headlineSmall.copy(fontFamily = NunitoSans),
    titleLarge = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = base.titleMedium.copy(fontFamily = NunitoSans),
    titleSmall = base.titleSmall.copy(fontFamily = NunitoSans),
    bodyLarge = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = base.bodyMedium.copy(fontFamily = NunitoSans),
    bodySmall = base.bodySmall.copy(fontFamily = NunitoSans),
    labelLarge = base.labelLarge.copy(fontFamily = NunitoSans),   // botones
    labelMedium = base.labelMedium.copy(fontFamily = NunitoSans), // barra inferior
    labelSmall = base.labelSmall.copy(fontFamily = NunitoSans)
)