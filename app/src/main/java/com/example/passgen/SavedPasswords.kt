package com.example.passgen

import android.content.ClipData
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import java.util.Random
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class SavedPasswords : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.saved_passwords)

        val toPasswordGen = findViewById<Button>(R.id.toPasswordGenerator)


        val currPass = intent.getStringExtra("message_key")
        val lastPass = intent.getStringExtra("last_pass")

        val clearButton = findViewById<Button>(R.id.clear)
        val savedPassFile = findViewById<TextView>(R.id.savedpass)

        var keep_list : List<String>

        try {
            var fileInputStream = openFileInput("saved_passwords.txt")
            val inputReader = InputStreamReader(fileInputStream)
            val output = inputReader.readText()
            // Data is displayed in the TextView
            savedPassFile.text = output

        } catch (e: Exception) {
            e.printStackTrace()
        }

        // to go back to main
        toPasswordGen.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("recent_pass", currPass)
            startActivity(intent)
        }

        clearButton.setOnClickListener {
            try {
                val file = File(filesDir, "saved_passwords.txt")
                val lines = file.readLines()

                // Only proceed if there are more than 4 lines
                if (lines.size >= 4) {
                    // Keep all lines except the last 4
                    val updatedContent = lines.dropLast(4)

                    // Write the remaining lines back to the file
                    file.writeText(updatedContent.joinToString("\n"))

                    // Update the TextView with the new content
                    savedPassFile.text = updatedContent.joinToString("\n")
                    Toast.makeText(baseContext, "Password successfully cleared!", Toast.LENGTH_SHORT).show()
                } else {
                    // If there are 4 or fewer lines, just clear the file
                    file.writeText("")
                    savedPassFile.text = ""
                    Toast.makeText(baseContext, "No passwords to clear!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}