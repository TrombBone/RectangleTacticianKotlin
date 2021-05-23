package com.example.rectangletacticiankotlin

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

const val TAG_PLAYER_COUNT = "playerCount"
const val TAG_FIELD_WIDTH = "fieldWidth"
const val TAG_FIELD_HEIGHT = "fieldHeight"

class SettingsData {

    var playerCount = 0
    var fieldWidth = 0
    var fieldHeight = 0

    fun getPref(activity: FragmentActivity) {
        activity.getPreferences(MODE_PRIVATE).apply {
            playerCount = getString(TAG_PLAYER_COUNT, null)?.toInt() ?: PLAYER_COUNT_DEFAULT
            fieldWidth = getString(TAG_FIELD_WIDTH, null)?.toInt() ?: FIELD_WIDTH_DEFAULT
            fieldHeight = getString(TAG_FIELD_HEIGHT, null)?.toInt() ?: FIELD_HEIGHT_DEFAULT
        }
    }

    fun setPref(activity: FragmentActivity) {
        val sPref = activity.getPreferences(AppCompatActivity.MODE_PRIVATE) ?: throw IllegalStateException("Activity cannot be null")
        val editor: SharedPreferences.Editor = sPref.edit()
        editor.putString(TAG_PLAYER_COUNT, playerCount.toString())
        editor.putString(TAG_FIELD_WIDTH, fieldWidth.toString())
        editor.putString(TAG_FIELD_HEIGHT, fieldHeight.toString())
        editor.apply()
    }
}