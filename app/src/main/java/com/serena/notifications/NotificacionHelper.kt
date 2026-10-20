package com.serena.notifications


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.serena.R

object NotificacionHelper {
    private const val CHANNEL_ID = "recordatorios_serena"
    private const val NOTIFICATION_ID = 1001

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios Diarios",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Aviso para registrar tus emociones del día"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    fun mostrarRecordatorio(context: Context) {
        crearCanal(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val notificacion = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_hoja) // Puedes cambiarlo por icon_corazon o tu logo
            .setContentTitle("Momento de pausa 🍃")
            .setContentText("¿Cómo te sientes hoy? Tómate un minuto para ti y registra tu emoción.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("¿Cómo te sientes hoy? Tómate un minuto para ti y registra tu emoción en tu diario."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notificacion)
    }
}