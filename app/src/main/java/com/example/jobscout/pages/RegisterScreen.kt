package com.example.jobscout.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.jobscout.Data.User
import com.example.jobscout.Data.UserViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobscout.R


@Composable
fun RegisterScreen(userViewModel: UserViewModel, onSignUpSuccess: () -> Unit) {
    val users = userViewModel.users
    var fName by remember {mutableStateOf("")}
    var lName by remember {mutableStateOf("")}
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}

    // Controls whether error messages should display or not
    var disNameError by remember { mutableStateOf(false) }
    var disEmailError by remember { mutableStateOf(false) }
    var disPasswordError by remember { mutableStateOf(false) }

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

    Column(modifier = Modifier
            .fillMaxSize()
        .background(Color(221, 221, 221)),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){

        Spacer(modifier = Modifier
            .height(30.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Job Scout", fontFamily = newFontFamily, fontSize = 40.sp,
            color = Color(98, 114, 84),
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "Register", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(30.dp))


        OutlinedTextField(
            colors = TextFieldDefaults.colors(Color(118, 136, 91)),
            value = fName, onValueChange = {fName = it},
            label = { Text(text = "First Name")})

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.colors(Color(118, 136, 91)),
            value = lName, onValueChange = {lName = it},
            label = { Text(text = "Last Name")})

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.colors(Color(118, 136, 91)),
            value = email, onValueChange = {email = it},
            label = { Text(text = "Email")})
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.colors(Color(118, 136, 91)),
            value = password, onValueChange = {password = it},
            label = { Text(text = "Password")})

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(8.dp)
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(98, 114, 84)),
            onClick = {
                if (fName.isNotBlank() && lName.isNotBlank()) {
                    disNameError = false
                } else if (fName.isBlank() && lName.isBlank()) {
                    disNameError = true
                }
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    disEmailError = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    disEmailError = true
                }
                if (Regex("^[a-zA-Z0-9@_]+$").matches(password) && password.length >= 8) {
                    disPasswordError = false
                } else if (!Regex("^[a-zA-Z0-9@_]+$").matches(password) && password.length < 8) {
                    disPasswordError = true
                }
                if (fName.isNotBlank() && lName.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    && Regex("^[a-zA-Z0-9@_]+$").matches(password) && password.length >= 8)
                {
                    val user = User(fName = fName, lName = lName, email = email, password = password)
                    userViewModel.addUser(user)
                    fName = ""
                    lName = ""
                    email = ""
                    password = ""
                    onSignUpSuccess()
                }
            },
        ) {
            Text(text = "Add User")
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (disNameError) Text(text = "Please enter your first and last name.", color = MaterialTheme.colorScheme.error)
        if (disEmailError) Text(text = "Please enter a valid email address.", color = MaterialTheme.colorScheme.error)
        if (disPasswordError) Text(text = "Password must be at least 8 characters and can only contain letters, numbers, @, and _", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.error)

//        Text(text = "User List", style = MaterialTheme.typography.titleMedium)
//        LazyColumn {
//            items(users) { user ->
//                UserItem(user, userViewModel)
//
//            }
//        }

    }
}

@Composable
fun UserItem(user: User, userViewModel: UserViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ){
        Text(text = user.fName, modifier = Modifier.weight(1f))
        Text(text = user.lName, modifier = Modifier.weight(1f))
        Text(text = user.email, modifier = Modifier.weight(1f))
        Text(text = user.password, modifier = Modifier.weight(1f))


        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {userViewModel.deleteUser(user)},
            modifier = Modifier.padding(start = 8.dp)
        ){
            Text(text = "Delete")
        }

    }

}