package com.oguzdogdu.walliescompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.R.font.googlesansmedium

val regular = FontFamily(Font(R.font.googlesansregular))
val medium = FontFamily(Font(googlesansmedium))
val bold = FontFamily(Font(R.font.googlesansbold))
val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = medium,
        color = Color.Black,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ), titleSmall = TextStyle(
        fontFamily = medium,
        color = Color.Unspecified,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = medium,
        color = Color.Unspecified,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = medium,
        color = Color.White,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )/* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)