package com.andreeailie.tracker_presentation.groceries_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreeailie.core.R
import com.andreeailie.core_ui.ButtonGreen


@Composable
fun AddGroceryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clickable { onClick() },
        contentAlignment = BottomEnd
    ) {
        Card(
            modifier = modifier
                .size(60.dp)
                .align(BottomEnd),
            shape = AbsoluteRoundedCornerShape(
                topLeft = 20.dp,
                topRight = 20.dp,
                bottomLeft = 20.dp,
                bottomRight = 20.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = ButtonGreen
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 19.dp, top = 7.dp)
                    .background(
                        color = ButtonGreen,
                        shape = AbsoluteRoundedCornerShape(
                            topLeft = 10.dp,
                            topRight = 10.dp,
                            bottomLeft = 10.dp,
                            bottomRight = 10.dp
                        )
                    ),

                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.add_grocery_label),
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 40.sp,
                    ),
                )
            }
        }
    }
}