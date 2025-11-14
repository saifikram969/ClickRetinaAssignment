package com.example.clickretinaassignment.ui.theme

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.clickretinaassignment.R
import com.example.clickretinaassignment.data.model.UserProfile
import com.example.clickretinaassignment.viewModel.ProfileViewModel
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchProfile(context)
    }

    val profile = viewModel.profile
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        errorMessage != null -> {
            ErrorScreen(errorMessage) { viewModel.fetchProfile(context) }
        }

        profile != null -> {
            ProfileContent(profile)
        }
    }
}

@Composable
fun ErrorScreen(message: String, retry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = message, color = Color.Red, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        Button(onClick = retry) {
            Text("Retry")
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileContent(user: UserProfile) {

    val context = LocalContext.current
    val (selectedTab, setSelectedTab) = remember { mutableStateOf("Shots") }
    val buttonFont = FontFamily.SansSerif

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.header_background),
                contentDescription = "Header",
                modifier = Modifier.fillMaxWidth()
            )

            GlideImage(
                model = user.avatar ?: R.drawable.ic_launcher_background,
                contentDescription = "Avatar",
                modifier = Modifier
                    .offset(y = (-50).dp)
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(8.dp))

            Text(user.name ?: "", style = MaterialTheme.typography.titleLarge)

            Text(
                "${user.location?.city}, ${user.location?.country}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            Card(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.width(344.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("${user.statistics?.followers} Followers")
                    Spacer(Modifier.width(24.dp))
                    Text("${user.statistics?.following} Following")
                }
            }

            Spacer(Modifier.height(16.dp))

            SocialIconsSection(user)

            Spacer(Modifier.height(16.dp))

            TabsSection(selectedTab, setSelectedTab, buttonFont)

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                "Shots" -> ShotsScreen()
                "Collections" -> CollectionsScreen()
            }
        }

        IconButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .offset(y = 10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.setting),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun SocialIconsSection(user: UserProfile) {
    val context = LocalContext.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        val socialItems = mutableListOf<@Composable () -> Unit>()

        user.social?.website?.let { website ->
            socialItems.add {
                IconButton(onClick = { openUrl(context, website) }) {
                    Icon(painterResource(R.drawable.web_logo), contentDescription = null)
                }
            }
        }

        user.social?.profiles?.forEach { profile ->
            profile.url?.let { url ->
                when (profile.platform?.lowercase()) {
                    "instagram" -> {
                        socialItems.add {
                            IconButton(onClick = { openUrl(context, url) }) {
                                Icon(painterResource(R.drawable.instagram_logo), contentDescription = null)
                            }
                        }
                    }
                    "facebook" -> {
                        socialItems.add {
                            IconButton(onClick = { openUrl(context, url) }) {
                                Icon(painterResource(R.drawable.facebook_logo), contentDescription = null)
                            }
                        }
                    }
                }
            }
        }

        socialItems.forEachIndexed { index, item ->
            item()
            if (index < socialItems.size - 1) {
                Spacer(Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.ellipse),
                    contentDescription = null,
                    modifier = Modifier.size(7.dp)
                )
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun TabsSection(
    selectedTab: String,
    setSelectedTab: (String) -> Unit,
    buttonFont: FontFamily
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TabItem(
            label = "0 Shots",
            selected = selectedTab == "Shots",
            onClick = { setSelectedTab("Shots") },
            buttonFont = buttonFont,
            modifier = Modifier.weight(1f)
        )

        TabItem(
            label = "0 Collections",
            selected = selectedTab == "Collections",
            onClick = { setSelectedTab("Collections") },
            buttonFont = buttonFont,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TabItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    buttonFont: FontFamily,
    modifier: Modifier
) {
    val activeColor = Color(0xFF5151C6)
    val inactiveColor = Color(0xFF888888)
    val underlineColor = Color.Black
    val activeBg = Color(0xFF9EA7EC)

    Column(
        modifier = modifier.fillMaxHeight().padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextButton(
            onClick = onClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (selected) activeColor else inactiveColor,
                containerColor = if (selected) activeBg else Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = buttonFont,
                    fontWeight = FontWeight.Bold
                ),
                color = if (selected) activeColor else inactiveColor
            )
        }

        Box(
            modifier = Modifier.height(2.dp).fillMaxWidth().background(
                if (selected) underlineColor else Color.Transparent
            )
        )
    }
}

@Composable
fun ShotsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.center_bg_icon),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
fun CollectionsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.center_bg_icon),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }
}

fun openUrl(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
