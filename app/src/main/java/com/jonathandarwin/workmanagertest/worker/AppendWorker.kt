package com.jonathandarwin.workmanagertest.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.jonathandarwin.workmanagertest.util.Database
import kotlinx.coroutines.delay

/**
 * Created By : Jonathan Darwin on August 31, 2023
 */
class AppendWorker(
    context: Context,
    workerParam: WorkerParameters
) : CoroutineWorker(context, workerParam) {

    override suspend fun doWork(): Result {

        try {
            while (!Database.isQueueEmpty()) {
                val name = Database.getQueue()

                for(i in 1..5) {
                    setProgress(
                        workDataOf(
                            "name" to name,
                            "progress" to i
                        )
                    )

                    if (name.toInt() == 3 && i == 3 && Database.isForceError) {
                        Database.isForceError = false
                        throw Exception()
                    }

                    delay(1000)
                }

                Database.popQueue()
            }
        } catch (throwable: Throwable) {
            println("JOE LOG error $throwable")
        }




        return Result.success()
    }
}