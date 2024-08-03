package com.github.dbconnection

import android.database.sqlite.SQLiteDatabase
import com.github.dbconnection.entity.Registration

interface DbInterface {

    fun insertRegistration(registration: Registration): Boolean;

    fun listRegistrations(): String;

    fun updateRegistration(registration: Registration): Boolean;

    fun findRegistration(cod: String): Registration?;

    fun deleteRegistration(cod: String): Boolean;
}