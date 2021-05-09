package com.jonathandarwin.workmanagertest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*

/**
 * Created By : Jonathan Darwin on May 09, 2021
 */ 
class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESULT = "EXTRA_RESULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val result = intent.getIntExtra(EXTRA_RESULT, -1)
        tvResult.text = result.toString()
    }
}