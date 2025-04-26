package com.do_big.diginotes.activity

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.do_big.diginotes.R
import com.do_big.diginotes.adapter.OnNoteItemClickListener
import com.do_big.diginotes.adapter.SearchAdapter
import com.do_big.diginotes.databinding.ActivityHomeBinding
import com.do_big.diginotes.model.Note
import com.do_big.diginotes.model.NoteViewModel
import com.do_big.diginotes.model.SharedViewModel
import com.do_big.diginotes.utils.AppConstant
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import java.util.Locale

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnNoteItemClickListener {
    private val REQ_CODE_SPEECH_INPUT = 100

    private var mAdapter: SearchAdapter? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var viewModel: NoteViewModel? = null
    private var sharedViewModel: SharedViewModel? = null
    private var binding: ActivityHomeBinding? = null
    private lateinit var buttonShareApp: MaterialButton
    private lateinit var buttonRateApp: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val layoutView: View = binding!!.root
        setContentView(layoutView)
        viewModel =
            ViewModelProvider.AndroidViewModelFactory(this.application).create<NoteViewModel>(
                NoteViewModel::class.java
            )
        sharedViewModel = ViewModelProvider(this@HomeActivity).get(
            SharedViewModel::class.java
        )
        binding!!.appBarContentMain.contentMain.myRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this)
        binding!!.appBarContentMain.contentMain.myRecyclerView.layoutManager = mLayoutManager


        NoteViewModel.allNotes.observe(this, object : Observer<List<Note?>?> {
            override fun onChanged(value: List<Note?>?) {
                mAdapter = SearchAdapter(value, this@HomeActivity)
                binding!!.appBarContentMain.contentMain.myRecyclerView.adapter = mAdapter
            }
        })
        binding!!.appBarContentMain.contentMain.fabSearch.setOnClickListener { showBottomSheetDialog() }

        val toolbar = findViewById<Toolbar>(R.id.toolbar_home)


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val view = navigationView.getHeaderView(0)
        buttonShareApp = view.findViewById(R.id.button_share)
        buttonRateApp = view.findViewById(R.id.button_rate)
        buttonShareApp.setOnClickListener(View.OnClickListener { v: View? -> shareApp() })
        buttonRateApp.setOnClickListener(View.OnClickListener { v: View? -> rateApp() })
    }


    private fun showBottomSheetDialog() {
        val fragment = AddNotesFragment()
        fragment.show(supportFragmentManager, "add")
    }


    override fun onResume() {
        super.onResume()

        sharedViewModel!!.voiceInput.observe(this, object : Observer<Boolean?> {
            override fun onChanged(value: Boolean?) {
                promptSpeechInput()
                sharedViewModel!!.setVoiceInput(false)
            }
        })
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            val b1 = AlertDialog.Builder(this@HomeActivity)
            b1.setTitle("Exit")
            b1.setMessage("Close Digi-Note")
            b1.setPositiveButton(
                "No"
            ) { arg0: DialogInterface, arg1: Int -> arg0.cancel() }
            b1.setNegativeButton(
                "Yes"
            ) { arg0: DialogInterface?, arg1: Int -> this@HomeActivity.finish() }
            overridePendingTransition(R.anim.right, R.anim.fadeout)
            b1.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_action_exit) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_how_it_work) {
            startActivity(Intent(this@HomeActivity, WelcomeActivity::class.java))
        } else if (id == R.id.button_rate) {
            rateApp()
            return true
        } else if (id == R.id.button_share) {
            shareApp()
            return true
        } else if (id == R.id.nav_setting) {
            startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun rateApp() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.PLAY_STORE_PATH)))
        overridePendingTransition(R.anim.right, R.anim.fadeout)
    }

    private fun shareApp() {
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "DigiNotes : https://play.google.com/store/apps/details?id=com.do_big.diginotes"
        )
        sendIntent.setType("text/plain")
        startActivity(sendIntent)
        overridePendingTransition(R.anim.right, R.anim.fadeout)
    }


    fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.speech_prompt)
        )
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == RESULT_OK && null != data) {
                    val result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    sharedViewModel!!.setVoiceInputNoteDescription(result!![0])
                    showBottomSheetDialog()
                    // etMultiline.setText(result.get(0));
                }
            }

        }
    }


    override fun onNoteItemClick(adapterPosition: Int, note: Note, viewId: Int) {
        if (viewId == R.id.btn_fav) {
            //TODO add lottie animation
            note.isFav = !note.isFav

            NoteViewModel.update(note)
        } else if (viewId == R.id.tv_edit) {
            sharedViewModel!!.setSelectedItem(note)
            sharedViewModel!!.edit = true
            showBottomSheetDialog()
        } else if (viewId == R.id.tv_delete) {
            NoteViewModel.delete(note)
        } else {
            //on NoteItem clicked
            val intent = Intent(this@HomeActivity, Description::class.java)
            intent.putExtra(AppConstant.ITEM_CLICKED_PARCEL, note)
            this.startActivity(intent)
        }
    }
}
