package com.serena.data.preferences

import android.content.Context

class SessionManager(context: Context) {

    private val preferences = context.getSharedPreferences(
        "serena_sesion",
        Context.MODE_PRIVATE
    )

    fun saveSession(userId: Int, userName: String, userEmail: String) {
        preferences.edit()
            .putBoolean("logged", true)
            .putInt("user_id", userId)
            .putString("user_name", userName)
            .putString("user_email", userEmail)
            .apply()
    }

    fun isLogged(): Boolean = preferences.getBoolean("logged", false)

    fun getUserId(): Int = preferences.getInt("user_id", -1)

    fun getUserName(): String = preferences.getString("user_name", "") ?: ""

    fun getUserEmail(): String = preferences.getString("user_email", "") ?: ""

    fun clearSession() {
        preferences.edit().clear().apply()
    }
}