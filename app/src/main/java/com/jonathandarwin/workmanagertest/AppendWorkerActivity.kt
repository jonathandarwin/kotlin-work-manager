package com.jonathandarwin.workmanagertest

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.jonathandarwin.workmanagertest.util.Database
import com.jonathandarwin.workmanagertest.worker.AppendWorker
import kotlinx.android.synthetic.main.activity_append_worker.btnStart
import kotlinx.android.synthetic.main.activity_append_worker.tvProgress

/**
 * Created By : Jonathan Darwin on August 31, 2023
 */
class AppendWorkerActivity : AppCompatActivity() {

    private var numOfWorker = 1

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_append_worker)

        workManager = WorkManager.getInstance(application)

        workManager
            .getWorkInfosForUniqueWorkLiveData("APPEND_WORKER")
            .observe(this, Observer {

                it.forEach {
                    println("JOE LOG $it")
                }
                it.filter { workInfo -> workInfo.state == WorkInfo.State.RUNNING }
                    .forEach { workInfo ->
                        if(workInfo.state == WorkInfo.State.RUNNING) {
                            val name = workInfo.progress.getString("name")
                            val progress = workInfo.progress.getInt("progress", 0)

                            val text = "$name - $progress"
                            tvProgress.text = text
                        }
                    }
            })

        btnStart.setOnClickListener {

            Database.putQueue("$numOfWorker")

            numOfWorker++

            // Prepare AddWorker
            val workerRequest = OneTimeWorkRequest.Builder(AppendWorker::class.java)
                .build()

            // Enqueue Worker
            workManager.enqueueUniqueWork(
                "APPEND_WORKER",
                ExistingWorkPolicy.KEEP,
                workerRequest
            )
        }
    }
}