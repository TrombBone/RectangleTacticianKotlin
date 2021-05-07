package com.example.rectangletacticiankotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class MyDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("my", "arguments: $arguments")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.warning)
                .setMessage(R.string.warning_text)
                .setCancelable(true)
                .setPositiveButton(R.string.yes) { _, _ ->
                    Intent(activity, MainGameActivity::class.java).also { it ->
                        //val mainActivity = MainActivity()
                        val playerCount = arguments?.getInt("playerCount")
                        val fieldWidth = arguments?.getInt("fieldWidth")
                        val fieldHeight = arguments?.getInt("fieldHeight")

                        it.putExtra("playerCount", playerCount)
                        it.putExtra("fieldWidth", fieldWidth)
                        it.putExtra("fieldHeight", fieldHeight)
                        Log.d("my", "playerCountDialog: $playerCount")
                        Log.d("my", "fieldWidthMainDialog: $fieldWidth")
                        Log.d("my", "fieldHeightMainDialog: $fieldHeight")
                        startActivity(it)
                    }
                    activity?.finish() ?: throw IllegalStateException("Activity cannot be null")
                }
                .setNegativeButton(R.string.no) { _, _ ->
                    dialog?.cancel() ?: throw IllegalStateException("Dialog cannot be null")
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}