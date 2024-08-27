package com.andreeailie.tracker_presentation.groceries_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.andreeailie.core.R
import com.andreeailie.core_ui.BackgroundNavigationGreen
import com.andreeailie.core_ui.LocalSpacing
import com.andreeailie.tracker_domain.model.Grocery

@Composable
fun GroceryItem(
    modifier: Modifier = Modifier,
    grocery: Grocery,
    onCheckGroceryClick: (Grocery) -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .padding(top = 3.dp, bottom = 3.dp)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = AbsoluteRoundedCornerShape(
                    topLeft = 10.dp,
                    topRight = 10.dp,
                    bottomLeft = 10.dp,
                    bottomRight = 10.dp
                )
            ),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        if (grocery.imageUrl != "") {
//            AsyncImage(
//                modifier = Modifier
//                    .size(90.dp)
//                    .background(
//                        color = Color.Gray,
//                        shape = AbsoluteRoundedCornerShape(
//                            topLeft = 10.dp,
//                            bottomLeft = 10.dp,
//                        )
//                    ),
//                model = grocery.imageUrl,
//                placeholder = painterResource(id = R.drawable.ic_grocery),
//                contentDescription = "Grocery image"
//            )
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = grocery.imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            error(R.drawable.ic_burger)
                            fallback(R.drawable.ic_burger)
                        }).build()
                ),
                contentDescription = grocery.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .aspectRatio(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 5.dp,
                            bottomStart = 5.dp
                        )
                    )
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Box(
                modifier = Modifier
                    .width(130.dp)
            ) {
                Text(
                    text = grocery.name,
                    textAlign = TextAlign.Left,
                    color = BackgroundNavigationGreen,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Spacer(modifier = Modifier.padding(spacing.spaceSmall))
            Text(
                text = grocery.quantity.toString(),
                textAlign = TextAlign.Left,
                color = colorResource(R.color.grey),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.padding(spacing.spaceExtraSmall))
            Text(
                text = grocery.unit,
                textAlign = TextAlign.Left,
                color = colorResource(R.color.grey),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.padding(spacing.spaceMedium))
            Checkbox(
                checked = grocery.isChecked,
                onCheckedChange = { onCheckGroceryClick(grocery) },
                colors = CheckboxDefaults.colors(
                    checkedColor = BackgroundNavigationGreen,
                    uncheckedColor = colorResource(R.color.grey)
                )
            )
        }
    }
}