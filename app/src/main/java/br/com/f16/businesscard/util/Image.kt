package br.com.f16.businesscard.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import br.com.f16.businesscard.BuildConfig
import br.com.f16.businesscard.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class Image {
    companion object {
        fun share(context: Context, card: View) {
            val bitmap = getScreenCard(card)

            bitmap?.let {
                saveScreenCard(context, bitmap)
            }
        }

        private fun saveScreenCard(context: Context, bitmap: Bitmap) {
            val filename = "${System.currentTimeMillis()}.jpg"

            var fos: OutputStream? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    fos = imageUri?.let {
                        shareIntent(context, imageUri)
                        resolver.openOutputStream(it)
                    }
                }
            } else {
                val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                shareIntent(
                    context,
                    FileProvider.getUriForFile(
                        context,
                        BuildConfig.APPLICATION_ID + ".provider", image
                    )
                )
                fos = FileOutputStream(image)
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(
                    context,
                    context.getString(R.string.txt_card_saved),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        private fun shareIntent(context: Context, imageUri: Uri) {
            val shareintent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/jpeg"
            }

            context.startActivity(
                Intent.createChooser(
                    shareintent,
                    context.resources.getText(R.string.share)
                )
            )
        }

        private fun getScreenCard(card: View): Bitmap? {
            var screenshot: Bitmap? = null

            try {
                screenshot = Bitmap.createBitmap(
                    card.measuredWidth,
                    card.measuredHeight,
                    Bitmap.Config.ARGB_8888

                )
                val canvas = Canvas(screenshot)
                card.draw(canvas)
            } catch (e: Exception) {
                Log.e("Error Screen Card", "Falha ao capturar a imagem - " + e.message)
            }

            return screenshot
        }
    }
}