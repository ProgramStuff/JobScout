package com.example.jobscout.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobscout.Data.JobViewModel
import com.example.jobscout.R

@Composable
fun HomeScreen(jobViewModel: JobViewModel){

    // Google fonts package provider
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val fontName = GoogleFont("Lobster Two")

    val newFontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(221, 221, 221)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(30.dp))

        /*** Home Page title ***/

        Spacer(modifier = Modifier
            .height(30.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Job Scout", fontFamily = newFontFamily, fontSize = 40.sp,
            color = Color(98, 114, 84),
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(40.dp))

        // Home page introduction


    }
}