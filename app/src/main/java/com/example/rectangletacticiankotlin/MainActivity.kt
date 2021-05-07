package com.example.rectangletacticiankotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnFragmentListener {

    //companion object {
        var playerCount = 0
        var fieldWidth = 0
        var fieldHeight = 0
    //}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide() ?: throw NoSuchElementException("ActionBar is lack")

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container_start, StartFragment())
            commit()
        }


    }

//    override fun onButtonSelected(buttonId: Int) {
//        when (buttonId) {
//            R.id.startButton -> {}
//            R.id.regulationsButton -> {}
//            R.id.settingsButton -> {}
//        }
//    }

    override fun onParamsSelected(params: Map<String, String>) {
        playerCount = if (params["playerCount"] == "true") 4 else 2
        fieldWidth = params["fieldWidth"]?.toInt() ?: 25
        fieldHeight = params["fieldHeight"]?.toInt() ?: 35

        Log.d("my", "playerCountMainActivity: $playerCount")
        Log.d("my", "fieldWidthMainActivity: $fieldWidth")
        Log.d("my", "fieldHeightMainActivity: $fieldHeight")

        val myDialogFragment = MyDialogFragment()
        val myDialogFragmentBundle = Bundle()
        myDialogFragmentBundle.putInt("playerCount", playerCount)
        myDialogFragmentBundle.putInt("fieldWidth", fieldWidth)
        myDialogFragmentBundle.putInt("fieldHeight", fieldHeight)
        Log.d("my", "myDialogFragmentBundle: $myDialogFragmentBundle")
        myDialogFragment.arguments = myDialogFragmentBundle
        Log.d("my", "myDialogFragment.arguments: ${myDialogFragment.arguments}")

    }

    override fun onBackPressed() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            replace(R.id.frame_container_start, StartFragment())
            commit()
        }
    }
}
