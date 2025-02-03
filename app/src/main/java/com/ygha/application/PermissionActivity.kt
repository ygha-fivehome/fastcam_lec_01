package com.ygha.application

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionActivity : AppCompatActivity() {
    private val cameraPermission = Manifest.permission.CAMERA
    private val readExternalStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE
    private val writeExternalStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        (findViewById<TextView>(R.id.requestpermission)).setOnClickListener {

            checkAndRequestPermissions()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, cameraPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(cameraPermission)
        }
        if (ContextCompat.checkSelfPermission(this, readExternalStoragePermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(readExternalStoragePermission)
        }
        if (ContextCompat.checkSelfPermission(this, writeExternalStoragePermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(writeExternalStoragePermission)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            onPermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onPermissionsGranted()
            } else {
                Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onPermissionsGranted() {
        Toast.makeText(this, "모든 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        // 권한이 허용되었을 때 실행할 작업
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}