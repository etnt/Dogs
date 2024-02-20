package se.kruskakli.dogs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import se.kruskakli.dogs.domain.BreedViewModel
import se.kruskakli.dogs.domain.Screen

@Composable
fun BreedScreen(
    viewModel: BreedViewModel,
    navController: NavController
    ) {
    val navigateToSettings by viewModel.navigateToSettings.collectAsState()

    if (navigateToSettings) {
        navController.navigate(Screen.SettingsScreen.route)
    }
    
    val currentBreed by viewModel.currentBreed.collectAsState()
    val breed = currentBreed

    breed?.let {
        Column(
            modifier = Modifier.padding(start=8.dp, top=4.dp, end=8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                AsyncImage(
                    model = it.image.url,
                    contentDescription = "Image of ${it.name}",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(280.dp)
                        .clip(RoundedCornerShape(8.dp))
                    //.fillMaxWidth()
                    //.aspectRatio(1f)
                )
            }

            DogText(text = annotatedText("Bred for: ", "${it.bred_for}"))
            DogText(text = annotatedText("Breed group: ", "${it.breed_group ?: "Unknown"}"))
            DogText(text = annotatedText("Temperament: ", "${it.temperament}"))
            DogText(text = annotatedText("Life span :", "${it.life_span}"))
            DogText(text = annotatedText("Height: ", "${it.height}"))
            DogText(text = annotatedText("Weight: ", "${it.weight}"))
        }
    } ?: run {
        Text(text = "Loading...")
    }
}

@Composable
fun DogText(
    text: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Box {
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
    }
}

// Build a text string with an optional annotation
@Composable
fun annotatedText(
    text: String,
    annotation: String? = null
) : AnnotatedString
{
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(text)
        }
        if (annotation != null) {
            withStyle(
                style = SpanStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = FontStyle.Italic,
                )
            ) {
                append(annotation)
            }
        }
    }
    return annotatedText
}