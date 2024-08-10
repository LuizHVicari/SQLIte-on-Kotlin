package com.github.dbconnection.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.dbconnection.DbInterface
import com.github.dbconnection.entity.Registration


class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), DbInterface {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "registration"
        const val COD = 0
        const val NAME = 1
        const val PHONE = 2

    }

    override fun insertRegistration(registration: Registration): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", registration.name)
        values.put("phone", registration.phone)
        return db.insert(TABLE_NAME, null, values) != (-1).toLong()
    }

    override fun updateRegistration(registration: Registration): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", registration.name)
        values.put("phone", registration.phone)
        return db.update(TABLE_NAME, values, "_id=${registration._id}", null) > 0
    }

    override fun deleteRegistration(cod: String): Boolean {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "_id=$cod", null) > 0
    }

    override fun findRegistration(cod: String): Registration? {
        val db = this.writableDatabase
        val cursor = db.query("registration", null, "_id=$cod", null, null, null, null)

        if (cursor.moveToNext()) {
            val reg= Registration(cursor.getInt(COD), cursor.getString(NAME), cursor.getString(PHONE))
            cursor.close()
            return reg
        }
        cursor.close()
        return null
    }

    override fun listRegistrations(): String {
        val db = this.writableDatabase
        val cursor = db.query("registration", null, null, null, null, null, null)
        val output = this.appendRegistrationToString(cursor)
        cursor.close()
        return output
    }

    fun cursorList(): Cursor {
        val db = this.writableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        return cursor
    }

    private fun appendRegistrationToString( cursor: Cursor): String {
        val output = StringBuilder()
        while (cursor.moveToNext()) {
            output.append(cursor.getInt(0))
            output.append(" - ")
            output.append(cursor.getInt(1))
            output.append(" - ")
            output.append(cursor.getInt(2))
            output.append("\n")
        }
        return output.toString()
    }
}