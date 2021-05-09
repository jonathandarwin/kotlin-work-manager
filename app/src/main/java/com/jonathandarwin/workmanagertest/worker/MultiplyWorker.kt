package com.jonathandarwin.workmanagertest.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.jonathandarwin.workmanagertest.util.NotificationUtil

/**
 * Created By : Jonathan Darwin on May 09, 2021
 */
class MultiplyWorker(private val context: Context, workerParam: WorkerParameters) : Worker(context, workerParam) {

    companion object {
        val NUM1 = "NUM1"
        val NUM2 = "NUM2"

        val RESULT = "RESULT"
    }

    override fun doWork(): Result {

        val num1 = inputData.getInt(NUM1, -1)
        val num2 = inputData.getInt(NUM2, -1)
        val result = (num1 * num2)

        Thread.sleep(3000)

        NotificationUtil.showNotification(
            context,
            "Hey! Your work already finished",
            "Yeay~",
            result
        )

        return Result.success(workDataOf(RESULT to result))
    }
}