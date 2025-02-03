package com.ygha.application

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts


//개별 권한에 대해 처리하는 예제
class PermissionActivityTwo : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_two)

        (findViewById<TextView>(R.id.request)).setOnClickListener {

            checkAndRequestPermissions()
        }
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // 특정 권한 확인
        val isCameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val isReadStorageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        val isWriteStorageGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false

        if (isCameraGranted && isReadStorageGranted && isWriteStorageGranted) {
            onPermissionsGranted()
        } else {
            // 권한별로 처리
            if (!isCameraGranted) {
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
            if (!isReadStorageGranted || !isWriteStorageGranted) {
                Toast.makeText(this, "외장 메모리 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAndRequestPermissions() {
        requestPermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    private fun onPermissionsGranted() {
        Toast.makeText(this, "모든 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        // 권한이 허용되었을 때 실행할 작업
    }
}