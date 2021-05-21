package com.example.rectangletacticiankotlin

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

const val MIN_FIELD_SIZE = 20
const val MAX_FIELD_SIZE = 60

class SettingsFragment(private val listener: OnFragmentListener) : Fragment() {

    private lateinit var playerSwitch: SwitchCompat
    private lateinit var fieldWidthTIET: TextInputEditText
    private lateinit var fieldHeightTIET: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        //(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settingsActivity_name)

        playerSwitch = view.findViewById(R.id.playersCountSwitch)
        fieldWidthTIET = view.findViewById(R.id.fieldWidthTIET)
        fieldHeightTIET = view.findViewById(R.id.fieldHeightTIET)
        load()

        fieldWidthTIET.apply {
            if (text.toString() == "") setText(FIELD_WIDTH_DEFAULT)
            doAfterTextChanged {
                setSelection(text?.length ?: 0)
                when {
                    text.toString() == "" -> setText("0")
                    text.toString().matches(Regex("""0+([0-9])+""")) -> setText(text.toString().replace("0", ""))
                    text.toString().toInt() > MAX_FIELD_SIZE -> setText(MAX_FIELD_SIZE)
                    text.toString().toInt() < MIN_FIELD_SIZE -> error = getString(R.string.fieldSizeError_text)
                }
            }
        }

        fieldHeightTIET.apply {
            if (text.toString() == "") setText(FIELD_HEIGHT_DEFAULT)
            doAfterTextChanged {
                setSelection(text?.length ?: 0)
                when {
                    text.toString() == "" -> setText("0")
                    text.toString().matches(Regex("""0+([0-9])+""")) -> setText(text.toString().replace("0", ""))
                    text.toString().toInt() > MAX_FIELD_SIZE -> setText(MAX_FIELD_SIZE)
                    text.toString().toInt() < MIN_FIELD_SIZE -> error = getString(R.string.fieldSizeError_text)
                }
            }
        }

        return view
    }

    private fun save() {
        if (fieldWidthTIET.text.toString().toInt() < MIN_FIELD_SIZE) fieldWidthTIET.setText(MIN_FIELD_SIZE)
        if (fieldHeightTIET.text.toString().toInt() < MIN_FIELD_SIZE) fieldHeightTIET.setText(MIN_FIELD_SIZE)

        val sPref = activity?.getPreferences(AppCompatActivity.MODE_PRIVATE) ?: throw IllegalStateException("Activity cannot be null")
        val editor: SharedPreferences.Editor = sPref.edit()
        editor.putString("playerCount", playerSwitch.isChecked.toString())
        editor.putString("fieldWidth", fieldWidthTIET.text.toString())
        editor.putString("fieldHeight", fieldHeightTIET.text.toString())
        editor.apply()

        listener.onParamsSelected(mapOf(
                "playerCount" to playerSwitch.isChecked.toString(),
                "fieldWidth" to fieldWidthTIET.text.toString(),
                "fieldHeight" to fieldHeightTIET.text.toString()
        ))
    }

    private fun load() {
        val sPref = activity?.getPreferences(AppCompatActivity.MODE_PRIVATE) ?: throw IllegalStateException("Activity cannot be null")
        val check = sPref.getString("playerCount", "")
        playerSwitch.isChecked = check == "true"
        val fieldWidth = sPref.getString("fieldWidth", "")
        fieldWidthTIET.setText(fieldWidth)
        val fieldHeight = sPref.getString("fieldHeight", "")
        fieldHeightTIET.setText(fieldHeight)
    }

    override fun onDestroy() {
        super.onDestroy()
        save()
    }
}