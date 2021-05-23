package com.example.rectangletacticiankotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmStartDialogFragment(private val settingsData: SettingsData) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val myActivity = requireActivity()
        return myActivity.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.warning)
                .setMessage(R.string.warning_text)
                .setCancelable(true)
                .setPositiveButton(R.string.yes) { _, _ ->
                    Intent(myActivity, MainGameActivity::class.java).also { intent ->
//                        Log.d("my", "argumentsMyDialogFragment: $arguments")
                        intent.putExtra(TAG_PLAYER_COUNT, settingsData.playerCount)
                        intent.putExtra(TAG_FIELD_WIDTH, settingsData.fieldWidth)
                        intent.putExtra(TAG_FIELD_HEIGHT, settingsData.fieldHeight)
                        startActivity(intent)
                    }
                    myActivity.finish()
                }
                .setNegativeButton(R.string.no) { _, _ ->
                    dialog?.cancel() ?: throw IllegalStateException("Dialog cannot be null")
                }
            builder.create()
        }
    }

}