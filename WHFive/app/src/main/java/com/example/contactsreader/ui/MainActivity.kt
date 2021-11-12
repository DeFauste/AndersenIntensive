package com.example.contactsreader.ui

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.contactsreader.R
import com.example.contactsreader.databinding.ActivityMainBinding
import com.example.contactsreader.ui.model.MainViewModel
import com.example.contactsreader.utilits.APP_ACTIVITY
import com.example.contactsreader.utilits.PERMISSION_REQUEST_WRITE
import com.example.contactsreader.utilits.READ_WRITE
import com.example.contactsreader.utilits.checkPermission

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        APP_ACTIVITY = this

        /*Проверяем планшет ли, если нет, то включаем навигацию*/
        if (!viewModel.isTablet) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            setupActionBarWithNavController(navController)
        }

        /*проверяем наличие разрешений на запись и чтение контактов*/
        if (checkPermission(READ_WRITE)) firstUpdate()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_WRITE -> {
                firstUpdate()
            }
        }
    }

    /*первично обновляем список*/
    private fun firstUpdate() {
        if (!viewModel.listIsUpdate) {
            viewModel.getAllPhoneContacts()
            viewModel.listIsUpdate = true
        }
    }

    /*сбрасываем фокус с клавиатуры*/
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
