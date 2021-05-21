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
        val myActivity = activity ?: throw IllegalStateException("Activity cannot be null")
        return myActivity.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.gameOver)
                .setMessage("${activity?.getString(R.string.finishDialog_text)}\n" +
                        "Игрок 1: ${arguments?.getInt("player1")}\n" +
                        "Игрок 2: ${arguments?.getInt("player2")}\n" +
                        "Игрок 3: ${arguments?.getInt("player3")}\n" +
                        "Игрок 4: ${arguments?.getInt("player4")}"
                )
                .setCancelable(true)
                .setPositiveButton(R.string.endGame_text) { _, _ ->
                    val fm = myActivity.supportFragmentManager
                    for (i in fm.fragments) {
                        fm.beginTransaction().remove(i)
                    }
                    Intent(myActivity, MainActivity::class.java).also{ intent ->
                        startActivity(intent)
                    }
                    myActivity.finish()
                    MainGameActivity.cleanAllData()
                }
                .setNegativeButton(R.string.showField_text) { _, _ ->
                    val myDialog = dialog ?: throw IllegalStateException("Dialog cannot be null")
                    myDialog.hide()
                    if (!myDialog.isShowing) {
                        Timer().schedule(7000L) {
                            myActivity.runOnUiThread {
                                show(myActivity.supportFragmentManager, "finishDialog")
                            }
                        }
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}