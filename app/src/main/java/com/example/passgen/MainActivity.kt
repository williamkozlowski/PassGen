package com.example.passgen

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Random



class MainActivity : ComponentActivity() {

    var currPass: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        val rnd = Random()

        val passwordOut = findViewById<TextView>(R.id.textViewDisplayTextChange)
        val newPassButton = findViewById<Button>(R.id.buttonDisplayTextChange)
        val copyButton = findViewById<Button>(R.id.buttonCopyToClipboard)
        val toSavedPasswords = findViewById<Button>(R.id.toSavedPasswords)
        val saveButton = findViewById<Button>(R.id.buttonSave)

        var saved_passes = ""
        var saved_title = ""
        var saved_username = ""

        try {
            var fileInputStream = openFileInput("saved_passwords.txt")
            val inputReader = InputStreamReader(fileInputStream)
            val output = inputReader.readText()
            // Data is displayed in the TextView
            saved_passes = output

        } catch (e: Exception) {
            e.printStackTrace()
        }



        if (intent.getStringExtra("recent_pass") == null) {

            currPass = ""

            var i = 0

            while (i < 16) {
                currPass += (rnd.nextInt(93) + 33).toChar()
                i++
            }
            passwordOut.text = "pass: " + currPass
        } else {
            currPass = intent.getStringExtra("recent_pass").toString()
            passwordOut.text = "pass: " + currPass
        }

        toSavedPasswords.setOnClickListener {

            val intent: Intent = Intent(this, SavedPasswords::class.java)
            intent.putExtra("message_key", currPass)
            startActivity(intent)
        }

        // Password generator
        newPassButton.setOnClickListener {

            currPass = ""
            var i = 0

            while (i < 16) {
                currPass += (rnd.nextInt(93) + 33).toChar()
                i++
            }
            passwordOut.text = "pass: " + currPass
        }

        // Copy button
        copyButton.setOnClickListener {

            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("EditText", currPass)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this@MainActivity, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
        }

        saveButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
            val editTitle = dialogLayout.findViewById<EditText>(R.id.edit_title)
            val editUsername = dialogLayout.findViewById<EditText>(R.id.edit_username)

            with (builder) {
                setTitle("Enter Title & Username")
                setPositiveButton("SAVE") {dialog, which ->
                    saved_title = editTitle.text.toString() //grabs username
                    saved_username = editUsername.text.toString() //grabs username

                    if (!(saved_title == "" || saved_username == "")) {

                        try { //adds password to text file
                            var fileOutputStream = openFileOutput("saved_passwords.txt", Context.MODE_PRIVATE)
                            val outputWriter = OutputStreamWriter(fileOutputStream)
                            saved_passes += "title: " + saved_title + "\n" + "username: " + saved_username + "\n" + "password: " + currPass.toString() + "\n\n"
                            outputWriter.write(saved_passes)
                            outputWriter.close()

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Toast.makeText(baseContext, "Password saved successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(baseContext, "Please fill out both fields!", Toast.LENGTH_SHORT).show()
                    }

                }
                setNegativeButton("Cancel") {dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }
                setView(dialogLayout)
                show()
            }

        }

    }

    private fun showEditTextDialog() {
        val saveButton = findViewById<Button>(R.id.buttonSave)

        saveButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
        }
    }
}
