package com.example.rectangletacticiankotlin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*
import kotlin.concurrent.schedule

class FinishDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.gameOver)
                .setMessage("${R.string.finishDialog_text} \n" +
                        "${arguments?.getInt("player1")} \n " +
                        "${arguments?.getInt("player2")} \n" +
                        "${arguments?.getInt("player3")} \n" +
                        "${arguments?.getInt("player4")}")
                .setCancelable(true)
                .setPositiveButton(R.string.endGame_text) { _, _ ->
                    Intent(activity, MainActivity::class.java).also { it ->
                        startActivity(it)
                    }
                    activity?.finish() ?: throw IllegalStateException("Activity cannot be null")
                }
                .setNegativeButton(R.string.showField_text) { _, _ ->
                    dialog?.dismiss() ?: throw IllegalStateException("Dialog cannot be null")
                    if (!(dialog?.isShowing
                            ?: throw IllegalStateException("Dialog cannot be null"))
                    ) Timer().schedule(8000L) {
                        dialog?.show() ?: throw IllegalStateException("Dialog cannot be null")
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}