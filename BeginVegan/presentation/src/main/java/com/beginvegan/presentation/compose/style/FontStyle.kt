package com.beginvegan.presentation.compose.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.beginvegan.presentation.R

@Composable
fun CaptionRegular() = TextStyle(
    color = colorResource(id = R.color.color_text_01),
    fontSize = 12.sp,
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal
)

val fontFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold)
)