package com.serena.data.repository

import android.util.Log
import com.serena.data.remote.GroqApiService
import com.serena.data.remote.GroqException
import com.serena.ui.model.ChatMessage
import kotlinx.coroutines.CancellationException
import java.io.IOException

class ChatRepository(
    private val api: GroqApiService = GroqApiService()
) {

    suspend fun sendMessage(history: List<ChatMessage>): Result<String> {
        // Por seguridad, la conversación enviada empieza con un mensaje del usuario
        // (se descarta el saludo inicial del bot) y se limita el contexto.
        val context = history
            .dropWhile { !it.isFromUser }
            .takeLast(MAX_CONTEXT_MESSAGES)

        return try {
            Result.success(api.generate(SYSTEM_PROMPT, context))
        } catch (e: CancellationException) {
            throw e
        } catch (e: GroqException) {
            Log.e(TAG, "GroqException httpCode=${e.httpCode}", e)
            Result.failure(Exception(e.toFriendlyMessage()))
        } catch (e: IOException) {
            Log.e(TAG, "IOException: ${e::class.java.simpleName}: ${e.message}", e)
            Result.failure(Exception("Sin conexión. Revisa tu internet e inténtalo de nuevo."))
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            Result.failure(Exception("Algo salió mal. Inténtalo de nuevo."))
        }
    }

    private fun GroqException.toFriendlyMessage() = when (httpCode) {
        GroqApiService.MISSING_KEY -> "Falta la API key. Agrégala en local.properties y vuelve a compilar."
        400 -> "La solicitud no es válida. Revisa el nombre del modelo en GroqApiService.MODEL."
        401, 403 -> "No se pudo autenticar con el servicio. Revisa la API key."
        404 -> "El modelo configurado no existe. Revisa GroqApiService.MODEL."
        429 -> "Hay muchas solicitudes. Espera un momento e inténtalo otra vez."
        in 500..599 -> "El servicio no está disponible ahora. Inténtalo más tarde."
        GroqApiService.NO_CONTENT -> "No pude responder a eso. ¿Puedes decirlo de otra forma?"
        else -> "Algo salió mal. Inténtalo de nuevo."
    }

    private companion object {
        const val TAG = "Serena"
        const val MAX_CONTEXT_MESSAGES = 20

        val SYSTEM_PROMPT = """
            Eres Serena, una Inteligencia Artificial de acompañamiento emocional dentro de una aplicación de bienestar.
            
            REGLAS GENERALES Y ROL:
            - Siempre aclara o ten presente que eres una IA de apoyo y no un psicólogo, médico ni profesional de la salud mental. Sin embargo, recuerda al usuario que este es un espacio seguro donde puede desahogarse libremente.
            - Responde siempre en español, con calidez, empatía, calma y sin juzgar.
            - Mantén respuestas breves (de 1 a 4 oraciones) y haz como máximo una pregunta por respuesta.
            - Valida siempre lo que la persona siente antes de sugerir cualquier actividad.
        
            CONTEXTO LOCAL Y EJERCICIOS (PERÚ):
            - Estás configurada para usuarios en Perú.
        
            LÍNEAS DE EMERGENCIA Y CRISIS EN PERÚ:
            - Si la persona menciona autolesión, suicidio, ideas de hacerse daño o que "ya no puede más", ACTIVA MODO DE CRISIS inmediatamente:
              1. Responde con profunda empatía y validación.
              2. Recuérdale que no está sola/o e insiste en que busque ayuda profesional o de alguien de confianza de inmediato.
              3. Proporciona los números gratuitos de emergencia e intervención en crisis de Perú sin preguntarle al usuario si esta de acuerdo en compartirselos:
                 • Línea 113 (Opción 5 - Salud Mental del MINSA, disponible 24/7).
                 • Línea 100 (Orientación y apoyo en crisis / violencia).
                 • Llama al 106 (SAMU) o 116 (Bomberos) si hay peligro inmediato.
              4. NO sugieras ejercicios de respiración ni continúes la conversación con normalidad mientras la persona siga en riesgo.
        """.trimIndent()
    }
}