package com.do_big.diginotes.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.do_big.diginotes.R
import com.do_big.diginotes.model.Note
import com.do_big.diginotes.utils.AppConstant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class Description : AppCompatActivity() {
    private var back: LinearLayout? = null
    private val des: String? = null
    private lateinit var content: TextView
    private var tts: TextToSpeech? = null
    private val data: String? = null
    private var ttsStatus = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.right, R.anim.fadeout)
        setContentView(R.layout.activity_description)
        content = findViewById(R.id.content)
        back = findViewById(R.id.background)

        tts = TextToSpeech(
            applicationContext
        ) { status ->
            ttsStatus = status
            tts!!.setLanguage(Locale.UK)
        }
        val note = intent.getParcelableExtra<Note>(AppConstant.ITEM_CLICKED_PARCEL)
        content.append(note!!.noteDescription)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_description)
        if (note.noteTitle != null) {
            toolbar.title = note.noteTitle
        }
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
        fab.setOnClickListener { view: View? ->
            tts!!.speak(
                note.noteDescription, TextToSpeech.QUEUE_FLUSH, null
            )
        }
    }

    override fun onPause() {
        tts!!.stop()
        finish()
        super.onPause()
    }

    override fun onBackPressed() {
        tts!!.stop()
        super.onBackPressed()
    }


    override fun onResume() {
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val textsize = settings.getString("TextSize", "18")!!
        Log.d("textSize", textsize)
        content.textSize = textsize.toFloat()
        val nightmode = settings.getBoolean("nightMode", false)
        if (nightmode) {
            content.setTextColor(Color.WHITE)
            back!!.setBackgroundColor(Color.BLACK)

            // content.append("true");
        } else {
            content.setTextColor(Color.BLACK)
            back!!.setBackgroundColor(Color.WHITE)
            //content.append("false");
        }
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_description, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item
        if (i.itemId == R.id.action_share) {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                content.text.toString() + "\n\tDigiNote"
            )
            sendIntent.setType("text/plain")
            startActivity(sendIntent)
            return true
        } else if (i.itemId == R.id.action_edit) {
            val share = Intent(this@Description, HomeActivity::class.java)
            share.setAction(Intent.ACTION_SEND)
            share.putExtra(Intent.EXTRA_TEXT, data)
            share.setType("text/plain")
            startActivity(share)
            return true
        } else if (i.itemId == R.id.action_settings) {
            startActivity(Intent(this@Description, SettingsActivity::class.java))
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}
