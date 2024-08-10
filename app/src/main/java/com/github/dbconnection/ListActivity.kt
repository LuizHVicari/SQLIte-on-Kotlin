package com.github.dbconnection

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dbconnection.adapter.RegisterListAdapter
import com.github.dbconnection.database.DatabaseHandler
import com.github.dbconnection.databinding.ActivityListBinding
import com.github.dbconnection.databinding.ActivityMainBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var db: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ftBtnCreate.setOnClickListener{
            ftBtnCreateOnClick()
        }

        binding.btSearch.setOnClickListener{
            btSearchOnClick()
        }

        getScreenItems()

    }

    private fun btSearchOnClick() {
        val cod = binding.etSearch.text.toString()
        if (cod.isEmpty()) {
            Toast.makeText(this, "Por favor, informe um código", Toast.LENGTH_LONG).show()
            return
        }

        val registration = db.findRegistration(cod)
        var registrationString = ""
        if (registration != null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cadastro encontrado")
            builder.setCancelable(false)

            registrationString = "${registration._id} - ${registration.name}: ${registration.phone}\n"
            builder.setPositiveButton("Ok!", null)
            builder.setMessage(registrationString)
            builder.show()
        } else {
            Toast.makeText(this, "Cadastro não encontrado", Toast.LENGTH_LONG).show()
        }
    }

    private fun ftBtnCreateOnClick() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        getScreenItems()
    }

    private fun getScreenItems() {
        db = DatabaseHandler(this)

        val registers = db.cursorList()
        val adapter = RegisterListAdapter(this, registers)

        binding.lvMain.adapter = adapter
    }
}