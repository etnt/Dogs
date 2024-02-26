package se.kruskakli.dogs.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import se.kruskakli.dogs.domain.BreedViewModel
import se.kruskakli.dogs.domain.MainIntent
import se.kruskakli.dogs.domain.MainIntent.ShowSelectedImage
import se.kruskakli.dogs.domain.MainIntent.ClearSelectedImage
import se.kruskakli.dogs.domain.Screen

@Composable
fun FavoriteScreen(
    viewModel: BreedViewModel
) {
    val images by viewModel.images.collectAsState()
    val selectedImage by viewModel.selectedImage.collectAsState()

    val currentIndex = images.indexOf(selectedImage)
    val canGoPrevious = currentIndex > 0
    val canGoNext = currentIndex < images.size - 1

    val transition = updateTransition(
        targetState = selectedImage != null,
        label = "transition"
    )
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) }, label = ""
    ) { isVisible -> if (isVisible) 0.5f else 0.0f }

    Box(
        Modifier
             .fillMaxSize()
    ) { 
        ImageGallery(images) { image ->
            viewModel.handleIntent(MainIntent.ShowSelectedImage(image))
        }

        // Scrim
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = alpha))
        ) {

            AnimatedVisibility(
                visible = selectedImage != null,
                enter = fadeIn(animationSpec = tween(durationMillis = 1500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1500))
            ) {
                selectedImage?.let { image ->
                    FullImageDialog(
                        image = image,
                        onClose = { viewModel.handleIntent(MainIntent.ClearSelectedImage) },
                        onPrevious = { if (canGoPrevious) viewModel.handleIntent(MainIntent.ShowSelectedImage(images[currentIndex - 1])) },
                        onNext = { if (canGoNext) viewModel.handleIntent(MainIntent.ShowSelectedImage(images[currentIndex + 1])) },
                        canGoPrevious = canGoPrevious,
                        canGoNext = canGoNext
                        ) 
                }
            }
        }
    }
}

@Composable
fun ImageGallery(
    images: List<Bitmap>,
    onClick: (Bitmap) -> Unit
) {
    LazyVerticalGrid(
        //columns = GridCells.Fixed(3),
        columns = GridCells.Adaptive(minSize = 100.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(images) {
            ImageThumbnail(it) {
                onClick(it)
            }
        }
    }
}

@Composable
fun ImageThumbnail(
    image: Bitmap,
    onImageClick: (Bitmap) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onImageClick(image) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@Composable
fun FullImageDialog(
    image: Bitmap,
    onClose: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    canGoPrevious: Boolean,
    canGoNext: Boolean
) {

    Dialog(onDismissRequest = onClose) {
        // Use Box for custom layout, apply animations as needed
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClose)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // The Image!!
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )

                // Row with previous and next buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp , end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Previous button
                    IconButton(
                        onClick = onPrevious,
                        enabled = canGoPrevious
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Previous image")
                    }

                    // Next button
                    IconButton(
                        onClick = onNext,
                        enabled = canGoNext
                    ) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "Next image")
                    }
                }
            }
        }
    }

}


// Modify ImageGallery or ImageThumbnail to use FullImageDialog on click
fun loadImageFromFile(file: String): Bitmap? {
    return BitmapFactory.decodeFile(file)
}