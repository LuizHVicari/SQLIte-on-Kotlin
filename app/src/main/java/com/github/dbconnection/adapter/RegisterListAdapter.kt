package com.github.dbconnection.adapter

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.github.dbconnection.MainActivity
import com.github.dbconnection.R
import com.github.dbconnection.database.DatabaseHandler.Companion.COD
import com.github.dbconnection.database.DatabaseHandler.Companion.NAME
import com.github.dbconnection.database.DatabaseHandler.Companion.PHONE
import com.github.dbconnection.entity.Registration

class RegisterListAdapter(private val context: Context, private val cursor: Cursor): BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)
        val register = Registration(
            cursor.getInt(COD),
            cursor.getString(NAME),
            cursor.getString(PHONE)
        )
        return register
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return cursor.getLong(COD)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.list_element, null)

        val tvElementListName = v.findViewById<TextView>(R.id.tvListName)
        val tvElementListPhone = v.findViewById<TextView>(R.id.tvListPhone)

        val tvEditListElement = v.findViewById<ImageButton>(R.id.btListEdit)

        tvEditListElement.setOnClickListener{
            cursor.moveToPosition(position)

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("cod", cursor.getInt(COD))
            intent.putExtra("name", cursor.getString(NAME))
            intent.putExtra("phone", cursor.getString(PHONE))
            context.startActivity(intent)
        }

        cursor.moveToPosition(position)
        val name = this.cursor.getString(NAME)
        val phone = this.cursor.getString(PHONE)
        tvElementListName.text = name.toString()
        tvElementListPhone.text = phone.toString()

        return v
    }
}