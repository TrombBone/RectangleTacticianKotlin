package com.example.rectangletacticiankotlin

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

const val PLAYER_COUNT_DEFAULT = 2
const val FIELD_WIDTH_DEFAULT = 25
const val FIELD_HEIGHT_DEFAULT = 35

class MainActivity : AppCompatActivity(), OnFragmentListener {

    private val settingsData = SettingsData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide() ?: throw NoSuchElementException("ActionBar is lack")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        settingsData.getPref(this)

        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            replace(R.id.frame_container_start, StartFragment(this@MainActivity as OnFragmentListener))
            commit()
        }

//        Log.d("my", "playerCountMainActivity: $playerCount")
//        Log.d("my", "fieldWidthMainActivity: $fieldWidth")
//        Log.d("my", "fieldHeightMainActivity: $fieldHeight")
    }

    override fun onButtonSelected(buttonId: Int) {
        when (buttonId) {
            R.id.startButton -> {
                ConfirmStartDialogFragment().apply {
                    arguments = settingsData.sendBundle()
                    show(supportFragmentManager, "confirmStartDialog")
                }
            }
            R.id.regulationsButton -> supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                replace(R.id.frame_container_start, RegulationsFragment())
                addToBackStack(null)
                commit()
            }
            R.id.settingsButton -> supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                replace(R.id.frame_container_start, SettingsFragment(this@MainActivity as OnFragmentListener, settingsData))
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onParamsSelected(params: Map<String, String>) {
        settingsData.getFromSettings(params)
//        playerCount = if (params["playerCount"] == "true") 4 else 2
//        fieldWidth = params["fieldWidth"]?.toInt() ?: 25
//        fieldHeight = params["fieldHeight"]?.toInt() ?: 35
//        Log.d("my", "playerCountMainActivityFromSettings: $playerCount")
//        Log.d("my", "fieldWidthMainActivityFromSettings: $fieldWidth")
//        Log.d("my", "fieldHeightMainActivityFromSettings: $fieldHeight")
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
    }
}
