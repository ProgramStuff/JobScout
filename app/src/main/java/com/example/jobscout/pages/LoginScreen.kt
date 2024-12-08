package com.example.jobscout.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobscout.Data.UserViewModel
import com.example.jobscout.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

// User class to hold implements login function
class User(private val validUsername: String, private val validPassword: String) {
    fun login(inputUsername: String, inputPassword: String): String {
        return if (inputUsername == validUsername && inputPassword == validPassword) {
            "Login Success"
        } else {
            "Login Failed."
        }
    }
}

// Login screen takes a lambda function or a function with no return value or parameters
// This allows navigation to WelcomeScreen after login
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, userViewModel: UserViewModel) {
    var email by remember { mutableStateOf("") };
    var password by remember { mutableStateOf("") };
    var welcomeMsg by remember { mutableStateOf("") };
    var emailError by remember { mutableStateOf("")}
    var loggedInUser by remember { mutableStateOf<Any?>(null) }
    val users = userViewModel.users

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
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        // Title
        Spacer(modifier = Modifier
            .height(30.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Job Scout", fontFamily = newFontFamily, fontSize = 40.sp,
            color = Color(98, 114, 84),
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Login", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(30.dp))



        /*** Email with validation ***/

        OutlinedTextField(
            colors = TextFieldDefaults.colors(Color(118, 136, 91)),
            value = email,
            onValueChange = {
                email = it
                emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) "Invalid email" else ""
            },
            label = { Text(text = "Email")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (emailError.isNotEmpty()) Text(text = emailError, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.padding(10.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.colors(Color(118, 136, 91)),
            value = password, onValueChange = {password = it},
            label = { Text(text = "Password")})

        Spacer(modifier = Modifier.padding(10.dp))

        // Loop through each user in array and try to login with current values
        Button(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(8.dp)
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(98, 114, 84)),
                    onClick = {
            val matchingUser = users.find { user ->
                // TODO: This is for testing change back to email and password values

                user.email == "jordan@nscc.ca" && user.password == "password"
            }

            if (matchingUser != null) {
                // Get the logged in users id to access throughout app
                users.forEachIndexed{ index, user ->
                    if(user.email == "jordan@nscc.ca"){
                        userViewModel.loggedInUser(user)
                    }
                }
                welcomeMsg = "Login Success"
                onLoginSuccess()
            } else {
                welcomeMsg = "Invalid credentials"
            }
        }) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = welcomeMsg)
    }
}
