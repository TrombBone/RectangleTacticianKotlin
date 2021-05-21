package com.example.rectangletacticiankotlin

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
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
            playerCount = if (getString(TAG_PLAYER_COUNT, "false") == "true") 4 else PLAYER_COUNT_DEFAULT
            fieldWidth = getString(TAG_FIELD_WIDTH, null)?.toInt() ?: FIELD_WIDTH_DEFAULT
            fieldHeight = getString(TAG_FIELD_HEIGHT, null)?.toInt() ?: FIELD_HEIGHT_DEFAULT
        }
    }

    fun setPref(activity: FragmentActivity, elementsStates: List<String>): Map<String, String> {
        val sPref = activity.getPreferences(AppCompatActivity.MODE_PRIVATE) ?: throw IllegalStateException("Activity cannot be null")
        val editor: SharedPreferences.Editor = sPref.edit()
        editor.putString(TAG_PLAYER_COUNT, elementsStates[0])
        editor.putString(TAG_FIELD_WIDTH, elementsStates[1])
        editor.putString(TAG_FIELD_HEIGHT, elementsStates[2])
        editor.apply()

        return mapOf(TAG_PLAYER_COUNT to elementsStates[0], TAG_FIELD_WIDTH to elementsStates[1], TAG_FIELD_HEIGHT to elementsStates[2])
    }

    fun sendBundle(): Bundle {
        val bundle = Bundle()
        bundle.also {
            it.putInt(TAG_PLAYER_COUNT, playerCount)
            it.putInt(TAG_FIELD_WIDTH, fieldWidth)
            it.putInt(TAG_FIELD_HEIGHT, fieldHeight)
        }
        return bundle
    }

    fun getFromSettings(params: Map<String, String>) {
        playerCount = if (params[TAG_PLAYER_COUNT] == "true") 4 else 2
        fieldWidth = params[TAG_FIELD_WIDTH]?.toInt() ?: 25
        fieldHeight = params[TAG_FIELD_HEIGHT]?.toInt() ?: 35
    }

}