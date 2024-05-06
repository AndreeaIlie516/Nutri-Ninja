package com.andreeailie.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreeailie.core_ui.LocalSpacing
import com.andreeailie.tracker_presentation.components.UnitDisplay

@Composable
fun NutrientBarInfo(
    value: Int,
    goal: Int,
    name: String,
    unit: String,
    color: Color,
    backgroundColor: Color,
    iconResource: Painter,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
) {
    val background = Color.White
    val goalExceededColor = MaterialTheme.colorScheme.error
    val angleRatio = remember {
        Animatable(0f)
    }
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = value) {
        angleRatio.animateTo(
            targetValue = if (goal > 0) {
                value / goal.toFloat()
            } else 0f,
            animationSpec = tween(
                durationMillis = 300
            )
        )
    }
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .background(background, RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(50.dp))
            .padding(start = 30.dp, top = 15.dp, end = 30.dp, bottom = 30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            Image(
                painter = iconResource,
                contentDescription = "icon",
                modifier = Modifier
                    .size(25.dp)
            )
        }
        Spacer(modifier = Modifier.height(spacing.spaceLarge))
        Canvas(
            modifier = Modifier
                .padding(top = 50.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .aspectRatio(1f),
        ) {
            drawArc(
                color = if(value <= goal) background else goalExceededColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                size = size,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
            drawArc(
                color = backgroundColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                size = size,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )
            if(value <= goal) {
                drawArc(
                    color = color,
                    startAngle = 90f,
                    sweepAngle = 360f * angleRatio.value,
                    useCenter = false,
                    size = size,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UnitDisplay(
                amount = value,
                unit = unit,
                amountColor = if(value <= goal) {
                    Color.DarkGray
                } else goalExceededColor,
                unitColor = if(value <= goal) {
                    Color.LightGray
                } else goalExceededColor
            )
        }
    }
}