package com.serena.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object RecordatorioScheduler {
    fun programar(context: Context) {
        // Le pasamos el 2 y el MINUTES directamente
        val solicitud = PeriodicWorkRequestBuilder<SerenaRecordatorioWorker>(15 , TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "recordatorio_diario_serena",
            ExistingPeriodicWorkPolicy.REPLACE, // <-- Cambiado a REPLACE para evitar el error rojo
            solicitud
        )
    }
}