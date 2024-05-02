package com.andreeailie.tracker_presentation.tracker_overview.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreeailie.core_ui.*
import com.andreeailie.tracker_presentation.tracker_overview.TrackerOverviewState

@Composable
fun UserHeader(
    userPhoto: Painter,
    userName: String,
    state: TrackerOverviewState,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Row(modifier = Modifier.padding(start = 15.dp)) {
        Box(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(60.dp))
                    .background(Color.White)
                    .padding(horizontal = spacing.spaceExtraSmall, vertical = spacing.spaceExtraSmall),
            ) {
                Image(
                    painter = userPhoto,
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(vertical = spacing.spaceExtraSmall)
        ) {
            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .height(70.dp)
                    .padding(horizontal = spacing.spaceLarge, vertical = spacing.spaceSmall),

            ) {
                Text(
                    text = "Hello,",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 18.sp,
                    color = DarkGray,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp,
                    color = DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(vertical = spacing.spaceExtraSmall)
        ) {
            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .height(70.dp)
                    .padding(horizontal = spacing.spaceLarge, vertical = spacing.spaceSmall),
            ) {
                Row() {
//                    Icon(
//                    painter = painterResource(id = R.drawable.ic_calories), // Assume you have an icon with this name
//                    contentDescription = "Calories",
//                    modifier = Modifier.size(20.dp)
//                )
                    Text(
                        text = state.totalCalories.toString() + " / " + state.caloriesGoal,
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 16.sp,
                        color = DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.size(spacing.spaceExtraSmall))
                Row() {
//                    Icon(
//                    painter = painterResource(id = R.drawable.ic_protein), // Assume you have an icon with this name
//                    contentDescription = "Protein",
//                    modifier = Modifier.size(20.dp)
//                )
                    Text(
                        text = state.totalProtein.toString() + " / " + state.proteinGoal,
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 16.sp,
                        color = DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
