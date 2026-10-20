package com.serena.notifications


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SerenaRecordatorioWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            NotificacionHelper.mostrarRecordatorio(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}