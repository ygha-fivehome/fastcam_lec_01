package com.ygha.application.instagram

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.ygha.application.R
import com.ygha.application.instagram.fragment.InstaFeedFragment
import com.ygha.application.instagram.fragment.InstaPostFragment
import com.ygha.application.instagram.fragment.InstaProfileFragment

/**
 * app:tabIndicatorHeight="0dp"
 *
 * 이속성에 0을 줘야 indicatior에 줄이 안 생긴다.
 */

class InstaMainActivity : AppCompatActivity() {

    val instaPostFragment = InstaPostFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insta_main)

        val tabs = findViewById<TabLayout>(R.id.main_tab)

        tabs.addTab(tabs.newTab().setIcon(R.drawable.baseline_home_24))
        tabs.addTab(tabs.newTab().setIcon(R.drawable.baseline_library_add_24))
        tabs.addTab(tabs.newTab().setIcon(R.drawable.baseline_face_24))

        val pager = findViewById<ViewPager2>(R.id.main_pager)
        pager.adapter = InstaMainPagerAdapter(this, 3, instaPostFragment)

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                pager.setCurrentItem(tab!!.position)
                if(tab!!.position == 1){
                    instaPostFragment.makePost()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        checkAndRequestPermissions()

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



class InstaMainPagerAdapter(
    fragmentActivity:FragmentActivity,
    val tabCount:Int,
    val instaPostFragment: InstaPostFragment
):FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0->return InstaFeedFragment()

            1-> {
                return instaPostFragment
            }

            else ->return InstaProfileFragment()
        }
    }
}