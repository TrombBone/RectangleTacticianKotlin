package com.example.rectangletacticiankotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

const val MIN_FIELD_SIZE = 20
const val MAX_FIELD_SIZE = 60

class SettingsFragment(private val listener: OnFragmentListener, val settingsData: SettingsData) : Fragment() {

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
            if (text.toString() == "") setText(FIELD_WIDTH_DEFAULT.toString())
            doAfterTextChanged {
                setSelection(text?.length ?: 0)
                when {
                    text.toString() == "" -> setText("0")
                    text.toString().matches(Regex("""0+([0-9])+""")) -> setText(text.toString().replace("0", ""))
                    text.toString().toInt() > MAX_FIELD_SIZE -> setText(MAX_FIELD_SIZE.toString())
                    text.toString().toInt() < MIN_FIELD_SIZE -> error = getString(R.string.fieldSizeError_text)
                }
            }
        }

        fieldHeightTIET.apply {
            if (text.toString() == "") setText(FIELD_HEIGHT_DEFAULT.toString())
            doAfterTextChanged {
                setSelection(text?.length ?: 0)
                when {
                    text.toString() == "" -> setText("0")
                    text.toString().matches(Regex("""0+([0-9])+""")) -> setText(text.toString().replace("0", ""))
                    text.toString().toInt() > MAX_FIELD_SIZE -> setText(MAX_FIELD_SIZE.toString())
                    text.toString().toInt() < MIN_FIELD_SIZE -> error = getString(R.string.fieldSizeError_text)
                }
            }
        }

        return view
    }

    private fun save() {
        if (fieldWidthTIET.text.toString().toInt() < MIN_FIELD_SIZE) fieldWidthTIET.setText(MIN_FIELD_SIZE.toString())
        if (fieldHeightTIET.text.toString().toInt() < MIN_FIELD_SIZE) fieldHeightTIET.setText(MIN_FIELD_SIZE.toString())

        settingsData.apply {
            fieldWidth = fieldWidthTIET.text.toString().toInt()
            fieldHeight = fieldHeightTIET.text.toString().toInt()
            playerCount = if (playerSwitch.isChecked) 4 else PLAYER_COUNT_DEFAULT
            setPref(requireActivity())
        }
    }

    private fun load() {
        settingsData.apply {
            playerSwitch.isChecked = playerCount == 4
            fieldWidthTIET.setText(fieldWidth.toString())
            fieldHeightTIET.setText(fieldHeight.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        save()
    }
}