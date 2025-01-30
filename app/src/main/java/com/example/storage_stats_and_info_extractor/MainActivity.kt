package com.example.storage_stats_and_info_extractor

import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storage_stats_and_info_extractor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnShow.setOnClickListener(){
            getStorageDetails()
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
}