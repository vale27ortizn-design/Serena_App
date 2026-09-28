package com.serena.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.serena.R

val NunitoSans = FontFamily(
    Font(
        resId = R.font.nunitosans_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.nunitosans_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.nunitosans_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.nunitosans_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.nunitosans_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.nunitosans_extrabold,
        weight = FontWeight.ExtraBold
    ),
    Font(
        resId = R.font.nunitosans_black,
        weight = FontWeight.Black
    )
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)