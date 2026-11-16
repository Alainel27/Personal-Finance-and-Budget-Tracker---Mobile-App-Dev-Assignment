//made separate class rather than putting it in main activity because it is more tidy and cleaner

package com.example.mad_ca1_27.activities


import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onFinished: () -> Unit) {
    //holds the current scale and can animate over time
    val scale = remember { Animatable(0f) }

    //runs once the splash enters the screen
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 800,
                easing = { OvershootInterpolator(4f).getInterpolation(it)}
            )
        )
        //keep the logo for abit
        delay(2000L)
        onFinished()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = com.example.mad_ca1_27.R.drawable.img),
            contentDescription = "App logo",
            modifier = Modifier.scale(scale.value)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Finance Tracker",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color= MaterialTheme.colorScheme.primary

        )


    }

}