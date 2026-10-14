package com.serena.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.TimeZone

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val fechaRegistro: Long,
)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                clave_hash TEXT NOT NULL,
                salt TEXT NOT NULL,
                fecha_registro INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE actividad (
                usuario_id INTEGER NOT NULL,
                dia INTEGER NOT NULL,
                PRIMARY KEY (usuario_id, dia),
                FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
            )
        """.trimIndent())

    } override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS actividad")

        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }


    // ---------------- REGISTER ----------------

    fun existeCorreo(correo: String): Boolean {
        readableDatabase.rawQuery(
            "SELECT 1 FROM usuarios WHERE correo = ?",
            arrayOf(correo.trim().lowercase())
        ).use { return it.moveToFirst() }
    }

    /** Retorna el id del nuevo usuario, o -1 si falló (ej. correo duplicado). */
    fun registrarUsuario(nombres: String, correo: String, clave: String): Long {
        val salt = generarSalt()
        val values = ContentValues().apply {
            put("nombre", nombres.trim())
            put("correo", correo.trim().lowercase())
            put("clave_hash", hash(clave, salt))
            put("salt", salt)
            put("fecha_registro", System.currentTimeMillis())
        }
        return writableDatabase.insert("usuarios", null, values)
    }

    // ---------------- LOGIN ----------------

    /** Retorna el Usuario si correo y clave son correctos,  SINO NULL. */
    fun login(correo: String, clave: String): Usuario? {
        readableDatabase.rawQuery(
            "SELECT id, nombre, correo, clave_hash, salt, fecha_registro FROM usuarios WHERE correo = ?",
            arrayOf(correo.trim().lowercase())
        ).use { c ->
            if (!c.moveToFirst()) return null
            val hashGuardado = c.getString(3)
            val salt = c.getString(4)
            return if (hash(clave, salt) == hashGuardado) {
                Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getLong(5))
            } else null
        }
    }

    //perfil
    fun obtenerUsuario(id: Int): Usuario? {
        readableDatabase.rawQuery(
            "SELECT id, nombre, correo, fecha_registro FROM usuarios WHERE id = ?",
            arrayOf(id.toString())
        ).use { c ->
            return if (c.moveToFirst()) {
                Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getLong(3))
            } else null
        }
    }

    // Número de día local (evita que el día cambie a las 7 pm por UTC en Perú)
    private fun diaActual(): Long {
        val ahora = System.currentTimeMillis()
        return (ahora + TimeZone.getDefault().getOffset(ahora)) / 86_400_000L
    }

    // Marca el día de hoy como "usó la app". Si ya existe, lo ignora
    fun registrarActividad(usuarioId: Int) {
        if (usuarioId <= 0 || !existeUsuario(usuarioId)) return
        try {
            val values = ContentValues().apply {
                put("usuario_id", usuarioId)
                put("dia", diaActual())
            }
            writableDatabase.insertWithOnConflict(
                "actividad", null, values, SQLiteDatabase.CONFLICT_IGNORE
            )
        } catch (e: Exception) {
            android.util.Log.e("DatabaseHelper", "Error registrando actividad", e)
        }
    }

    //Días consecutivos hasta hoy
    fun calcularRacha(usuarioId: Int): Int {
        val dias = mutableListOf<Long>()
        readableDatabase.rawQuery(
            "SELECT dia FROM actividad WHERE usuario_id = ? ORDER BY dia DESC",
            arrayOf(usuarioId.toString())
        ).use { c ->
            while (c.moveToNext()) dias.add(c.getLong(0))
        }

        if (dias.isEmpty()) return 0

        val hoy = diaActual()
        if (dias[0] != hoy && dias[0] != hoy - 1) return 0   // racha rota

        var racha = 1
        for (i in 1 until dias.size) {
            if (dias[i - 1] - dias[i] == 1L) racha++ else break
        }
        return racha
    }

    fun existeUsuario(id: Int): Boolean {
        readableDatabase.rawQuery(
            "SELECT 1 FROM usuarios WHERE id = ?",
            arrayOf(id.toString())
        ).use { return it.moveToFirst() }
    }

    // ---------------- SEGURIDAD ----------------

    //Las bases de datos de las aplicaciones (incluyendo este archivo de SQLite
    //que se guarda en el celular del usuario) pueden ser robadas o comprometidas.
    //encriptado
    private fun generarSalt(): String {
        val bytes = ByteArray(16).also { SecureRandom().nextBytes(it) }
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
    //Este algoritmo toma cualquier texto y lo convierte en una cadena fija
    //de 64 caracteres. Es de una sola vía: puedes obtener el hash desde la contraseña, p
    //ero no puedes descifrar la contraseña original desde el hash.
    private fun hash(clave: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest((salt + clave).toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(digest, Base64.NO_WRAP)
    }
    companion object {
        private const val DATABASE_NAME = "serena_app.db"
        private const val DATABASE_VERSION = 3   // antes: 2
    }
}