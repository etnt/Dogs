package se.kruskakli.dogs.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import se.kruskakli.dogs.domain.BreedViewModel
import se.kruskakli.dogs.domain.MainIntent
import se.kruskakli.dogs.domain.Screen

@Composable
fun FavoriteScreen(
    viewModel: BreedViewModel
) {
    val images by viewModel.images.collectAsState()
    val selectedImage by viewModel.selectedImage.collectAsState()

    ImageGallery(images) { image ->
        // FIXME: should go via MainIntent
        viewModel.selectImage(image)
    }

    AnimatedVisibility(visible = selectedImage != null) {
        selectedImage?.let { image ->
            FullImageDialog(image = image) {
                viewModel.clearSelectedImage()
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
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        // Use Box for custom layout, apply animations as needed
        Box(modifier = Modifier.clickable(onClick = onClose)) {
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(DpSize(300.dp, 300.dp)), // Or use screen size
                contentScale = ContentScale.Fit
            )
        }
    }
}


// Modify ImageGallery or ImageThumbnail to use FullImageDialog on click
fun loadImageFromFile(file: String): Bitmap? {
    return BitmapFactory.decodeFile(file)
}