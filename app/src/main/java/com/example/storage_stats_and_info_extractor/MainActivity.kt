package com.example.storage_stats_and_info_extractor

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storage_stats_and_info_extractor.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//        intent.data = Uri.parse("package:$packageName")
//        startActivity(intent)
        binding.btnMsg.setOnClickListener(){
            queryLlama("What is the capital of India?")
        }
        binding.btnShow.setOnClickListener(){
            getStorageDetails()
            getTotalDocumentsSize(contentResolver)
            getTotalImagesSize(contentResolver)
            getTotalVideosSize(contentResolver)
            getTotalAudiosSize(contentResolver)
            getTotalApksSize(contentResolver)
        }
    }
    fun getStorageDetails() {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong
        val totalStorage = totalBlocks * blockSize
        val usedStorage = totalStorage - (availableBlocks * blockSize)
        var percentage=(usedStorage.toDouble()/totalStorage.toDouble())*100
        Log.e("PhoneClone", "Total Storage: ${formatSize(totalStorage)}")
        Log.e("PhoneClone", "Used Storage: ${formatSize(usedStorage)}")
        Log.e("PhoneClone", "Percentage: ${percentage}")
    }
    fun formatSize(size: Long): String {
        val kb = size / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        return String.format("%.2f GB", gb)
    }
    fun getTotalImagesSize(contentResolver: ContentResolver) {
        Log.e("PhoneClone","Getting Images")
        CoroutineScope(Dispatchers.IO).launch{
        val imageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
           val projection = arrayOf(MediaStore.Images.Media.DATA)

           var totalSize: Long = 0
           val cursor: Cursor? = contentResolver.query(
               imageUri,
               projection,
               null,
               null,
               null
           )

           cursor?.use {
               val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
               while (it.moveToNext()) {
                   val filePath = it.getString(columnIndex)
                   val file = File(filePath)
                   if (file.exists()) {
                       totalSize += file.length() // Add image file size to total
                   }
               }
           }
           Log.e("PhoneClone","Total Images Size: ${formatSize(totalSize)}")
       }
    }

    private fun getTotalDocumentsSize(contentResolver: ContentResolver) {
        Log.e("PhoneClone","Getting Docs")
        CoroutineScope(Dispatchers.IO).launch {
            val uri: Uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
            val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} IN (?, ?, ?, ?)"
            val selectionArgs = arrayOf(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "text/plain"
            )

            var totalSize: Long = 0
            val cursor: Cursor? =
                contentResolver.query(uri, projection, selection, selectionArgs, null)

            cursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                while (it.moveToNext()) {
                    val filePath = it.getString(columnIndex)
                    val file = File(filePath)
                    if (file.exists()) {
                        totalSize += file.length() // Add file size to the total
                    }
                }
            }
            Log.e("PhoneClone", "Total Documents Size: ${formatSize(totalSize)}")
        }
    }
    fun getTotalVideosSize(contentResolver: ContentResolver) {
        Log.e("PhoneClone","Getting Videos")
        CoroutineScope(Dispatchers.IO).launch {
            val videoUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Video.Media.DATA)

            var totalSize: Long = 0
            val cursor: Cursor? = contentResolver.query(
                videoUri,
                projection,
                null,
                null,
                null
            )

            cursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                while (it.moveToNext()) {
                    val filePath = it.getString(columnIndex)
                    val file = File(filePath)
                    if (file.exists()) {
                        totalSize += file.length() // Add video file size to total
                    }
                }
            }
            Log.e("PhoneClone", "Total Videos Size: ${formatSize(totalSize)}")
        }
    }
    fun getTotalAudiosSize(contentResolver: ContentResolver) {
        Log.e("PhoneClone","Getting Audios")
        CoroutineScope(Dispatchers.IO).launch {
            val audioUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Audio.Media.DATA)

            var totalSize: Long = 0
            val cursor: Cursor? = contentResolver.query(
                audioUri,
                projection,
                null,
                null,
                null
            )

            cursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                while (it.moveToNext()) {
                    val filePath = it.getString(columnIndex)
                    val file = File(filePath)
                    if (file.exists()) {
                        totalSize += file.length() // Add audio file size to total
                    }
                }
            }
            Log.e("PhoneClone", "Total Audios Size: ${formatSize(totalSize)}")
        }
    }
    fun getTotalApksSize(contentResolver: ContentResolver) {
        Log.e("PhoneClone","Getting APKS")
        CoroutineScope(Dispatchers.IO).launch {
            val uri: Uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
            val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
            val selectionArgs =
                arrayOf("application/vnd.android.package-archive") // MIME type for APKs

            var totalSize: Long = 0
            val cursor: Cursor? =
                contentResolver.query(uri, projection, selection, selectionArgs, null)

            cursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                while (it.moveToNext()) {
                    val filePath = it.getString(columnIndex)
                    val file = File(filePath)
                    if (file.exists()) {
                        totalSize += file.length() // Add APK file size to total
                    }
                }
            }
            Log.e("PhoneClone", "Total APK's Size: ${formatSize(totalSize)}")
        }
    }
    fun checkdel(){
        CoroutineScope(Dispatchers.IO).launch {
            val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
            val selection = "${MediaStore.Files.FileColumns.DATA} LIKE ?"
            val selectionArgs = arrayOf("%deleted_folder%")

            val cursor = contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                selectionArgs,
                null
            )
            cursor?.use {
                while (it.moveToNext()) {
                    val filePath = it.getString(0)
                    println("Recoverable file: $filePath")
                }
            }

        }
    }
    fun queryLlama(prompt: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = LlamaRequest("llama2-13b", prompt)
                val response = RetrofitClient.instance.generateResponse(request)
                println("Llama Response: ${response.output}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}