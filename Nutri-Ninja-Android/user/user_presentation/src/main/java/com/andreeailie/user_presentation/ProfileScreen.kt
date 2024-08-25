package com.andreeailie.user_presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreeailie.core.R
import com.andreeailie.core.util.UiEvent
import com.andreeailie.core_ui.BackgroundLightGreen
import com.andreeailie.core_ui.ButtonGreen
import com.andreeailie.core_ui.LocalSpacing

@Composable
fun ProfileScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val (password, setPassword) = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLightGreen)
            .padding(top = spacing.spaceLarge, bottom = 70.dp, start = 15.dp, end = 15.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .padding()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column()
                {
                    Text(
                        text = stringResource(id = R.string.profile_screen),
                        textAlign = TextAlign.Left,
                        color = colorResource(R.color.black),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            }

            Box(
                modifier = Modifier.padding(start = 110.dp, top = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(180.dp))
                        .background(Color.White)
                        .padding(
                            horizontal = spacing.spaceExtraSmall,
                            vertical = spacing.spaceExtraSmall
                        ),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "User Photo",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Text(text = "Username")
            BasicTextField(
                value = "andreea.ilie",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Text(text = "FirstName")
            BasicTextField(
                value = "Andreea",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Text(text = "LastName")
            BasicTextField(
                value = "Ilie",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Text(text = "Email")
            BasicTextField(
                value = "andreea.ilie@gmail.com",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )
//        UserProfileHeader(
//            userPhoto = painterResource(id = R.drawable.profile),
//            userName = "Andreea Ilie",
//            userEmail = "andreea.ilie@example.com",
//            userUsername = "andreea123",
//            state
//        )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Text(text = "Password")
            BasicTextField(
                value = "AlaBalaPortocala",
                onValueChange = setPassword,
                visualTransformation = PasswordVisualTransformation(),
                readOnly = true,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Text(text = "Height")
            BasicTextField(
                value = "1.50",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Text(text = "Weight")
            BasicTextField(
                value = "50",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Text(text = "Age")
            BasicTextField(
                value = "23",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )


            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Text(text = "Activity Level")
            BasicTextField(
                value = "Medium",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Text(text = "Goal")
            BasicTextField(
                value = "Lose weight",
                readOnly = true,
                onValueChange = setPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = spacing.spaceMedium)
                    .background(
                        Color.White,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    )
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.spaceSmall),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen)
            ) {
                Text(text = "Save")
            }

        }
    }
}
