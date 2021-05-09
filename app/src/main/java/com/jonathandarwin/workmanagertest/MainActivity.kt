package com.jonathandarwin.workmanagertest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.jonathandarwin.workmanagertest.worker.AddWorker
import com.jonathandarwin.workmanagertest.worker.MultiplyWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var workerId: UUID? = null
    private lateinit var workManager: WorkManager

    private var num1 = -1
    private var num2 = -1
    private var num3 = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(application)

        btnStart.setOnClickListener{
            hideKeyboard()
            try{
                num1 = etNumber1.text.toString().toInt()
                num2 = etNumber2.text.toString().toInt()
                num3 = etNumber3.text.toString().toInt()

                showToast("Please check the progress in your log")
                startTask()
            }
            catch (e: Exception) {
                showToast(e.message ?: "Error Please try again")
            }
        }

        btnCancel.setOnClickListener {
            workerId?.let {
                workManager.cancelWorkById(it)
                showToast("Work Cancelled")
            }
        }
    }

    private fun startTask() {
        // This data can be accessed in either AddWorker or MultiplyWorker
        val data = workDataOf(
            AddWorker.NUM1 to num1,
            AddWorker.NUM2 to num2,
            AddWorker.NUM3 to num3
        )

        // Prepare AddWorker
        val addWorker = OneTimeWorkRequest.Builder(AddWorker::class.java)
                    .setInputData(data)
                    .build()

        // Prepare MultiplyWorker
        val multiplyWorker = OneTimeWorkRequest.Builder(MultiplyWorker::class.java)
            .build()

        // Enqueue Worker
        workManager
            .beginWith(addWorker)
            .then(multiplyWorker)
            .enqueue()

        // Listen for AddWorker State
        workManager.getWorkInfoByIdLiveData(addWorker.id).observe(this, Observer {
            when(it.state) {
                WorkInfo.State.ENQUEUED -> logWorker("AddWorker Enqueued")
                WorkInfo.State.RUNNING -> {
                    logWorker("AddWorker Running")

                    workerId = addWorker.id
                    btnCancel.visibility = View.VISIBLE
                    btnCancel.text = "Cancel Add Worker"
                }
                WorkInfo.State.SUCCEEDED -> {
                    logWorker("AddWorker Succeeded")

                    btnCancel.visibility = View.GONE
                }
                WorkInfo.State.CANCELLED -> {
                    logWorker("AddWorker Canceled")

                    btnCancel.visibility = View.GONE
                }
                else -> logWorker("AddWorker on Other State")
            }
        })

        // Listen for MultiplyWorker State
        workManager.getWorkInfoByIdLiveData(multiplyWorker.id).observe(this, Observer {
            when(it.state) {
                WorkInfo.State.ENQUEUED -> logWorker("MultiplyWorker Enqueued")
                WorkInfo.State.RUNNING -> {
                    logWorker("MultiplyWorker Running")

                    workerId = multiplyWorker.id
                    btnCancel.visibility = View.VISIBLE
                    btnCancel.setText("Cancel Multiply Worker")
                }
                WorkInfo.State.SUCCEEDED -> {
                    logWorker("MultiplyWorker Succeeded")

                    btnCancel.visibility = View.GONE

                    // Get Result From MultiplyWorker
                    val result = it.outputData.getInt(MultiplyWorker.RESULT, -1)

                    // Go to ResultActivity
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra(ResultActivity.EXTRA_RESULT, result)
                    startActivity(intent)
                }
                WorkInfo.State.CANCELLED -> {
                    logWorker("MultiplyWorker Canceled")

                    btnCancel.visibility = View.GONE
                }
                else -> logWorker("MultiplyWorker on Other State")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logWorker(message: String) {
        Log.d("<WORKER>", message)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = currentFocus

        if(v != null) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}