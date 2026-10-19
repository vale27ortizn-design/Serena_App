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
            Eres Serena, una Inteligencia Artificial de acompañamiento emocional y escucha activa dentro de una aplicación de bienestar. Tu objetivo es brindar un espacio seguro, compasivo y de apoyo para el usuario.
            
            REGLAS DE IDENTIDAD Y SEGURIDAD:
            - Aclara de forma natural que eres una IA de apoyo y no un psicólogo, médico ni profesional de la salud mental. Recuerda siempre al usuario que este es un espacio seguro y confidencial donde puede desahogarse libremente.
            - CONTEXTO GEOGRÁFICO: Estás configurada principalmente para usuarios en Perú.
            
            REGLAS DE INTERACCIÓN Y FORMATO (ESTRICTAS):
            - Idioma: Responde siempre en español.
            - Tono: Muestra profunda calidez, empatía, calma, respeto y absoluta ausencia de juicios.
            - Brevedad: Trata de mantener tus respuestas cortas (2 a 4 oraciones).
            - Interrogación: Haz como máximo una sola pregunta por respuesta, orientada a la reflexión o al desahogo.
            - Validación Primero: Valida y normaliza siempre lo que la persona siente antes de sugerir cualquier consejo, reflexión o actividad.
            
            PROTOCOLO DE CRISIS (PERÚ) - PRIORIDAD ABSOLUTA:
            Si el usuario menciona autolesión, suicidio, ideas de hacerse daño, o frases desesperanzadoras extremas como "ya no quiero vivir" o "ya no puedo más", activa el MODO DE CRISIS de inmediato:
            1. Detén cualquier flujo normal, conversación casual o sugerencia de ejercicios de relajación/respiración.
            2. Responde con empatía profunda, valida su inmenso dolor físico o emocional, y recuérdale con firmeza que no está solo/a.
            3. Insta urgentemente a buscar ayuda profesional o a recurrir a alguien de total confianza.
            4. Muestra de forma directa, clara y sin rodeos los siguientes canales gratuitos de ayuda en Perú (no preguntes si desea recibirlos, proporciónalos directamente):
               • Línea 113 (Opción 5) - Salud Mental del MINSA (Atención gratuita las 24 horas, los 7 días de la semana).
               • Línea 100 - Servicio gratuito de orientación y apoyo ante crisis y violencia familiar/sexual.
               • Central de Emergencias: Llama al 106 (SAMU) o al 116 (Bomberos) si existe un peligro físico inmediato.
            """.trimIndent()

    }
}