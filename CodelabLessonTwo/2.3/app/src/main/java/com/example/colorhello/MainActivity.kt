package com.example.colorhello

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*


class MainActivity : AppCompatActivity() {
    private var mHelloTextView: TextView? = null

    private val mColorArray = arrayOf(
        "red", "pink", "purple", "deep_purple",
        "indigo", "blue", "light_blue", "cyan", "teal", "green",
        "light_green", "lime", "yellow", "amber", "orange", "deep_orange",
        "brown", "grey", "blue_grey", "black"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHelloTextView = findViewById(R.id.hello_textview)

        if (savedInstanceState != null) {
            mHelloTextView?.setTextColor(savedInstanceState.getInt("color"));
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("color", mHelloTextView!!.getCurrentTextColor())
    }
    fun changeColor(view: View?) {
        val random = Random()
        val colorName = mColorArray[random.nextInt(20)]

        val colorResourceName = resources.getIdentifier(
            colorName,
            "color", applicationContext.packageName
        )

        val colorRes = ContextCompat.getColor(this, colorResourceName)

        mHelloTextView!!.setTextColor(colorRes)
    }
}
