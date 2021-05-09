package com.jonathandarwin.workmanagertest.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

/**
 * Created By : Jonathan Darwin on May 09, 2021
 */ 
class AddWorker(context: Context, workerParam: WorkerParameters) : Worker(context, workerParam) {

    companion object {
        val NUM1 = "NUM1"
        val NUM2 = "NUM2"
        val NUM3 = "NUM3"
    }

    override fun doWork(): Result {
        val num1 = inputData.getInt(NUM1, -1)
        val num2 = inputData.getInt(NUM2, -1)
        val num3 = inputData.getInt(NUM3, -1)

        Thread.sleep(3000)

        return Result.success(workDataOf(
            MultiplyWorker.NUM1 to (num1 + num2),
            MultiplyWorker.NUM2 to num3
        ))
    }
}