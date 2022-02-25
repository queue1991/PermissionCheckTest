package com.hsj.permissionchecktest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.Settings
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val MULTIPLE_PERMISSIONS = 10

    private val permissions = arrayListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission(permissions)
    }

    /**
     * 퍼미션 확인
     */
    private fun checkPermission(permissions : ArrayList<String>){
        val listPermissionsNeeded = ArrayList<String>()

        for(permission in permissions){
            if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)

            else listPermissionsNeeded.add(permission)
        }

        if(!listPermissionsNeeded.isEmpty())
            requestPermission(listPermissionsNeeded)
    }

    /**
     * 퍼미션 요청
     */
    private fun requestPermission(listPermissionsNeeded : ArrayList<String>){
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(),MULTIPLE_PERMISSIONS );
    }

    /**
     * 퍼미션 요청 Response 콜백
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            MULTIPLE_PERMISSIONS -> {
                var deniedSize = 0

                for(permission in permissions){
                    for(permission in permissions){
                        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
                            deniedSize++
                    }
                }

                if (deniedSize > 0) {
                    startActivity(appSettings(this))

                    // 왜 세팅 페이지 들어가는지 설명 필요.
                    Toast.makeText(
                        this,
                        "세팅이 그래서 필요합니다.",
                        Toast.LENGTH_LONG
                    )
                }
            }
        }
    }

    fun appSettings(context: Context): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + context.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }

}