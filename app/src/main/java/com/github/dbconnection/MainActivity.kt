package com.github.dbconnection

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dbconnection.database.DatabaseHandler
import com.github.dbconnection.databinding.ActivityMainBinding
import com.github.dbconnection.entity.Registration

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SQLiteDatabase
    private lateinit var dbInterface: DbInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setButtonListeners()

        db = SQLiteDatabase.openOrCreateDatabase(
           this.getDatabasePath("dbConnection.sqlite"),
            null
        )
        dbInterface = DatabaseHandler(this)
    }

    private fun setButtonListeners() {
        binding.btInclude.setOnClickListener {
            btIncludeOnCLick()
        }

        binding.btChange.setOnClickListener {
            btChangeOnCLick()
        }

        binding.btDelete.setOnClickListener {
            btDeleteOnCLick()
        }

        binding.btSearch.setOnClickListener {
            btSearchOnCLick()
        }

        binding.btList.setOnClickListener {
            btListOnCLick()
        }
    }

    private fun btListOnCLick() {
        val registrations = dbInterface.listRegistrations()
        this.displayRegistrationsOnToast(registrations)
    }

    private fun btSearchOnCLick() {
        val cod = binding.etCode.text.toString()
        val registration = dbInterface.findRegistration(cod)
        var registrationString = ""
        if (registration != null) {
            registrationString = "${registration._id} - ${registration.name} = ${registration.phone}\n"
        }
        this.displayRegistrationsOnToast(registrationString)
    }

    private fun btDeleteOnCLick() {
        if (!validateCodField()) {
            return
        }
        val cod = binding.etCode.text.toString()
        val success = dbInterface.deleteRegistration(cod)
        displaySuccessToast(success, "deletado")
    }

    private fun btChangeOnCLick() {
        if  (!validateCodField()) {
            return
        }
        if (!validateInputFields()) {
            return
        }
        val cod = binding.etCode.text.toString()
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()
        val success = this.dbInterface.updateRegistration(Registration(cod.toInt(), name, phone))
        displaySuccessToast(success, "atualizado")
    }

    private fun btIncludeOnCLick() {
        if (!validateInputFields()) {
            return
        }
        val name = binding.etName.text.toString()
        val phone = binding.etPhone.text.toString()

        val inserted = dbInterface.insertRegistration(Registration(null, name, phone))
        if (inserted) {
            Toast.makeText(this,"Incluído com sucesso", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_LONG).show()
        }
    }

    private fun displayRegistrationsOnToast(registrations: String) {
        if( registrations.isNotBlank()) {
            Toast.makeText(this, registrations, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Não há registros", Toast.LENGTH_LONG).show()
        }
    }

    private fun displaySuccessToast(success: Boolean, verb: String) {
        if (success) {
            Toast.makeText(this, "Registro $verb", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Registro não encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateCodField(): Boolean{
        if (binding.etCode.text.toString().isEmpty()) {
            Toast.makeText(this, "O campo cod não pode estar vazio", Toast.LENGTH_LONG).show()
            binding.etCode.requestFocus()
            return false
        }
        if (binding.etCode.text.toString().toIntOrNull() == null) {
            Toast.makeText(this, "O campo cod deve ser um número", Toast.LENGTH_LONG).show()
            binding.etCode.setText("")
            binding.etCode.requestFocus()
            return false
        }
        return true
    }

    private fun validateInputFields(): Boolean {
        if (binding.etName.text.toString().isEmpty()) {
            Toast.makeText(this, "O campo nome não pode estar vazio", Toast.LENGTH_LONG).show()
            binding.etName.requestFocus()
            return false
        }
        if (binding.etPhone.text.toString().isEmpty()) {
            Toast.makeText(this, "O campo telefone não pode estar vazio", Toast.LENGTH_LONG).show()
            binding.etPhone.requestFocus()
            return false
        }
        return true

    }
}