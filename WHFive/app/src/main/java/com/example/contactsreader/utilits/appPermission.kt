package com.example.contactsreader.utilits

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_WRITE = Manifest.permission.WRITE_CONTACTS
const val PERMISSION_REQUEST_WRITE = 200
/*функция проверки и запроса разрешения на редактирование*/
fun checkPermission(permission: String): Boolean {
    /* Функция принимает разрешение и проверяет, если разрешение еще не было
    * предоставлено запускает окно с запросом пользователю */
    return if (Build.VERSION.SDK_INT >= 23 &&
        ContextCompat.checkSelfPermission(
                APP_ACTIVITY,
                permission
            ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission, Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_WRITE)

        false
    } else true
}
