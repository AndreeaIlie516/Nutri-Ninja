package com.andreeailie.tracker_presentation.tracker_overview.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.andreeailie.core.R
import com.andreeailie.core_ui.LocalSpacing
import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_presentation.components.NutrientInfo

@ExperimentalCoilApi
@Composable
fun TrackedFoodItem(
    trackedFood: TrackedFood,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(spacing.spaceExtraSmall)
            .shadow(
                elevation = 0.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = AbsoluteRoundedCornerShape(
                    topLeft = 10.dp,
                    topRight = 10.dp,
                    bottomLeft = 10.dp,
                    bottomRight = 10.dp
                )
            )
            .padding(end = spacing.spaceSmall)
            .height(100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = trackedFood.imageUrl)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        error(R.drawable.ic_burger)
                        fallback(R.drawable.ic_burger)
                    }).build()
            ),
            contentDescription = trackedFood.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 5.dp,
                        bottomStart = 5.dp
                    )
                )
        )
        Spacer(modifier = Modifier.width(spacing.spaceMedium))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = trackedFood.name,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
            Text(
                text = stringResource(
                    id = R.string.amount_info,
                    trackedFood.quantity
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    id = R.string.calorie_info,
                    trackedFood.calories
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.width(spacing.spaceSmall))
        Row(
            modifier = Modifier.padding(bottom = 14.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NutrientInfo(
                name = stringResource(id = R.string.carbs),
                amount = trackedFood.carbs,
                unit = stringResource(id = R.string.grams),
                amountTextSize = 14.sp,
                unitTextSize = 10.sp,
                nameTextStyle = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            NutrientInfo(
                name = stringResource(id = R.string.protein),
                amount = trackedFood.protein,
                unit = stringResource(id = R.string.grams),
                amountTextSize = 14.sp,
                unitTextSize = 10.sp,
                nameTextStyle = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            NutrientInfo(
                name = stringResource(id = R.string.fat),
                amount = trackedFood.fat,
                unit = stringResource(id = R.string.grams),
                amountTextSize = 14.sp,
                unitTextSize = 10.sp,
                nameTextStyle = MaterialTheme.typography.bodySmall
            )
        }
//        Column(
//            modifier = Modifier.fillMaxHeight(),
//            verticalArrangement = Arrangement.Center
//        ) {
////            Icon(
////                imageVector = Icons.Default.Close,
////                contentDescription = stringResource(id = R.string.delete),
////                modifier = Modifier
////                    .size(10.dp)
////                    .align(Alignment.End)
////                    .clickable { onDeleteClick() }
////            )
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                NutrientInfo(
//                    name = stringResource(id = R.string.carbs),
//                    amount = trackedFood.carbs,
//                    unit = stringResource(id = R.string.grams),
//                    amountTextSize = 16.sp,
//                    unitTextSize = 12.sp,
//                    nameTextStyle = MaterialTheme.typography.bodyMedium
//                )
//                Spacer(modifier = Modifier.width(spacing.spaceSmall))
//                NutrientInfo(
//                    name = stringResource(id = R.string.protein),
//                    amount = trackedFood.protein,
//                    unit = stringResource(id = R.string.grams),
//                    amountTextSize = 16.sp,
//                    unitTextSize = 12.sp,
//                    nameTextStyle = MaterialTheme.typography.bodyMedium
//                )
//                Spacer(modifier = Modifier.width(spacing.spaceSmall))
//                NutrientInfo(
//                    name = stringResource(id = R.string.fat),
//                    amount = trackedFood.fat,
//                    unit = stringResource(id = R.string.grams),
//                    amountTextSize = 16.sp,
//                    unitTextSize = 12.sp,
//                    nameTextStyle = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
    }
}